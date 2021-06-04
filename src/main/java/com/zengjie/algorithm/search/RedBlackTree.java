package com.zengjie.algorithm.search;

import lombok.Data;

/**
 * 左右旋时，总是讲被旋转节点作废子节点进行的，
 *
 * @author jie.zeng
 * @version 1.0
 * @since 2021/6/4 22:09
 */
public class RedBlackTree {
    private Node root;

    /**
     * 由于插入黑色肯定会破坏黑色平衡，所以插入时，选择插入红色节点
     *
     * @param value 插入的值
     */
    public void directInsert(int value) {
        Node findNode = root;
        Node parent = null;
        while (findNode != null) {
            if (findNode.value == value) {
                return;
            } else if (findNode.value > value) {
                parent = findNode;
                findNode = findNode.getLeft();
            } else {
                parent = findNode;
                findNode = findNode.getRight();
            }
        }

        if (parent == null) {
            throw new RuntimeException("插入数据失败");
        }
        Node newNode = new Node();
        newNode.setParent(parent);
        newNode.setValue(value);
        newNode.setRed(true);
        if (parent.value > newNode.getValue()) {
            parent.setRight(newNode);
        } else {
            parent.setLeft(newNode);
        }
    }

    private void rightRotation(Node node) {
        Node left, parent, leftRight;
        if (node == null) {
            return;
        }

        if ((left = node.getLeft()) == null) {
            return;
        }

        parent = node.getParent();
        leftRight = left.getRight();
        node.setLeft(leftRight);
        if (leftRight != null) {
            leftRight.setParent(node);
        }

        node.setParent(left);
        left.setRight(node);

        if (parent == null) {
            root = left;
            root.setRed(false);
        } else {
            left.setParent(parent);
            if (parent.getLeft() == node) {
                parent.setLeft(left);
            } else {
                parent.setRight(left);
            }
        }
    }

    private void leftRotation(Node node) {
        //先获取到所有需要的值，再从底部换上来
        Node right, parent, rightLeft;

        if (node == null) {
            return;
        }

        if ((right = node.getRight()) == null) {
            return;
        }

        parent = node.getParent();
        rightLeft = right.getLeft();
        node.setRight(rightLeft);
        if (rightLeft != null) {
            rightLeft.setParent(node);
        }

        right.setLeft(node);
        node.setParent(right);

        if (parent == null) {
            root = right;
            root.setRed(false);
        } else {
            if (parent.getLeft() == node) {
                parent.setLeft(right);
            } else {
                parent.setRight(right);
            }
        }
    }

    @Data
    static class Node {
        private Node parent;
        private Node right;
        private Node left;
        private boolean red;
        private int value;
    }
}
