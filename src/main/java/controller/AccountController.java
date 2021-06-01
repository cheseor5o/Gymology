package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;
import util.Controllers;
import util.Databases;
import util.Tools;
import util.UserDatabase;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
    @FXML
    public Button save;
    @FXML
    public Label prompt;
    @FXML
    public TextField balance;
    @FXML
    public Button topUp;
    @FXML
    private Button information;

    @Override
    public void scene() {
        LoginController loginController = Controllers.get(LoginController.class);
        if (loginController.hasLogging()) {
            Instance instance = loginController.getInstance();
            scene(instance);
        } else {
            Controllers.setCenter(loginController.getScene(),false);
        }
    }

    public void scene(Instance instance) {
        switch (instance.getIdentity()) {
            case Customer:
                Controllers.setCenter(this.getClass(),false);
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
                setTopUp(false);
                break;
            case Manager:
                database = Databases.getDatabase(Manager.class);
                setTopUp(false);
                break;
        }
        user = database.get(instance.getEmail());
        if (instance.getIdentity().equals(Instance.Identity.Customer)){
            balance.setText(String.valueOf(((Customer) user).getBalance()));
        }else {
            balance.setText("Unavailable");
        }
        username.setText(user.getName());
        password.setText(instance.getPassword());
        phone.setText(user.getPhone());
        gender.getSelectionModel().select(user.getGender());
    }

    private void setTopUp(boolean status){
        topUp.setDisable(status);
        topUp.setVisible(status);
    }
    
    @FXML
    public void save() {
        String username = this.username.getText();
        String password = this.password.getText();
        String gender = this.gender.getValue();
        String phone = this.phone.getText();
        Instance instance = Controllers.get(LoginController.class).getInstance();
        if (Controllers.get(RegisterController.class).validateBasic(username, password, password, gender, phone, prompt)) {
            User user;
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
    public void topUp(){
        TextInputDialog dialog = Tools.openDialog("Balance Recharge Center","Here to top up your account","Enter the amount:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> {
            try {
                double amount = Double.parseDouble(s);
                if (amount<=0){
                    Tools.openMessage(Alert.AlertType.ERROR, "Balance Recharge Center", "Balance <= 0", "Please enter a positive amount!").showAndWait();
                }else {
                    Instance instance = Controllers.get(LoginController.class).getInstance();
                    Customer customer = Databases.getDatabase(Customer.class).get(instance.getEmail());
                    customer.setBalance(customer.getBalance()+amount);
                    Tools.openMessage(Alert.AlertType.INFORMATION, "Balance Recharge Center", "Success", "Top up successfully!").showAndWait();
                    Controllers.reload(this.getClass());
                    Controllers.get(this.getClass()).scene();
                }
            }catch (Exception e){
                e.printStackTrace();
                Tools.openMessage(Alert.AlertType.ERROR, "Balance Recharge Center", "Invalid input", "Please enter a valid amount!").showAndWait();
            }
        });
    }


    @FXML
    public void logout() {
        LoginController loginController = Controllers.get(LoginController.class);
        loginController.setInstance(null);
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
        balance.setDisable(true);
    }
}
