package com.zengjie.algorithm.search;

/**
 * （1）每个节点或者是黑色，或者是红色。
 * （2）根节点是黑色。
 * （3）每个叶子节点（NIL）是黑色。 [注意：这里叶子节点，是指为空(NIL或NULL)的叶子节点！]
 * （4）如果一个节点是红色的，则它的子节点必须是黑色的。不可以同时存在两个红色节点相连
 * （5）从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点。如果一个节点存在黑色子节点，那么该节点肯定有两个子节点
 *
 * @author jie.zeng
 * @version 1.0
 * @since 2021/6/5 8:36
 */
public class RedBlackTree<K extends Comparable<K>, V> {

    public RedBlackTreeNode<K, V> getRoot() {
        return root;
    }

    private RedBlackTreeNode<K, V> root;

    public void insert(K key, V value) {
        //查找到的位置的父节点
        RedBlackTreeNode<K, V> parent = null;
        RedBlackTreeNode<K, V> findRedBlackTreeNode = root;
        while (findRedBlackTreeNode != null) {
            parent = findRedBlackTreeNode;
            int compare = key.compareTo(findRedBlackTreeNode.key);
            if (compare == 0) {
                //相等，替换值
                System.out.printf("根据key【%s】,找到相同的，value由【%s】更改为【%s】\n", key, findRedBlackTreeNode.value, value);
                findRedBlackTreeNode.value = value;
                return;
            } else if (compare > 0) {
                //插入的key大于当前节点
                findRedBlackTreeNode = findRedBlackTreeNode.right;
            } else {
                findRedBlackTreeNode = findRedBlackTreeNode.left;
            }
        }

        if (parent == null) {
            //根节点为黑色
            root = new RedBlackTreeNode<>(key, value, false);
            System.out.printf("首次插入，根节点key【%s】,value为【%s】\n", key, value);
            return;
        }

        //非根节点插入时，都插入红色，因为插入红色有可能不会破坏黑色平衡
        RedBlackTreeNode<K, V> redBlackTreeNode = new RedBlackTreeNode<>(key, value, true);
        System.out.printf("插入，节点key【%s】,value为【%s】\n", key, value);
        redBlackTreeNode.parent = parent;
        //判断插在父节点的位置
        System.out.printf("插入的位置的父节点【%s】\n", parent.key);

        int compare = key.compareTo(parent.key);
        if (compare > 0) {
            //插入节点比父节点大，插在父节点的右节点
            parent.right = redBlackTreeNode;
            System.out.print("插在父节点的右节点\n");
        } else {
            parent.left = redBlackTreeNode;
            System.out.print("插在父节点的左节点\n");
        }
        balanceInsertBy234(redBlackTreeNode);
    }

    public V remove(K key) {
        RedBlackTreeNode<K, V> redBlackTreeNode = getNode(key);
        if (redBlackTreeNode == null) {
            return null;
        }
        V value = redBlackTreeNode.value;

        deleteNode(redBlackTreeNode);
        return value;
    }

    /**
     * 删除
     * 1.一个子节点都没有，直接删除
     * 2.有且存在一个子节点，用存在的子节点来代替
     * 3.有俩个子节点，需要找到前驱节点或后继节点来替代
     *
     * @param redBlackTreeNode 需要删除的节点
     */
    private void deleteNode(RedBlackTreeNode<K, V> redBlackTreeNode) {
        if (rightOf(redBlackTreeNode) != null && leftOf(redBlackTreeNode) != null) {
            //有俩个子节点
            //获取后继节点
            RedBlackTreeNode<K, V> successor = getSuccessor(redBlackTreeNode);
            redBlackTreeNode.key = successor.key;
            redBlackTreeNode.value = successor.value;
            redBlackTreeNode = successor;
            //被删除的节点被替换为后继节点.也就是所后面只管执行删除redBlackTreeNode这个的代码，无需关心有俩个子节点的情况
        }

        //节点被删除后的替代
        RedBlackTreeNode<K, V> replacement = redBlackTreeNode.left == null ? redBlackTreeNode.right : redBlackTreeNode.left;

        if (replacement != null) {
            //如果存在替代，也就是所redBlackTreeNode被删除后，replacement把这个位置站住，所以需要针对replacement进行平衡
            replacement.parent = redBlackTreeNode.parent;
            if (redBlackTreeNode.parent == null) {
                root = replacement;
            } else if (redBlackTreeNode == redBlackTreeNode.parent.left) {
                redBlackTreeNode.parent.left = replacement;
            } else {
                redBlackTreeNode.parent.right = replacement;
            }
            redBlackTreeNode.parent = redBlackTreeNode.left = redBlackTreeNode.right = null;

            if (redBlackTreeNode.color == BLACK) {
                balanceRemove(replacement);
            }
        } else if (redBlackTreeNode.parent == null) {
            root = null;
        } else {
            //没有子节点，直接删除
            if (redBlackTreeNode.color == BLACK) {
                //调整
                balanceRemove(redBlackTreeNode);
            }

            //调整过后，redBlackTreeNode.parent再次等于nulL
            if (redBlackTreeNode != null) {
                if (redBlackTreeNode == redBlackTreeNode.parent.left) {
                    redBlackTreeNode.parent.left = null;
                } else {
                    redBlackTreeNode.parent.right = null;
                }
            }


            redBlackTreeNode.parent = redBlackTreeNode.left = redBlackTreeNode.right = null;
        }
    }

    /**
     * 另外删除时，由于删除时进行了处理，指挥删除到2-3-4的叶子节点
     * 删除时，删除对应2-3-4树，删除3，4节点的一个，不需要额外处理，
     *
     * @param redBlackTreeNode 调整的节点
     * @author zengjie
     * @since 2021/6/5 20:25
     */
    private void balanceRemove(RedBlackTreeNode<K, V> redBlackTreeNode) {
        while (redBlackTreeNode != root && colorOf(redBlackTreeNode) == BLACK) {
            //找兄弟接
            RedBlackTreeNode<K, V> brother;
            if (redBlackTreeNode == leftOf(parentOf(redBlackTreeNode))) {
                brother = rightOf(parentOf(redBlackTreeNode));
                //判断兄弟是否是真兄弟
                if (colorOf(brother) == RED) {
                    setColor(brother, BLACK);
                    setColor(parentOf(redBlackTreeNode), RED);
                    leftRotate(parentOf(redBlackTreeNode));
                    //现在才是真兄弟
                    brother = rightOf(parentOf(redBlackTreeNode));
                }
                //兄弟没得借, 分析：兄弟都是真的黑，比当前节点高一级，不可能，更不可一黑一红
                if (colorOf(leftOf(brother)) == BLACK && colorOf(rightOf(brother)) == BLACK) {
                    //自己被删除，让兄弟节点也变红，此时子树平衡
                    setColor(brother, RED);
                    //继续往上平衡
                    redBlackTreeNode = parentOf(redBlackTreeNode);
                } else {
                    //3节点或4节点 , 兄弟为右子树，但是兄弟的右子树为空，虽然从血脉上来所是真正的兄弟，但是不是我想要的兄弟，我想要的兄弟需要有右子树，需要通右旋兄弟节点实现（有点像RL的情况）
                    if (colorOf(rightOf(brother)) == BLACK) {
                        //兄弟的右节点为空，通过对兄弟进行右旋，将兄弟的左节点上升为redBlackTreeNode的兄弟节点，而当前节点转换为最新兄弟节点的右节点
                        setColor(leftOf(brother), BLACK);
                        setColor(brother, RED);
                        rightRotate(brother);
                        //更新为新的兄弟
                        brother = rightOf(parentOf(redBlackTreeNode));
                    }

                    //上面的方法已经将没有右节点的情况转换为右节点的情况，现在就可以只考虑有右子树的情况了（有点像RR的情况）
                    setColor(brother, colorOf(parentOf(redBlackTreeNode)));
                    //下面俩个节点parentOf(redBlackTreeNode)，rightOf(brother)在兄弟左旋后变为他的俩个节点，故染色为黑色
                    setColor(parentOf(redBlackTreeNode), BLACK);
                    setColor(rightOf(brother), BLACK);

                    leftRotate(parentOf(redBlackTreeNode));
                    redBlackTreeNode = root;
                }
            } else {//与上面相反
                brother = leftOf(parentOf(redBlackTreeNode));
                if (colorOf(brother) == RED) {
                    setColor(brother, BLACK);
                    setColor(parentOf(redBlackTreeNode), RED);
                    rightRotate(parentOf(redBlackTreeNode));
                    brother = leftOf(parentOf(redBlackTreeNode));
                }
                if (colorOf(leftOf(brother)) == BLACK && colorOf(rightOf(brother)) == BLACK) {
                    setColor(brother, RED);
                    redBlackTreeNode = parentOf(redBlackTreeNode);
                } else {
                    if (colorOf(leftOf(brother)) == BLACK) {
                        setColor(rightOf(brother), BLACK);
                        setColor(brother, RED);
                        leftRotate(brother);
                        brother = leftOf(parentOf(redBlackTreeNode));
                    }

                    setColor(brother, colorOf(parentOf(redBlackTreeNode)));
                    setColor(parentOf(redBlackTreeNode), BLACK);
                    setColor(leftOf(brother), BLACK);

                    rightRotate(parentOf(redBlackTreeNode));
                    redBlackTreeNode = root;
                }
            }
        }

        //替代节点是染黑，则直接染黑，补偿删除的黑色的节点，这样红黑树依然保持平衡
        setColor(redBlackTreeNode, BLACK);
    }

    public RedBlackTreeNode<K, V> getNode(K key) {
        RedBlackTreeNode<K, V> node = root;
        while (node != null) {
            int compare = key.compareTo(node.key);
            if (compare == 0) {
                return node;
            } else if (compare > 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return null;
    }

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    /**
     * 红黑树规定null时，为黑色
     */
    public boolean colorOf(RedBlackTreeNode<K, V> redBlackTreeNode) {
        return redBlackTreeNode == null ? BLACK : redBlackTreeNode.color;
    }

    public void setColor(RedBlackTreeNode<K, V> redBlackTreeNode, boolean color) {
        if (redBlackTreeNode != null) {
            redBlackTreeNode.color = color;
        }
    }

    public RedBlackTreeNode<K, V> parentOf(RedBlackTreeNode<K, V> redBlackTreeNode) {
        return redBlackTreeNode == null ? null : redBlackTreeNode.parent;
    }

    public RedBlackTreeNode<K, V> rightOf(RedBlackTreeNode<K, V> redBlackTreeNode) {
        return redBlackTreeNode == null ? null : redBlackTreeNode.right;
    }

    public RedBlackTreeNode<K, V> leftOf(RedBlackTreeNode<K, V> redBlackTreeNode) {
        return redBlackTreeNode == null ? null : redBlackTreeNode.left;
    }

    /**
     * 通过2-3-4树进行平衡操作
     * 对应关系参考文档https://blog.csdn.net/jiangtianjiao/article/details/88969899
     */
    private void balanceInsertBy234(RedBlackTreeNode<K, V> redBlackTreeNode) {
        redBlackTreeNode.color = true;

        while (redBlackTreeNode != null && redBlackTreeNode != root && colorOf(parentOf(redBlackTreeNode)) != BLACK) {
            if (parentOf(redBlackTreeNode) == leftOf(parentOf(parentOf(redBlackTreeNode)))) {
                //父节点作为左子树
                //叔叔是否为红色
                RedBlackTreeNode<K, V> uncle = rightOf(parentOf(parentOf(redBlackTreeNode)));
                if (RED == colorOf(uncle)) {
                    setColor(uncle, BLACK);
                    setColor(parentOf(redBlackTreeNode), BLACK);
                    setColor(parentOf(parentOf(redBlackTreeNode)), RED);
                    //完成后，根据爷爷再次进行平衡
                    redBlackTreeNode = parentOf(parentOf(redBlackTreeNode));
                } else {
                    if (redBlackTreeNode == rightOf(parentOf(redBlackTreeNode))) {
                        //左右的情况，先转换为左左
                        //根据父节点进行左旋转
                        redBlackTreeNode = parentOf(redBlackTreeNode);
                        //旋转后，redBlackTreeNode又跑了插入点（类似）
                        leftRotate(redBlackTreeNode);
                    }

                    //针对左左
                    setColor(parentOf(redBlackTreeNode), BLACK);
                    setColor(parentOf(parentOf(redBlackTreeNode)), RED);
                    rightRotate(parentOf(parentOf(redBlackTreeNode)));
                }
            } else {
                RedBlackTreeNode<K, V> uncle = leftOf(parentOf(parentOf(redBlackTreeNode)));
                if (RED == colorOf(uncle)) {
                    setColor(uncle, BLACK);
                    setColor(parentOf(redBlackTreeNode), BLACK);
                    setColor(parentOf(parentOf(redBlackTreeNode)), RED);
                    //完成后，根据爷爷再次进行平衡
                    redBlackTreeNode = parentOf(parentOf(redBlackTreeNode));
                } else {
                    if (redBlackTreeNode == leftOf(parentOf(redBlackTreeNode))) {
                        //右左的情况，先转换为右右
                        //根据父节点进行左旋转
                        redBlackTreeNode = parentOf(redBlackTreeNode);
                        //旋转后，redBlackTreeNode又跑了插入点（类似）
                        rightOf(redBlackTreeNode);
                    }

                    //针对右右
                    setColor(parentOf(redBlackTreeNode), BLACK);
                    setColor(parentOf(parentOf(redBlackTreeNode)), RED);
                    leftRotate(parentOf(parentOf(redBlackTreeNode)));
                }
            }
        }

        root.color = BLACK;
    }

    /**
     * 查找前驱节点，即小于节点的最大值
     *
     * @param redBlackTreeNode 需要获取前驱节点的节点
     * @return 前驱节点
     * @author zengjie
     * @since 2021/6/5 18:20
     */
    private RedBlackTreeNode<K, V> getPredecessor(RedBlackTreeNode<K, V> redBlackTreeNode) {
        if (redBlackTreeNode == null) {
            return null;
        }

        RedBlackTreeNode<K, V> left = leftOf(redBlackTreeNode);
        if (left == null) {
            RedBlackTreeNode<K, V> parent = redBlackTreeNode.parent;
            //没有左子树
            while (parent != null && redBlackTreeNode == leftOf(parent)) {
                redBlackTreeNode = parent;
                parent = parentOf(parent);
            }
            return parent;
        }
        while (left.right != null) {
            left = left.right;
        }

        return left;
    }

    /**
     * 查找后继节点，即大于节点的最小值
     *
     * @param redBlackTreeNode 需要获取后继节点的节点
     * @return 后继节点
     * @author zengjie
     * @since 2021/6/5 18:20
     */
    private RedBlackTreeNode<K, V> getSuccessor(RedBlackTreeNode<K, V> redBlackTreeNode) {
        if (redBlackTreeNode == null) {
            return null;
        }

        RedBlackTreeNode<K, V> right = rightOf(redBlackTreeNode);
        if (right == null) {
            RedBlackTreeNode<K, V> parent = redBlackTreeNode.parent;
            //没有右子树
            while (parent != null && redBlackTreeNode == rightOf(parent)) {
                redBlackTreeNode = parent;
                parent = parentOf(parent);
            }
            return parent;
        }
        while (right.left != null) {
            right = right.left;
        }

        return right;
    }

    /**
     * 情况1：插入节点为根节点，不需要处理，插入时，已经设为黑色
     * 情况2：插入节点已存在，不需要处理
     * 情况3：插入节点的父节点为黑色，不要再次平衡，因为未打破平衡
     * 情况4：插入节点的父节点为红色
     * ----情况4.1：插入节点的叔叔为红色，父叔双红，将父叔变为黑色，将爷爷设置为红色,再以爷爷节点进行下一步处理(虽然没有破坏爷爷以下的黑色平衡，但是将爷爷设置为红色，可能造成红色相连)
     * ----情况4.2: 插入节点的叔叔为不存在或者黑色,父节点为左节点
     * ----------情况4.2.1：插入节点为父节点的左节点(LL)，父节点变黑色，爷爷变为红色，以爷爷节点右旋就行了(虽然爷爷设置为红色，但是叔叔不出在或者为黑色，并未破坏黑色平衡)
     * ----------情况4.2.2：插入节点为父节点的右节点(LR),以父节点左旋转,再平衡父节点
     * ----情况4.3: 插 入节点的叔叔为不存在或者黑色,父节点为右节点
     * ----------情况4.3.2：插入节点为父节点的右节点(RR)，父节点变黑色，爷爷变为红色，以爷爷节点左旋就行了
     * ----------情况4.3.1：插入节点为父节点的左节点(RL)，以父节点右旋旋转,再平衡父节点
     *
     * @param redBlackTreeNode 平衡节点
     */
    public void balanceInsertion(RedBlackTreeNode<K, V> redBlackTreeNode) {
        if (redBlackTreeNode == null) {
            return;
        }
        root.color = false;

        RedBlackTreeNode<K, V> parent = redBlackTreeNode.parent;
        if (parent == null) {
            System.out.printf("平衡节点【%s】的父节点为null，跳过平衡", redBlackTreeNode.key);
            return;
        }

        if (!parent.color) {
            System.out.printf("平衡节点【%s】的父节点为黑色节点，跳过平衡", parent.key);
            return;
        }

        RedBlackTreeNode<K, V> gparent = parent.parent;
        if (gparent == null) {
            throw new RuntimeException(String.format("红节点肯定不为根节点，所以肯定有爷爷节点，请检查算法正确性，父节点【%s】", parent.key));
        }

        RedBlackTreeNode<K, V> uncle;

        if (gparent.right == parent) {
            uncle = gparent.left;
            if (uncle == null || !uncle.color) {
                if (parent.right == redBlackTreeNode) {
                    //RR
                    parent.color = false;
                    gparent.color = true;
                    leftRotate(gparent);
                } else {
                    //RL
                    rightRotate(parent);
                    balanceInsertion(parent);
                }
            } else {
                parent.color = false;
                uncle.color = false;
                gparent.color = true;
                balanceInsertion(gparent);
            }

        } else {
            uncle = gparent.right;
            if (uncle == null || !uncle.color) {
                if (parent.right == redBlackTreeNode) {
                    //LR
                    leftRotate(parent);
                    balanceInsertion(parent);
                } else {
                    //LL
                    parent.color = false;
                    gparent.color = true;
                    rightRotate(gparent);
                }
            } else {
                parent.color = false;
                uncle.color = false;
                gparent.color = true;
                balanceInsertion(gparent);
            }
        }
    }


    /**
     * 右旋
     * <p>
     * 左孩子的右孩子作为当前节点的左节点
     * 左孩子作为当前节点的父节点
     * 当前节点成为左孩子的右节点
     *
     * @param redBlackTreeNode 被右旋的节点
     */
    public void rightRotate(RedBlackTreeNode<K, V> redBlackTreeNode) {
        if (redBlackTreeNode == null) {
            System.out.println("旋转节点为null，跳过右旋");
            return;
        }
        RedBlackTreeNode<K, V> left = redBlackTreeNode.left;

        if (left == null) {
            System.out.println("左孩子为null，跳过右旋");
            return;
        }

        RedBlackTreeNode<K, V> leftRight = left.right;
        redBlackTreeNode.left = leftRight;
        if (leftRight != null) {
            leftRight.parent = redBlackTreeNode;
        }

        RedBlackTreeNode<K, V> parent = redBlackTreeNode.parent;
        left.parent = parent;
        if (parent == null) {
            root = left;
        } else {
            if (parent.right == redBlackTreeNode) {
                parent.right = left;
            } else {
                parent.left = left;
            }
        }

        redBlackTreeNode.parent = left;
        left.right = redBlackTreeNode;
    }

    /**
     * 左旋
     * <p>
     * 右孩子的左孩子作为当前节点的右节点
     * 右孩子成为当前节点的父节点，
     * 当前节点成为右孩子的左节点
     *
     * @param redBlackTreeNode 被旋转的点
     */
    public void leftRotate(RedBlackTreeNode<K, V> redBlackTreeNode) {
        if (redBlackTreeNode == null) {
            System.out.println("旋转节点为null，跳过左旋");
            return;
        }
        RedBlackTreeNode<K, V> right = redBlackTreeNode.right;
        if (right == null) {
            System.out.println("右孩子为null，跳过左旋");
            return;
        }

        RedBlackTreeNode<K, V> rightLeft = right.left;
        redBlackTreeNode.right = rightLeft;
        if (rightLeft != null) {
            rightLeft.parent = redBlackTreeNode;
        }

        RedBlackTreeNode<K, V> parent = redBlackTreeNode.parent;
        right.parent = parent;
        if (parent == null) {
            root = right;
        } else {
            if (parent.right == redBlackTreeNode) {
                parent.right = right;
            } else {
                parent.left = right;
            }
        }

        redBlackTreeNode.parent = right;
        right.left = redBlackTreeNode;
    }

    static class RedBlackTreeNode<K extends Comparable<K>, V> extends TreeNode<K, V> {
        boolean color;

        public RedBlackTreeNode(K key, V value, boolean color) {
            super(key, value);
            this.color = color;
        }
    }
}
