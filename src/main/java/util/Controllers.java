package util;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import model.Instance;
import model.User;


import java.io.IOException;
import java.util.*;

/**
 * @author Dong
 */
public final class Controllers {
    private static final HashMap<Class<? extends AbstractController>,Object> CONTROLLER_BOX =new HashMap<>();
    private static BorderPane mainPane;
    private static final SceneList<Parent> sceneList = new SceneList<>();
    private static int current;

    public static BorderPane getMainPane() {
        return mainPane;
    }

    public static void setMainPane(BorderPane mainPane) {
        Controllers.mainPane = mainPane;
    }


    /**
     * 前往下一页
     */
    public static void forward(){
        Parent currentScene = sceneList.currentScene();
        if (!mainPane.getCenter().equals(currentScene)) {
            mainPane.setCenter(currentScene);
        }else {
            Parent parent = sceneList.nextScene();
            if (parent != null){
                mainPane.setCenter(parent);
            }
        }
    }
    /**
     * 前往上一页
     */
    public static void backward(){
        Parent currentScene = sceneList.currentScene();
        if (!mainPane.getCenter().equals(currentScene)) {
            mainPane.setCenter(currentScene);
        }else {
            Parent parent = sceneList.preScene();
            if (parent != null){
                mainPane.setCenter(parent);
            }
        }
    }
    
    /**
     * 不带参数的setCenter方法，默认保存访问记录
     */
    public static void setCenter(Parent node){
        setCenter(node,true);
    }

    /**
     * 切换不同的页面，同时有选择性地保存访问记录
     * 一般来说，当发生强制性的跳转时，比如因为查看订单时未登录而跳转至了登录界面。
     * 该登录界面不应该被保存，否则若存在多个登录界面记录，登录后其它的登录界面依然还在sceneList中
     * @param node 页面
     * @param track 是否保存记录
     */
    public static void setCenter(Parent node, boolean track){
        if (track){
            if (sceneList.isEmpty() || !node.equals(sceneList.currentScene())){
                sceneList.add(node);
            }
        }
        mainPane.setCenter(node);
//        System.out.println(sceneList);
    }

    /**
     * 不带参数的setCenter方法，默认保存访问记录
     * @param clazz clazz
     */
    public static void setCenter(Class<? extends AbstractController> clazz){
        setCenter(clazz,true);
    }

    /**
     * 同理
     * @param clazz clazz
     * @param track 是否保存记录
     * @see #setCenter(Parent, boolean) 
     */
    public static void setCenter(Class<? extends AbstractController> clazz, boolean track){
        setCenter(get(clazz).getScene(),track);
    }

    public static <T extends AbstractController> void put(T controller){
        CONTROLLER_BOX.put(controller.getClass(),Objects.requireNonNull(controller));
    }

    public static <T extends AbstractController> T get(Class<T> clazz){
        T controller =(T)CONTROLLER_BOX.get(clazz);
        return Objects.requireNonNull(controller);
    }

   

    public static <T extends AbstractController> void reload(Class<T> clazz) throws IOException {
        FXMLLoader loader;
        StringBuilder builder = new StringBuilder(clazz.getSimpleName());
        int index = builder.indexOf("Controller");
        builder.delete(index,builder.length());
        builder.setCharAt(0, Character.toLowerCase(builder.charAt(0)));
        builder.insert(0,"/fxml/");
        builder.append(".fxml");
        loader = new FXMLLoader(clazz.getResource(builder.toString()));
        Parent parent = loader.load();
        T controller = loader.getController();
        controller.setScene(parent);
        put(controller);
    }

    /**
     * 用下一个scene替换当前的scene
     * 例如：用户注册成功，跳转登录，不能再返回注册界面
     * 用户支付成功，不能再返回支付页面
     */
    public static void replaceCenter(Parent node){
        sceneList.replace(node);
        mainPane.setCenter(sceneList.currentScene());
    }
    
    public static void replaceCenter(Class<? extends AbstractController> clazz){
        replaceCenter(get(clazz).getScene());
    }

    /**
     * 清除浏览记录
     */
    public static void clearHistory() {
        sceneList.clear();
    }
}
