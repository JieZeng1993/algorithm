package com.zengjie.algorithm.search;

/**
 * @author jie.zeng
 * @version 1.0
 * @since 2021/6/5 8:36
 */
public class RedBlackTree<K extends Comparable<K>, V> {

    public static void main(String[] args) {
        RedBlackTree<Integer, Object> rbt = new RedBlackTree<>();
        int i = 0;
        while (i++ < 8) {
            rbt.insert(i, null);
            show(rbt.root);
        }
    }

    private Node<K, V> root;

    public void insert(K key, V value) {
        //查找到的位置的父节点
        Node<K, V> parent = null;
        Node<K, V> findNode = root;
        while (findNode != null) {
            parent = findNode;
            int compare = key.compareTo(findNode.key);
            if (compare == 0) {
                //相等，替换值
                System.out.printf("根据key【%s】,找到相同的，value由【%s】更改为【%s】\n", key, findNode.value, value);
                findNode.value = value;
                return;
            } else if (compare > 0) {
                //插入的key大于当前节点
                findNode = findNode.right;
            } else {
                findNode = findNode.left;
            }
        }

        if (parent == null) {
            //根节点为黑色
            root = new Node<>(key, value, false);
            System.out.printf("首次插入，根节点key【%s】,value为【%s】\n", key, value);
            return;
        }

        //非根节点插入时，都插入红色，因为插入红色有可能不会破坏黑色平衡
        Node<K, V> node = new Node<>(key, value, true);
        System.out.printf("插入，节点key【%s】,value为【%s】\n", key, value);
        node.parent = parent;
        //判断插在父节点的位置
        System.out.printf("插入的位置的父节点【%s】\n", parent.key);

        int compare = key.compareTo(parent.key);
        if (compare > 0) {
            //插入节点比父节点大，插在父节点的右节点
            parent.right = node;
            System.out.print("插在父节点的右节点\n");
        } else {
            parent.left = node;
            System.out.print("插在父节点的左节点\n");
        }
        balanceInsertion(node);
    }

    /**
     * 情况1：插入节点为根节点，不需要处理，插入时，已经设为黑色
     * 情况2：插入节点已存在，不需要处理
     * 情况3：插入节点的父节点为黑色，不要再次平衡，因为未打破平衡
     * 情况4：插入节点的父节点为红色
     * ----情况4.1：插入节点的叔叔为红色，父叔双红，将父叔变为黑色，将爷爷设置为红色,再以爷爷节点进行下一步处理
     * ----情况4.2: 插入节点的叔叔为不存在或者黑色,父节点为左节点
     * ----------情况4.2.1：插入节点为父节点的左节点(LL)，父节点变黑色，爷爷变为红色，以爷爷节点右旋就行了
     * ----------情况4.2.2：插入节点为父节点的右节点(LR),以父节点左旋转,再平衡父节点
     * ----情况4.3: 插入节点的叔叔为不存在或者黑色,父节点为右节点
     * ----------情况4.3.2：插入节点为父节点的右节点(RR)，父节点变黑色，爷爷变为红色，以爷爷节点左旋就行了
     * ----------情况4.3.1：插入节点为父节点的左节点(RL)，以父节点右旋旋转,再平衡父节点
     *
     * @param node 平衡节点
     */
    public void balanceInsertion(Node<K, V> node) {
        if (node == null) {
            return;
        }
        root.red = false;

        Node<K, V> parent = node.parent;
        if (parent == null) {
            System.out.printf("平衡节点【%s】的父节点为null，跳过平衡", node.key);
            return;
        }

        if (!parent.red) {
            System.out.printf("平衡节点【%s】的父节点为黑色节点，跳过平衡", parent.key);
            return;
        }

        Node<K, V> gparent = parent.parent;
        if (gparent == null) {
            throw new RuntimeException(String.format("红节点肯定不为根节点，所以肯定有爷爷节点，请检查算法正确性，父节点【%s】", parent.key));
        }

        Node<K, V> uncle;

        if (gparent.right == parent) {
            uncle = gparent.left;
            if (uncle == null || !uncle.red) {
                if (parent.right == node) {
                    //RR
                    parent.red = false;
                    gparent.red = true;
                    leftRotate(gparent);
                } else {
                    //RL
                    rightRotate(parent);
                    balanceInsertion(parent);
                }
            } else {
                parent.red = false;
                uncle.red = false;
                gparent.red = true;
                balanceInsertion(gparent);
            }

        } else {
            uncle = gparent.right;
            if (uncle == null || !uncle.red) {
                if (parent.right == node) {
                    //LR
                    leftRotate(parent);
                    balanceInsertion(parent);
                } else {
                    //LL
                    parent.red = false;
                    gparent.red = true;
                    rightRotate(gparent);
                }
            } else {
                parent.red = false;
                uncle.red = false;
                gparent.red = true;
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
     * @param node 被右旋的节点
     */
    public void rightRotate(Node<K, V> node) {
        if (node == null) {
            System.out.println("旋转节点为null，跳过右旋");
            return;
        }
        Node<K, V> left = node.left;

        if (left == null) {
            System.out.println("左孩子为null，跳过右旋");
            return;
        }

        Node<K, V> leftRight = left.right;
        node.left = leftRight;
        if (leftRight != null) {
            leftRight.parent = node;
        }

        Node<K, V> parent = node.parent;
        left.parent = parent;
        if (parent == null) {
            root = left;
        } else {
            if (parent.right == node) {
                parent.right = left;
            } else {
                parent.left = left;
            }
        }

        node.parent = left;
        left.right = node;
    }

    /**
     * 左旋
     * <p>
     * 右孩子的左孩子作为当前节点的右节点
     * 右孩子成为当前节点的父节点，
     * 当前节点成为右孩子的左节点
     *
     * @param node 被旋转的点
     */
    public void leftRotate(Node<K, V> node) {
        if (node == null) {
            System.out.println("旋转节点为null，跳过左旋");
            return;
        }
        Node<K, V> right = node.right;
        if (right == null) {
            System.out.println("右孩子为null，跳过左旋");
            return;
        }

        Node<K, V> rightLeft = right.left;
        node.right = rightLeft;
        if (rightLeft != null) {
            rightLeft.parent = node;
        }

        Node<K, V> parent = node.parent;
        right.parent = parent;
        if (parent == null) {
            root = right;
        } else {
            if (parent.right == node) {
                parent.right = right;
            } else {
                parent.left = right;
            }
        }

        node.parent = right;
        right.left = node;
    }

    static class Node<K, V> {
        Node<K, V> parent;
        Node<K, V> left;
        Node<K, V> right;
        K key;
        V value;
        boolean red;

        public Node(K key, V value, boolean red) {
            this.key = key;
            this.value = value;
            this.red = red;
        }
    }

    //---------------------------------打印相关代码---------------------------------------------------
    public static <K, V> int getTreeDepth(RedBlackTree.Node<K, V> root) {
        return root == null ? 0 : (1 + Math.max(getTreeDepth(root.left), getTreeDepth(root.right)));

    }

    private static <K, V> void writeArray(RedBlackTree.Node<K, V> currNode, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
        // 保证输入的树不为空

        if (currNode == null) return;

        // 先将当前节点保存到二维数组中

        res[rowIndex][columnIndex] = currNode.key + "-" + (currNode.red ? "R" : "B") + "";

        // 计算当前位于树的第几层

        int currLevel = ((rowIndex + 1) / 2);

        // 若到了最后一层，则返回

        if (currLevel == treeDepth) return;

        // 计算当前行到下一行，每个元素之间的间隔(下一行的列索引与当前元素的列索引之间的间隔)

        int gap = treeDepth - currLevel - 1;

        // 对左儿子进行判断，若有左儿子，则记录相应的"/"与左儿子的值

        if (currNode.left != null) {
            res[rowIndex + 1][columnIndex - gap] = "/";

            writeArray(currNode.left, rowIndex + 2, columnIndex - gap * 2, res, treeDepth);

        }

        // 对右儿子进行判断，若有右儿子，则记录相应的"\"与右儿子的值

        if (currNode.right != null) {
            res[rowIndex + 1][columnIndex + gap] = "\\";

            writeArray(currNode.right, rowIndex + 2, columnIndex + gap * 2, res, treeDepth);

        }

    }

    public static <K, V> void show(RedBlackTree.Node<K, V> root) {
        if (root == null) System.out.println("EMPTY!");

        // 得到树的深度
        int treeDepth = getTreeDepth(root);
        // 最后一行的宽度为2的(n - 1)次方乘3，再加1
        // 作为整个二维数组的宽度
        int arrayHeight = treeDepth * 2 - 1;
        int arrayWidth = (2 << (treeDepth - 2)) * 3 + 1;
        // 用一个字符串数组来存储每个位置应显示的元素
        String[][] res = new String[arrayHeight][arrayWidth];
        // 对数组进行初始化，默认为一个空格
        for (int i = 0; i < arrayHeight; i++) {
            for (int j = 0; j < arrayWidth; j++) {
                res[i][j] = " ";
            }
        }

        // 从根节点开始，递归处理整个树
        // res[0][(arrayWidth + 1)/ 2] = (char)(root.val + '0');
        writeArray(root, 0, arrayWidth / 2, res, treeDepth);
        // 此时，已经将所有需要显示的元素储存到了二维数组中，将其拼接并打印即
        for (String[] line : res) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < line.length; i++) {
                sb.append(line[i]);
                if (line[i].length() > 1 && i <= line.length - 1) {
                    i += line[i].length() > 4 ? 2 : line[i].length() - 1;
                }
            }
            System.out.println(sb);
        }
    }
}
