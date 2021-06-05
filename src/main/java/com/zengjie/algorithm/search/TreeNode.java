package com.zengjie.algorithm.search;

/**
 * @author jie.zeng
 * @version 1.0
 * @since 2021/6/5 12:26
 */
public class TreeNode<K extends Comparable<K>, V> {

    public RedBlackTree.RedBlackTreeNode<K, V> parent;
    public RedBlackTree.RedBlackTreeNode<K, V> left;
    public RedBlackTree.RedBlackTreeNode<K, V> right;
    public K key;
    public V value;

    public TreeNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

}
