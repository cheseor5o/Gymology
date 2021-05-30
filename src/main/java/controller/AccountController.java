package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import model.User;
import util.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Dong
 */
public class AccountController extends AbstractController {

    @FXML
    public Button logoutBtn;
    @FXML
    private Label label;

    @Override
    public void scene() {
        LoginController loginController = Controllers.get(LoginController.class);
        if (loginController.hasLogging()){
            User user = loginController.getUser();
            scene(user);
        }else {
            Controllers.setCenter(loginController.getScene(),false);
        }
    }

    public void scene(User user){
        switch (user.getIdentity()){
            case Customer: Controllers.replaceCenter(this.getClass());
                break;
            case Manager://进入manager界面
                break;
            case Coach://进入coach界面
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    public void logout() {
        LoginController loginController = Controllers.get(LoginController.class);
        loginController.setUser(null);
        try {
            Controllers.reload(LoginController.class);
            Controllers.reload(CoachController.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Controllers.clearHistory();
        Controllers.setCenter(HomeController.class);
    }
}
