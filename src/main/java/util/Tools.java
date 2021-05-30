package util;

import java.util.List;

public class Tools {
    /**
     * 二分查找
     * @param list list
     * @param content 查找的内容
     * @return 该content在list的坐标。若无，返回应该被插入的位置
     */
    public static int findIndex(List<String> list, String content){
        int left = 0, right = list.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String midContent = list.get(mid);
            if (content.compareTo(midContent) > 0) {
                left = mid + 1;
            } else if (content.compareTo(midContent) < 0) {
                right = mid - 1;
            } else {//找到了
                return mid;
            }
        }
        return left;
    }
}
