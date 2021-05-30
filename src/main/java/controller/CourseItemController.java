package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.Course;
import model.Customer;
import model.User;
import util.Controllers;
import util.CustomerDatabase;


import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Cheese
 * @date 2021/4/26 23:11
 */
public class CourseItemController extends AbstractController {
    
    private Course course;


    @FXML
    private Pane courseItemPane;
    @FXML
    private ImageView coursePic;
    @FXML
    private ImageView courseVipLogo;
    @FXML
    private Label courseSort;
    @FXML
    private Label courseTime;
    @FXML
    private Label courseName;
    @FXML
    private Button functionBtn;
    @FXML
    private Button likeButton;


    private final String editIcon = Objects.requireNonNull(getClass().getResource("/img/edit.png")).toString();
    private final String likeIcon = Objects.requireNonNull(getClass().getResource("/img/like.png")).toString();
    private final Image VipIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/Vip.png")));

    
    //TODO 身份识别的方法
    public void setCourseData(Course course) {
        this.course = course;
        courseName.setText(course.getName());
        courseSort.setText(course.getSort());
        courseTime.setText(course.getTime());
        if (course.getVip()) {
            courseVipLogo.setImage(VipIcon);
        }
        if (!"null".equals(course.getPicture())) {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(course.getPicture())));
            coursePic.setImage(image);
        }
    }

    private void setButtonIcon(Button button, String path) {
        Image icon = new Image(path);
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(25);
        imageView.setFitHeight((int) (25 * icon.getHeight() / icon.getWidth()));
        button.setGraphic(imageView);


        ColorAdjust colorAdjust = new ColorAdjust();
        button.setOnMousePressed(event -> {
            colorAdjust.setBrightness(0.5);
            button.setEffect(colorAdjust);
        });
        button.setOnMouseReleased(event -> {
            colorAdjust.setBrightness(0);
            button.setEffect(colorAdjust);
        });
    }

    @FXML
    private void clickCourse() {
        CoursePlayerController coursePlayerController = Controllers.get(CoursePlayerController.class);
        if (!course.getVip()){
            coursePlayerController.mediaPlayerOnLoad(course);
            coursePlayerController.scene();
        }else {
            User user = Controllers.get(LoginController.class).getUser();
            if (user == null){
                Controllers.setCenter(LoginController.class, false);
            }else if (user.getIdentity() == User.Identity.Customer){
                Customer customer = CustomerDatabase.get(user.getEmail());
                if (customer.getVip()){
                    coursePlayerController.mediaPlayerOnLoad(course);
                    coursePlayerController.scene();
                }else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Dear "+customer.getName() + ", ");
                    alert.setContentText("This course is for VIP customer,Please go to purchase VIP first!");
                    alert.showAndWait();
                }
            }else {
                coursePlayerController.mediaPlayerOnLoad(course);
                coursePlayerController.scene();
            }

        }


    }

    @FXML
    void clickButton() {
        CourseEditController courseEditController = Controllers.get(CourseEditController.class);
        courseEditController.unLoad();
        courseEditController.onLoad(course);
        courseEditController.setFlag(true);
        Controllers.get(CourseEditController.class).scene();

    }

    @FXML
    private void mouseIn() {
        User user = Controllers.get(LoginController.class).getUser();
        if (user == null){
            functionBtn.setVisible(false);
            likeButton.setVisible(false);
        }else if (user.getIdentity() == User.Identity.Customer){
            likeButton.setVisible(true);
        }else if (user.getIdentity() == User.Identity.Manager){
            functionBtn.setVisible(true);
        }else if (user.getIdentity() == User.Identity.Coach){
            functionBtn.setVisible(false);
            likeButton.setVisible(false);
        }
        functionBtn.setOpacity(100);
        likeButton.setOpacity(100);
    }

    @FXML
    private void mouseOut() {
        functionBtn.setOpacity(0);
        likeButton.setOpacity(0);
        likeButton.setVisible(false);
        functionBtn.setVisible(false);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setButtonIcon(functionBtn, editIcon);
        setButtonIcon(likeButton, likeIcon);
        courseItemPane.setCursor(Cursor.HAND);
    }
}
