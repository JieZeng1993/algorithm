package com.zengjie.algorithm.search;

/**
 * @author jie.zeng
 * @version 1.0
 * @since 2021/6/4 21:13
 */
public class SearchMain {
    public static void main(String[] args) {
        ISearch arraySearch = new BinarySearch();

        int index = arraySearch.search(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 3);
    }
}
