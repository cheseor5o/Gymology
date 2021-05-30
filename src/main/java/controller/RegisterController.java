package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.*;
import util.*;

import java.net.URL;
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
            String passwordText = password1.getText();
            String genderValue = gender.getValue();
            String phoneText = phone.getText();
            if (!validField(usernameText)) {
                prompt.setText("Invalid Username!");
            } else if (!validPassword(passwordText, password2.getText())) {
                prompt.setText("Invalid Password! (Must > 6)");
            } else if (genderValue != null && !validField(genderValue)) {
                prompt.setText("Select gender plz!");
            } else if (!validPhone(phoneText)) {
                prompt.setText("Invalid phone number!");
            } else if (!verifyCode.getText().equals(checkCode)) {
                prompt.setText("Incorrect check code!");
            } else {
                User user = new User();
                user.setEmail(emailText);
                user.setIdentity(User.Identity.Customer);
                if (!UserDataBase.exists(UserDataBase.contains(user), user)) {
                    user.setPassword(passwordText);
                    switch (user.getIdentity()) {
                        case Customer:
                            user.setIdentity(User.Identity.Customer);
                            Customer customer = new Customer(user);
                            customer.setName(usernameText);
                            customer.setPhone(phoneText);
                            customer.setGender(genderValue);
                            CustomerDatabase.add(customer);
                            break;
                        case Coach:
                            user.setIdentity(User.Identity.Coach);
                            Coach coach = new Coach(user);
                            coach.setName(usernameText);
                            coach.setPhone(phoneText);
                            coach.setGender(genderValue);
                            CoachDatabase.add(coach);
                            break;
                        case Manager:
                            user.setIdentity(User.Identity.Manager);
                            Manager manager = new Manager(user);
                            manager.setName(usernameText);
                            manager.setPhone(phoneText);
                            manager.setGender(genderValue);
                            
                    }
                    
                    UserDataBase.add(user);
                    prompt.setText("");
                    Controllers.get(LoginController.class).loginScene("Register Successfully!");
                } else {
                    prompt.setText("This email is already been used!");
                }
            }
        } else {
            email.setText("");
            prompt.setText("Email invalid!");
        }
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
