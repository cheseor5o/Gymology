package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import model.User;
import util.Controllers;
import util.UserDataBase;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Dong
 */
public class LoginController extends AbstractController {
    
    /**
     * 全局user
     */
    private User user;
    @FXML
    private TextField email;

    @FXML
    private Label prompt;

    @FXML
    private PasswordField password;
    

    @FXML
    private void login() {
        User user = new User();
        user.setEmail(email.getText());
        user.setPassword(password.getText());
        int index = UserDataBase.contains(user);
        if (UserDataBase.exists(index, user)) {
            prompt.setText("");
            this.user = UserDataBase.get(index);
            try {
                Controllers.reload(CoachController.class);
                Controllers.reload(CourseController.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Controllers.get(AccountController.class).scene(this.user);
        } else {
            password.setText("");
            prompt.setText("Login failed!");
        }
    }
    
    
    public boolean hasLogging(){
        return user != null;
    }


    @Override
    public void scene() {
        Controllers.setCenter(user == null ? LoginController.class : AccountController.class);
    }

    public void loginScene(String prompt)  {
        try {
            Controllers.reload(RegisterController.class);
            this.prompt.setText(prompt);
            Controllers.replaceCenter(LoginController.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @FXML
    public void register() {
        Controllers.setCenter(RegisterController.class);
    }
    
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        password.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)){
                login();
            }
        });
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
