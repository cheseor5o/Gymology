package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import model.Instance;
import util.Controllers;
import util.InstanceDataBase;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Handling login service
 * @author Dong
 */
public class LoginController extends AbstractController {
    
    /**
     * 全局user
     */
    private Instance instance;
    @FXML
    private TextField email;

    @FXML
    private Label prompt;

    @FXML
    private PasswordField password;
    

    @FXML
    private void login() {
        Instance instance = new Instance();
        instance.setEmail(email.getText());
        instance.setPassword(password.getText());
        int index = InstanceDataBase.contains(instance);
        if (InstanceDataBase.login(index, instance)) {
            prompt.setText("");
            this.instance = InstanceDataBase.get(index);
            try {
                Controllers.reload(CoachController.class);
                Controllers.reload(CourseController.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Controllers.get(AccountController.class).scene(this.instance);
        } else {
            password.setText("");
            prompt.setText("Login failed!");
        }
    }
    
    
    public boolean hasLogging(){
        return instance != null;
    }


    @Override
    public void scene() {
        Controllers.setCenter(instance == null ? LoginController.class : AccountController.class);
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

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }
}
