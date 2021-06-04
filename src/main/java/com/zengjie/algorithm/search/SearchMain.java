package com.zengjie.algorithm.search;

import java.util.Scanner;

/**
 * @author jie.zeng
 * @version 1.0
 * @since 2021/6/4 21:13
 */
public class SearchMain {
    public static void main(String[] args) {
        ISearch arraySearch = new BinarySearch();

        int[] array = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            System.out.println(arraySearch.search(array, scanner.nextInt()));
        }
    }
}
