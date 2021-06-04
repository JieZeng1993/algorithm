package com.zengjie.algorithm.search;


/**
 * 二分查找法
 *
 * @author jie.zeng
 * @version 1.0
 * @since 2021/6/4 20:50
 */
public class BinarySearch implements ISearch {

    @Override
    public int search(int[] array, int value) {
        if (array == null || array.length == 0) {
            return ISearch.NOT_FOUND_INDEX;
        }
        int start = 0, end = array.length - 1;

        int mid;

        while (end >= start) {
            mid = (end + start) / 2;
            if (array[mid] == value) {
                return mid;
            }
            if (array[mid] > value) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }


        return ISearch.NOT_FOUND_INDEX;
    }
}
