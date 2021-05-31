package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Coach;
import model.Instance;
import util.Controllers;
import util.Databases;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Cheese
 * @date 2021/5/30 11:55
 */
public class CoachEditItemController extends AbstractController {

    @FXML
    private TextField coachEditTextField;

    @FXML
    private Label coachEditTimeLabel;

    @FXML
    private Button coachEditDeleteBtn;

    @FXML
    private Button coachEditSaveBtn;

    public Pane flag;

    public Coach.Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Coach.Schedule schedule) {
        this.schedule = schedule;
    }

    public Coach.Schedule schedule;


    public void setFlag(Pane flag) {
        this.flag = flag;
    }

    private VBox coachVbox = Controllers.get(CoachInfoController.class).getCoachInfoVbox() ;

    public TextField getCoachEditTextField() {
        return coachEditTextField;
    }

    public Label getCoachEditTimeLabel() {
        return coachEditTimeLabel;
    }

    public Button getCoachEditDeleteBtn() {
        return coachEditDeleteBtn;
    }

    public Button getCoachEditSaveBtn() {
        return coachEditSaveBtn;
    }

    public void delete(){
        Instance instance = Controllers.get(LoginController.class).getUser();
        Coach coach = Databases.getDatabase(Coach.class).get(instance.getEmail());
        List<Coach.Schedule> timeStrings = coach.getTime();
        timeStrings.remove(schedule);
        coachVbox.getChildren().remove(flag);
    }

    public void save(){
        System.out.println(schedule);
        schedule.setTime(coachEditTextField.getText());
    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
