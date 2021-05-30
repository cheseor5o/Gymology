package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import model.Coach;
import model.Customer;
import model.User;
import util.Controllers;
import util.CustomerDatabase;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Cheese
 * @date 2021/5/9 15:18
 */
public class CoachItemController extends AbstractController {
    
    private Coach coach;
    
    @FXML
    private ImageView coachPicture;

    @FXML
    private Label coachName;

    @FXML
    private Label coachSort;

    @FXML
    public Label coachPrice;
    
    @FXML
    private Label coachTime;

    @FXML
    private Button coachCheckButton;

    @FXML
    private Button coachLikeButton;
    


    public void setCoachData(Coach coach){
        this.coach = coach;
        coachName.setText(coach.getName());
        coachSort.setText(coach.getSort().toString());
        coachPrice.setText(String.valueOf(coach.getPrice()));
        coachTime.setText(coach.getAvailable().size() == 0 ? "No" : "Yes");
        LoginController loginController = Controllers.get(LoginController.class);
        User user = loginController.getUser();
        if (!loginController.hasLogging()){
            coachLikeButton.setTextFill(Color.GREEN);
        }else if (user.getIdentity() == User.Identity.Customer){
            Customer customer = CustomerDatabase.get(user.getEmail());
            if (customer.exists(customer.getLikeCoach(),coach.getId())) {
                coachLikeButton.setText("dislike");
                coachLikeButton.setTextFill(Color.RED);
            }else {
                coachLikeButton.setText("like");
                coachLikeButton.setTextFill(Color.GREEN);
            }
        }

        if (!"null".equals(coach.getPicture())) {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(coach.getPicture())));
            coachPicture.setImage(image);
        }



    }
    public void clickCheckButton() throws Exception {
        CoachInfoController coachInfoController = Controllers.get(CoachInfoController.class);
        coachInfoController.setCoachInfoData(coach);
        Controllers.setCenter(coachInfoController.getScene());
    }

    public void clickLikeButton(){
        LoginController loginController = Controllers.get(LoginController.class);
        User user = loginController.getUser();
        if (!loginController.hasLogging()){
            Controllers.setCenter(loginController.getScene(),false);
        }else if (user.getIdentity() == User.Identity.Customer){
            System.out.println(user);
            Customer customer = CustomerDatabase.get(user.getEmail());
            if (!customer.exists(customer.getLikeCoach(),coach.getId())) {
                customer.addFavouriteCoach(coach.getId());
                coachLikeButton.setText("dislike");
                coachLikeButton.setTextFill(Color.RED);
            }else {
                coachLikeButton.setText("like");
                coachLikeButton.setTextFill(Color.GREEN);
                customer.removeFavouriteCoach(coach.getId());
            }
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}
