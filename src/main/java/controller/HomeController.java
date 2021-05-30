package controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import util.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Cheese
 * @date 2021/4/26 23:11
 */
public class HomeController extends AbstractController {
    
    

    @FXML
    private AnchorPane homePane1;

    @FXML
    private AnchorPane homePane2;

    @FXML
    private AnchorPane homePane3;
    

    public void translateAnimation(double duration, Node node, double width){
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration),node);
        translateTransition.setByX(width);
        translateTransition.play();
    }


    int show = 0;
    public void next(){

        if (show==0){
            translateAnimation(0.5,homePane2,-600);
            show++;
        }else if (show==1){
            translateAnimation(0.5,homePane3,-600);
            show++;
        }else if (show==2){
            back();
            back();
        }
    }
    public void back(){
        if (show==0){
            next();
            next();
        }else if (show==1){
            translateAnimation(0.5,homePane2,600);
            show--;
        }else if (show==2){
            translateAnimation(0.5,homePane3,600);
            show--;
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        translateAnimation(0.5,homePane2,600);
        translateAnimation(0.5,homePane3,600);
    }

}
