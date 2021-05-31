package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;
import util.*;

import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dong
 */
public class RegisterController extends AbstractController {
    private static final String COACH_AUTHORIZATION_CODE = "2020";
    private static final String MANAGER_AUTHORIZATION_CODE = "2021";
    public ComboBox<Instance.Identity> type;
    //
    @FXML
    private TextField email;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password1;
    @FXML
    private PasswordField password2;
    @FXML
    private ComboBox<String> gender;
    @FXML
    private TextField phone;
    @FXML
    private TextField verifyCode;
    @FXML
    private Button send;

    private String checkCode;
    @FXML
    private Label prompt;

    @FXML
    private void register() {
        String emailText = email.getText();
        if (MailUtil.isFormatValid(emailText)) {
            String usernameText = username.getText();
            String genderValue = gender.getValue();
            String phoneText = phone.getText();
            if (validateBasic(usernameText,password1.getText(),password2.getText(),genderValue,phoneText,prompt)){
                if (verifyCode.getText().equals(checkCode)) {
                    Instance instance = new Instance();
                    instance.setEmail(emailText);
                    instance.setIdentity(type.getValue());
                    if (!InstanceDataBase.exists(InstanceDataBase.contains(instance), instance)) {
                        instance.setPassword(password1.getText());
                        User user = null;
                        UserDatabase database = null;
                        switch (instance.getIdentity()) {
                            case Customer:
                                instance.setIdentity(Instance.Identity.Customer);
                                database = Databases.getDatabase(Customer.class);
                                user = new Customer();
                                break;
                            case Coach:
                                instance.setIdentity(Instance.Identity.Coach);
                                database = Databases.getDatabase(Coach.class);
                                user = new Coach();
                                break;
                            case Manager:
                                instance.setIdentity(Instance.Identity.Manager);
                                database = Databases.getDatabase(Manager.class);
                                user = new Manager();
                                break;
                            default:Tools.openMessage(Alert.AlertType.ERROR,"Error Dialog","ERROR OCCUR!","Unknown Error!").showAndWait();
                        }
                        setUp(user,instance.getEmail(),usernameText,phoneText,genderValue);
                        database.add(user);
                        InstanceDataBase.add(instance);
                        prompt.setText("");
                        Controllers.get(LoginController.class).loginScene("Register Successfully!");
                    } else {
                        prompt.setText("This email is already been used!");
                    }
                } else {
                    prompt.setText("Incorrect check code!");
                }
            }
            
        } else {
            email.setText("");
            prompt.setText("Email invalid!");
        }
    }

    public void login(){
        LoginController loginController = Controllers.get(LoginController.class);
        Controllers.setCenter(loginController.getScene(), false);
    }
    
    private void setUp(User user, String email, String name, String phone, String gender){
        user.setId(email);
        user.setName(name);
        user.setPhone(phone);
        user.setGender(gender);
    }

    @FXML
    public void sendCode() {
        send.setDisable(true);
        send.setText("Sent");
        String character = "Aa0BbCc1DdEe2FfGg3HhIi4JjKk5LlMmNnOoPpQqRrSs6TtUu7VvWw8XxYy9Zz";
        StringBuilder checkCode = new StringBuilder();
        Random random = new Random();
        String email = this.email.getText();
        String username = this.username.getText();
        for (int i = 1; i <= 8; i++) {
            char c = character.charAt(random.nextInt(character.length()));
            checkCode.append(c);
        }
        this.checkCode = checkCode.toString();
        System.out.println(checkCode);
        String text = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Hi, " + username +
                "! Welcome to Gymology, we offer you the best courses and instructions you have never experience before!" +
                " Your registered check code is: <a>" + this.checkCode +
                "</a>! Please finish your sign up process ASAP!<br><hr/>" +
                "Do not reply this email!<br>---------------------------------------Your dear dongdong";


        FutureTask<Boolean> trySend = sendTest(email);
        sendAndCountdown(trySend, send, prompt, text);
    }


    private FutureTask<Boolean> sendTest(String email) {
        FutureTask<Boolean> sending = new FutureTask<>(() -> MailUtil.isValid(email, "jootmir.org"));
        new Thread(sending).start();
        return sending;
    }

    private void sendAndCountdown(FutureTask<Boolean> trySend, Button send, Label prompt, String text) {
        new Thread(() -> {
            while (!trySend.isDone()) {
                Thread.yield();
            }
            try {
                if (trySend.get()) {
                    new Thread(() -> MailUtil.sendEmail(email.getText(), "Gymology", username.getText(), "Gymology Register Confirmation", text)).start();
                    int seconds = 45;
                    while (seconds != 0) {
                        int finalSeconds = seconds;
                        Platform.runLater(() -> send.setText(String.valueOf(finalSeconds)));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        --seconds;
                    }
                } else {
                    Platform.runLater(() -> prompt.setText("Non-exist email!"));
                }
                Platform.runLater(() -> {
                    send.setText("Send");
                    send.setDisable(false);
                });
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gender.getItems().addAll("Male", "Female", "Prefer not to say");
        type.getItems().addAll(Instance.Identity.Customer, Instance.Identity.Coach, Instance.Identity.Manager);
        type.getSelectionModel().select(0);
        type.getSelectionModel().selectedItemProperty().addListener((observableValue, oldOne, newOne) -> {
            if (!newOne.equals(Instance.Identity.Customer)){
                TextInputDialog dialog = Tools.openDialog("Identity verification","We need your authorization code","Enter authorization code:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> {
                    switch (newOne){
                        case Coach:
                            if (!COACH_AUTHORIZATION_CODE.equals(name)){
                                Tools.openMessage(Alert.AlertType.ERROR,"Error Dialog","ERROR OCCUR!","Incorrect authorization code!").showAndWait();
                                type.getSelectionModel().select(oldOne);
                            }
                            break;
                        case Manager:
                            if (!MANAGER_AUTHORIZATION_CODE.equals(name)) {
                                Tools.openMessage(Alert.AlertType.ERROR,"Error Dialog","ERROR OCCUR!","Incorrect authorization code!").showAndWait();
                                type.getSelectionModel().select(oldOne);
                            }
                            break;
                        default:
                            System.out.println("ERROR");
                    }
                });
                if (!result.isPresent()){
                    type.getSelectionModel().select(Instance.Identity.Customer);
                }
                
            }
        });
    }

    
    
    
    public boolean validateBasic(String username, String password1,String password2, String gender, String phone, Label prompt){
        if (!validField(username)) {
            prompt.setText("Invalid Username!");
            return false;
        }
        if (!validPassword(password1, password2)) {
            prompt.setText("Invalid Password! (Must > 6)");
            return false;
        } 
        if (gender != null && !validField(gender)) {
            prompt.setText("Select gender plz!");
            return false;
        } 
        if (!validPhone(phone)) {
            prompt.setText("Invalid phone number!");
            return false;
        }
        return true;
    }

    public static boolean validField(String text) {
        return !"".equals(text.trim());
    }

    public static boolean validPassword(String p1, String p2) {
        if (p1.length() < 6) {
            return false;
        }
        String check = "^[A-Za-z0-9]+$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(p1);
        return matcher.matches() && p1.equals(p2);
    }

    public static boolean validPhone(String phone) {
        if (phone.length() != 11) {
            return false;
        }
        String check = "^[0-9]*$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(phone);
        return matcher.matches();
    }
}
