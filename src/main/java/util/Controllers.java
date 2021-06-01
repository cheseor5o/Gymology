package util;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;


import java.io.IOException;
import java.util.*;

/**
 * The Controllers class is literally a box of controller
 * It keeps every controller that controls scene that essential to the system
 * A class is pass into this box, and represents only an object that it keeps
 * @author Dong
 * @since April 30, 2021
 */
public final class Controllers {
    /**
     * The controller box
     */
    private static final HashMap<Class<? extends AbstractController>,Object> CONTROLLER_BOX =new HashMap<>();
    /**
     * The pane that represent the main scene
     */
    private static BorderPane mainPane;
    /**
     * A scene list for controlling the browse history
     */
    private static final SceneList<Parent> sceneList = new SceneList<>();
    
    public static BorderPane getMainPane() {
        return mainPane;
    }

    public static void setMainPane(BorderPane mainPane) {
        Controllers.mainPane = mainPane;
    }


    /**
     * If the actual scene is not the current scene, which is standardized in sceneList
     * @see SceneList
     * then change actual scene to that current scene
     * If the actual scene is the current scene, then check if there is a next scene
     * Switch it to next scene if it has a next scene
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
     * If the actual scene is not the current scene, which is standardized in sceneList
     * @see SceneList
     * then change actual scene to that current scene
     * If the actual scene is the current scene, then check if there is a previous scene
     * Switch it to previous scene if it has a previous scene
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
     * Switch between different scenes while selectively saving access records
     * In general, when a mandatory jump occurs, 
     * such as a jump to the login screen because you are not logged in while viewing an order.
     * The login screen should not be saved, otherwise if there are multiple login records, 
     * the other login screens will remain in the SceneList after login
     * @param node scene
     * @param track whether to save records
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
     * The setCenter method, which takes no parameters, saves the access record by default
     * @param clazz clazz
     */
    public static void setCenter(Class<? extends AbstractController> clazz){
        setCenter(clazz,true);
    }

    /**
     * Obtain the scene by class, then setCenter
     * @param clazz clazz
     * @param track whether to save records
     * @see #setCenter(Parent, boolean) 
     */
    public static void setCenter(Class<? extends AbstractController> clazz, boolean track){
        setCenter(get(clazz).getScene(),track);
    }

    /**
     * Putting a controller into the CONTROLLER_BOX
     * @param controller controller that has to extends {@link AbstractController}
     * @param <T> Generic form of controller
     */
    public static <T extends AbstractController> void put(T controller){
        CONTROLLER_BOX.put(controller.getClass(),Objects.requireNonNull(controller));
    }

    /**
     * 
     * @param clazz A clazz of a controller
     * @param <T> Generic form of controller
     * @return A controller, which is an object of the class that clazz represent
     */
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
