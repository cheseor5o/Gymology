package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Coach;
import model.User;
import util.CoachDatabase;
import util.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Cheese
 * @date 2021/5/9 13:40
 */
public class CoachController extends AbstractController {

    @FXML
    private VBox coachVbox;

    @Override
    public void scene(){
        if (Controllers.get(LoginController.class).hasLogging()) {
            User user = Controllers.get(LoginController.class).getUser();
            switch (user.getIdentity()) {
                case Customer:
                    try { setCoachScene(Coach.Sort.ALL); }catch (Exception e) { e.printStackTrace(); }
                    Controllers.setCenter(super.getScene());
                    break;
                case Coach:
                    Coach coach = CoachDatabase.get(user.getEmail());
                    try { setCoachEditScene(coach); }catch (Exception e) { e.printStackTrace(); }
                    Controllers.setCenter(CoachInfoController.class);
                    break;
                default:
                    System.out.println("ERROR");
            }
        }else {
            try { setCoachScene(Coach.Sort.ALL); }catch (Exception e) { e.printStackTrace(); }
            Controllers.setCenter(super.getScene());
        }
    }

    public void setCoachScene(Coach.Sort sort) throws Exception {
        Controllers.get(CoachInfoController.class).getCoachAddBtn().setVisible(false);
        coachVbox.getChildren().clear();
        List<Coach> coaches = CoachDatabase.getCoaches(sort);
        for (Coach coach:coaches){
            FXMLLoader coachItemLoader = new FXMLLoader();
            coachItemLoader.setLocation(getClass().getClassLoader().getResource("fxml/coachItem.fxml"));
            Pane coachItemPane = coachItemLoader.load();
            CoachItemController coachItemController = coachItemLoader.getController();
            coachItemController.setCoachData(coach);
            coachVbox.getChildren().add(coachItemPane);
        }

    }

    public void setCoachEditScene(Coach coach) throws Exception {
        Controllers.get(CoachInfoController.class).getCoachAddBtn().setVisible(true);
        coachVbox = Controllers.get(CoachInfoController.class).getCoachInfoVbox();
        Controllers.get(CoachInfoController.class).setCoachInfoData(coach);

        coachVbox.getChildren().clear();
        CoachDatabase.get(coach.getId());
        List<Coach.Schedule> timeStrings = coach.getTime();
        for(Coach.Schedule schedule:timeStrings){
            FXMLLoader coachEditItemLoader = new FXMLLoader();
            coachEditItemLoader.setLocation(getClass().getClassLoader().getResource("fxml/coachEditItem.fxml"));
            Pane coachEditItemPane = coachEditItemLoader.load();
            CoachEditItemController coachEditItemController = coachEditItemLoader.getController();
            coachEditItemController.getCoachEditTextField().setText(schedule.getTime());
            if (schedule.isOrdered()){
                coachEditItemController.getCoachEditSaveBtn().setDisable(true);
                coachEditItemController.getCoachEditDeleteBtn().setDisable(true);
                coachEditItemController.getCoachEditTimeLabel().setTextFill(Color.RED);
                coachEditItemController.getCoachEditTextField().setDisable(true);
                coachEditItemController.getCoachEditTimeLabel().setText("Unavailable");

            }
            coachVbox.getChildren().add(coachEditItemPane);
            coachEditItemController.setFlag(coachEditItemPane);
            coachEditItemController.setSchedule(schedule);
        }




    }






    @Override
    public void initialize(URL location, ResourceBundle resources) { }
}
