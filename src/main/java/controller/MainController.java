package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import util.Controllers;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Cheese
 * @since 2021/4/26 23:11
 */
public class MainController extends AbstractController {
    
    @FXML
    private BorderPane mainPane;

    public BorderPane getMainPane() {
        return mainPane;
    }

    @FXML
    public void homeScene() {
        Controllers.get(HomeController.class).scene();
    }

    @FXML
    private void courseScene() {
        Controllers.get(CourseController.class).scene();
    }

    @FXML
    private void accountScene() {
        Controllers.get(AccountController.class).scene();
    }

    @FXML
    private void coachScene() {
        Controllers.get(CoachController.class).scene();
    }

    @FXML
    private void orderScene() {
        Controllers.get(OrderController.class).scene();
    }
    
    @FXML
    public void backward(){
        Controllers.backward();
    }

    @FXML
    public void forward(){
        Controllers.forward();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader accountLoader = new FXMLLoader(AccountController.class.getResource("/fxml/account.fxml"));
            FXMLLoader homeLoader = new FXMLLoader(HomeController.class.getResource("/fxml/home.fxml"));
            FXMLLoader loginLoader = new FXMLLoader(LoginController.class.getResource("/fxml/login.fxml"));
            FXMLLoader registerLoader = new FXMLLoader(RegisterController.class.getResource("/fxml/register.fxml"));
            FXMLLoader courseLoader = new FXMLLoader(RegisterController.class.getResource("/fxml/course.fxml"));
            FXMLLoader courseItemLoader = new FXMLLoader(RegisterController.class.getResource("/fxml/courseItem.fxml"));
            FXMLLoader coursePlayerLoader = new FXMLLoader(RegisterController.class.getResource("/fxml/coursePlayer.fxml"));
            FXMLLoader courseEditLoader = new FXMLLoader(RegisterController.class.getResource("/fxml/courseEdit.fxml"));
            FXMLLoader coachLoader = new FXMLLoader(RegisterController.class.getResource("/fxml/coach.fxml"));
            FXMLLoader coachItemLoader = new FXMLLoader(RegisterController.class.getResource("/fxml/coachItem.fxml"));
            FXMLLoader coachInfoLoader = new FXMLLoader(RegisterController.class.getResource("/fxml/coachInfo.fxml"));
            FXMLLoader orderLoader = new FXMLLoader(OrderController.class.getResource("/fxml/order.fxml"));


            Parent load;

            load = accountLoader.load();
            AccountController accountController = accountLoader.getController();
            accountController.setScene(load);
            Controllers.put(accountController);

            load = homeLoader.load();
            HomeController homeController = homeLoader.getController();
            homeController.setScene(load);
            Controllers.put(homeController);

            load = loginLoader.load();
            LoginController loginController = loginLoader.getController();
            loginController.setScene(load);
            Controllers.put(loginController);

            load = registerLoader.load();
            RegisterController registerController = registerLoader.getController();
            registerController.setScene(load);
            Controllers.put(registerController);

            load = courseLoader.load();
            CourseController courseController = courseLoader.getController();
            courseController.setScene(load);
            Controllers.put(courseController);

            load = courseItemLoader.load();
            CourseItemController courseItemController = courseItemLoader.getController();
            courseItemController.setScene(load);
            Controllers.put(courseItemController);

            load = coursePlayerLoader.load();
            CoursePlayerController coursePlayerController = coursePlayerLoader.getController();
            coursePlayerController.setScene(load);
            Controllers.put(coursePlayerController);

            load = courseEditLoader.load();
            CourseEditController courseEditController = courseEditLoader.getController();
            courseEditController.setScene(load);
            Controllers.put(courseEditController);

            load = coachLoader.load();
            CoachController coachController = coachLoader.getController();
            coachController.setScene(load);
            Controllers.put(coachController);

            load = coachItemLoader.load();
            CoachItemController coachItemController = coachItemLoader.getController();
            coachItemController.setScene(load);
            Controllers.put(coachItemController);

            load = coachInfoLoader.load();
            CoachInfoController coachInfoController = coachInfoLoader.getController();
            coachInfoController.setScene(load);
            Controllers.put(coachInfoController);

            load = orderLoader.load();
            OrderController orderController = orderLoader.getController();
            orderController.setScene(load);
            Controllers.put(orderController);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
