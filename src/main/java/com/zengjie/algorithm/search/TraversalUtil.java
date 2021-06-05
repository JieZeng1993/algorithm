package com.zengjie.algorithm.search;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 此外还有 层序遍历：每一层从左到右访问每一个节点。 未实现
 *
 * @author jie.zeng
 * @version 1.0
 * @since 2021/6/5 12:25
 */
public class TraversalUtil {

    public static <K extends Comparable<K>, V> void preorderTraversal(TreeNode<K, V> root) {
        List<K> keyList = new ArrayList<>();
        preorderTraversal(root, keyList);
        System.out.println("preorderTraversal: " + keyList.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    private static <K extends Comparable<K>, V> void preorderTraversal(TreeNode<K, V> treeNode, List<K> keyList) {
        if (treeNode == null) {
            return;
        }
        keyList.add(treeNode.key);
        preorderTraversal(treeNode.left, keyList);
        preorderTraversal(treeNode.right, keyList);
    }


    public static <K extends Comparable<K>, V> void middleTraversal(TreeNode<K, V> root) {
        List<K> keyList = new ArrayList<>();
        middleTraversal(root, keyList);
        System.out.println("middleTraversal: " + keyList.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    private static <K extends Comparable<K>, V> void middleTraversal(TreeNode<K, V> treeNode, List<K> keyList) {
        if (treeNode == null) {
            return;
        }
        middleTraversal(treeNode.left, keyList);
        keyList.add(treeNode.key);
        middleTraversal(treeNode.right, keyList);
    }

    public static <K extends Comparable<K>, V> void postorderTraversal(TreeNode<K, V> root) {
        List<K> keyList = new ArrayList<>();
        postorderTraversal(root, keyList);
        System.out.println("postorderTraversal: " + keyList.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    private static <K extends Comparable<K>, V> void postorderTraversal(TreeNode<K, V> treeNode, List<K> keyList) {
        if (treeNode == null) {
            return;
        }
        postorderTraversal(treeNode.left, keyList);
        postorderTraversal(treeNode.right, keyList);
        keyList.add(treeNode.key);
    }
}
