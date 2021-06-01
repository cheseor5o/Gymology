package controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.*;
import util.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Account controller controllers the scene of different user account
 * @author Dong
 */
public class AccountController extends AbstractController {

    /**
     * Log out button
     */
    @FXML
    public Button logoutBtn;
    /**
     * Username input field
     */
    @FXML
    public TextField username;
    /**
     * Password input field
     */
    @FXML
    public PasswordField password;
    /**
     * Phone input field
     */
    @FXML
    public TextField phone;
    /**
     * Select field
     */
    @FXML
    public ComboBox<String> gender;
    /**
     * Save button
     */
    @FXML
    public Button save;
    /**
     * Prompt field
     */
    @FXML
    public Label prompt;
    /**
     * balance field
     */
    @FXML
    public TextField balance;
    /**
     * Top up button
     */
    @FXML
    public Button topUp;
    /**
     * Purchase button
     */
    @FXML
    public Button purchase;
    @FXML
    public Label membershipStatus;
    @FXML
    public Label overdue;
    
    private static final double vipPrice = 500;

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

    /**
     * Choose a scene according the user type
     * @param instance the login instance
     */
    public void scene(Instance instance) {
        Controllers.setCenter(this.getClass(),false);
        switch (instance.getIdentity()) {
            case Customer:
                checkVipService(instance);
                fill(instance);
                break;
            case Manager://进入manager界面
                break;
            case Coach://进入coach界面
        }
    }

    /**
     * Check if the user is vip
     * @param instance the login instance
     */
    private void checkVipService(Instance instance){
        Customer customer = Databases.getDatabase(Customer.class).get(instance.getEmail());
        if (customer.isVip()){
            if (Tools.now().compareTo(customer.getVipExpired())>0){
                customer.setVip(false);
            }
        }
    }

    /**
     * Fill the information into the account information scene
     * @param instance the login instance
     */
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
        if (Customer.isCustomer(instance)){
            Customer customer = (Customer) user;
            membershipStatus.setText(customer.isVip() ? "Vip. "+customer.getVipRank() : "Not Vip");
            overdue.setText(customer.isVip() ? customer.getVipExpired() : "Not Vip");
            balance.setText(String.valueOf(customer.getBalance()));
        }else {
            balance.setText("Unavailable");
        }
        username.setText(user.getName());
        password.setText(instance.getPassword());
        phone.setText(user.getPhone());
        gender.getSelectionModel().select(user.getGender());
        purchase.setOnMouseClicked(mouseEvent -> {
            if (Customer.isCustomer(instance)) {
                Customer customer = (Customer) user;
                if (customer.getBalance() < vipPrice){
                    Tools.openMessage(Alert.AlertType.WARNING,"Vip purchase Center","Failed!","Reason: Balance is not enough!").showAndWait();
                }else {
                    Alert alert = Tools.openMessage(Alert.AlertType.CONFIRMATION, "Vip purchase Center", "Are you sure?", "Purchase vip for a month");
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK){
                        customer.setBalance(customer.getBalance() - vipPrice);
                        String vipExpired = customer.getVipExpired();
                        String now = Tools.now();
                        if (vipExpired == null || now.compareTo(vipExpired) > 0){
                            vipExpired = Tools.dateString(Tools.parse(now).plusMonths(1));
                            customer.setVipExpired(vipExpired);
                        }else {
                            LocalDateTime time = Tools.parse(vipExpired);
                            vipExpired = Tools.dateString(time.plusMonths(1));
                        }
                        customer.setVip(true);
                        customer.setVipExpired(vipExpired);
                        customer.setVipRank(customer.getVipRank() + 1);
                        membershipStatus.setText("Vip. "+customer.getVipRank());
                        overdue.setText(vipExpired);
                        balance.setText(String.valueOf(customer.getBalance()));
                        Tools.openMessage(Alert.AlertType.WARNING,"Vip purchase Center","Success!","Successfully purchase vip! Thanks you!").showAndWait();
                    }
                }
            }
        });
        
        
    }

    /**
     * Set up if the buttons
     * @param status
     */
    private void setTopUp(boolean status){
        topUp.setDisable(status);
        topUp.setVisible(status);
    }

    /**
     * Save the information
     */
    @FXML
    public void save() {
        String username = this.username.getText();
        String password = this.password.getText();
        String gender = this.gender.getValue();
        String phone = this.phone.getText();
        Instance instance = Controllers.get(LoginController.class).getInstance();
        if (RegisterController.validateBasic(username, password, password, gender, phone, prompt)) {
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

    /**
     * Top up the balance of the user
     */
    @FXML
    public void topUp(){
        Instance instance = Controllers.get(LoginController.class).getInstance();
        if (instance.getIdentity().equals(Instance.Identity.Customer)){
            TextInputDialog dialog = Tools.openDialog("Balance Recharge Center","Here to top up your account","Enter the amount:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(s -> {
                try {
                    double amount = Double.parseDouble(s);
                    if (amount<=0){
                        Tools.openMessage(Alert.AlertType.ERROR, "Balance Recharge Center", "Balance <= 0", "Please enter a positive amount!").showAndWait();
                    }else {
                        Customer customer = Databases.getDatabase(Customer.class).get(instance.getEmail());
                        customer.setBalance(customer.getBalance()+amount);
                        Tools.openMessage(Alert.AlertType.INFORMATION, "Balance Recharge Center", "Success", "Top up successfully!").showAndWait();
                        balance.setText(String.valueOf(customer.getBalance()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Tools.openMessage(Alert.AlertType.ERROR, "Balance Recharge Center", "Invalid input", "Please enter a valid amount!").showAndWait();
                }
            });
        }
    }

    /**
     * Instance log out
     */
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
