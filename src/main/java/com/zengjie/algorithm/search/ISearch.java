package com.zengjie.algorithm.search;

/**
 * 查询接口
 *
 * @author jie.zeng
 * @version 1.0
 * @since 2021/6/4 21:13
 */
public interface ISearch {
    int NOT_FOUND_INDEX = -1;

    /**
     * @param array 查询所在数组
     * @param value 查找值
     * @return 返回查找到的位置
     * @author zengjie
     * @since 2021/6/4 21:18
     */
    int search(int[] array, int value);
}
