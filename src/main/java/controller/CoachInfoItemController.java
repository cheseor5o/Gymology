package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Coach;
import model.Customer;
import model.Order;
import model.User;
import util.Controllers;
import util.CustomerDatabase;
import util.OrderDatabase;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Cheese
 * @since  2021/5/9 22:14
 */
public class CoachInfoItemController extends AbstractController {

    @FXML
    private Label coachInfoItemTime;

    @FXML
    private Button coachInfoItemReserveButton;

    @FXML
    private Label coachInfoItemStatusLabel;
    
    public void setCoachInfoItemData(String time){
        coachInfoItemTime.setText(time);
    }

    public Button getCoachInfoItemReserveButton() {
        return coachInfoItemReserveButton;
    }

    public Label getCoachInfoItemStatusLabel() { return coachInfoItemStatusLabel; }

    @FXML
    public void reserveCoach(){
        LoginController loginController = Controllers.get(LoginController.class);
        User user = loginController.getUser();
        if (user == null){
            Controllers.setCenter(loginController.getScene(),false);
        }else if (user.getIdentity().equals(User.Identity.Customer)){
            CoachInfoController coachInfoController = Controllers.get(CoachInfoController.class);
            reserve(user, coachInfoController.getCoach(),coachInfoItemTime.getText());
            try {
                coachInfoController.setCoachInfoData(coachInfoController.getCoach());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void reserve(User user, Coach coach, String time){
        String id = System.currentTimeMillis() + "";
        Customer customer = CustomerDatabase.get(user.getEmail());
        Order order = new Order(id,customer.getId(),coach.getId(), time,coach.getPrice());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dear "+CustomerDatabase.get(order.getCustomer()).getName() + ", ");
        if (customer.order(order)) {
            coach.reserve(order);
            OrderDatabase.add(order);
            Controllers.get(OrderController.class).setChanged(true);
            alert.setContentText("Reserve successfully");
        }else {
            alert.setContentText("Fail! No enough balance!");
        }
        alert.showAndWait();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
