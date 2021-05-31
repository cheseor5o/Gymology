package util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

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

    public static Alert openMessage(Alert.AlertType alertType, String title, String headerText, String context){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(context);
        return alert;
    }
    
    public static TextInputDialog openDialog(String title, String headerText, String context){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(context);
        return dialog;
    }
}
