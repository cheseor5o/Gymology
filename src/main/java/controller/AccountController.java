package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;
import util.*;

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
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    public TextField phone;
    @FXML
    public ComboBox<String> gender;
    public Button save;
    public Label prompt;
    @FXML
    private Button information;

    @Override
    public void scene() {
        LoginController loginController = Controllers.get(LoginController.class);
        if (loginController.hasLogging()) {
            Instance instance = loginController.getUser();
            scene(instance);
        } else {
            Controllers.setCenter(loginController.getScene(), false);
        }
    }

    public void scene(Instance instance) {
        switch (instance.getIdentity()) {
            case Customer:
                Controllers.replaceCenter(this.getClass());
                fill(instance);
                break;
            case Manager://进入manager界面
                break;
            case Coach://进入coach界面
        }
    }

    private void fill(Instance instance) {
        password.setText(instance.getPassword());
        User user;
        UserDatabase database = null;
        switch (instance.getIdentity()) {
            case Customer:
                database = Databases.getDatabase(Customer.class);
                break;
            case Coach:
                database = Databases.getDatabase(Coach.class);
                break;
            case Manager:
                database = Databases.getDatabase(Manager.class);
                break;
        }
        user = database.get(instance.getEmail());
        username.setText(user.getName());
        password.setText(instance.getPassword());
        phone.setText(user.getPhone());
        gender.getSelectionModel().select(user.getGender());
    }


    @FXML
    public void save() {
        String username = this.username.getText();
        String password = this.password.getText();
        String gender = this.gender.getValue();
        String phone = this.phone.getText();
        Instance instance = Controllers.get(LoginController.class).getUser();
        if (Controllers.get(RegisterController.class).validateBasic(username, password, password, gender, phone, prompt)) {
            User user = null;
            UserDatabase database = null;
            switch (instance.getIdentity()) {
                case Customer:
                    instance.setIdentity(Instance.Identity.Customer);
                    database = Databases.getDatabase(Customer.class);
                    break;
                case Coach:
                    instance.setIdentity(Instance.Identity.Coach);
                    database = Databases.getDatabase(Coach.class);
                    break;
                case Manager:
                    instance.setIdentity(Instance.Identity.Manager);
                    database = Databases.getDatabase(Manager.class);
                    break;
            }
            user = database.get(instance.getEmail());
            user.setName(username);
            user.setPhone(phone);
            user.setGender(gender);
            instance.setPassword(password);
            prompt.setText("Update successfully!");
        }
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                Platform.runLater(() -> prompt.setText(""));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gender.getItems().addAll("Male", "Female", "Prefer not to say");
    }
}
