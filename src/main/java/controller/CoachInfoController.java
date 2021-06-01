package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Coach;
import util.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Manage the scene of all coach
 */
public class CoachInfoController extends AbstractController {
    @FXML
    private VBox coachInfoVbox;

    @FXML
    private Label coachInfoNameLb;

    @FXML
    private Label coachInfoGenderLb;

    @FXML
    private Label coachInfoSortLb;

    @FXML
    private Label coachInfoWeightLb;

    @FXML
    private Label coachInfoHeightLb;

    @FXML
    private Text coachInfoDescriptionLb;

    @FXML
    private Label coachInfoPriceLb;

    @FXML
    private Button coachAddBtn;

    @FXML
    private ImageView coachInfoPic;

    private VBox coachVbox;
    
    private Coach coach;

    public Button getCoachAddBtn() {
        return coachAddBtn;
    }

    public VBox getCoachInfoVbox() {
        return coachInfoVbox;
    }

    public VBox getCoachVbox() {
        return coachVbox;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }



    public void setCoachInfoData(Coach coach) throws Exception {
        this.coach = coach;
        Image image =new Image(Objects.requireNonNull(getClass().getResourceAsStream(coach.getPicture())));
        coachInfoPic.setImage(image);
        coachInfoNameLb.setText(coach.getName());
        coachInfoGenderLb.setText(coach.getGender());
        coachInfoSortLb.setText(coach.getSort().toString());
        coachInfoPriceLb.setText(Double.toString(coach.getPrice()));
        coachInfoWeightLb.setText(coach.getWeight());
        coachInfoHeightLb.setText(coach.getHeight());
        coachInfoDescriptionLb.setText(coach.getDescription());

        List<Coach.Schedule> timeStrings = coach.getTime();
        coachInfoVbox.getChildren().clear();
        for(Coach.Schedule schedule:timeStrings){
            FXMLLoader coachInfoItemLoader = new FXMLLoader();
            coachInfoItemLoader.setLocation(getClass().getClassLoader().getResource("fxml/coachInfoItem.fxml"));
            Pane coachInfoItemPane = coachInfoItemLoader.load();
            CoachInfoItemController coachInfoItemController = coachInfoItemLoader.getController();
            coachInfoItemController.setCoachInfoItemData(schedule.getTime());
            if (schedule.isOrdered()){
                coachInfoItemController.getCoachInfoItemReserveButton().setDisable(true);
                coachInfoItemController.getCoachInfoItemStatusLabel().setTextFill(Color.RED);
                coachInfoItemController.getCoachInfoItemStatusLabel().setText("Unavailable");
            }
            coachInfoVbox.getChildren().add(coachInfoItemPane);
        }
    }

    public void coachEditAddTime() throws IOException {
        coachVbox= Controllers.get(CoachInfoController.class).getCoachInfoVbox();
        FXMLLoader coachEditItemLoader = new FXMLLoader();
        coachEditItemLoader.setLocation(getClass().getClassLoader().getResource("fxml/coachEditItem.fxml"));
        Pane coachEditItemPane = coachEditItemLoader.load();
        CoachEditItemController coachEditItemController = coachEditItemLoader.getController();
        coachEditItemController.getCoachEditTextField().setText(new Date().toString());
        coachEditItemController.setFlag(coachEditItemPane);
        coachVbox.getChildren().add(coachEditItemPane);
        List<Coach.Schedule> timeStrings = coach.getTime();
        Coach.Schedule schedule = new Coach.Schedule(coachEditItemController.getCoachEditTextField().getText(),false);

        timeStrings.add(schedule);
        coachEditItemController.setSchedule(schedule);



    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
