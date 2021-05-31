package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Coach;
import model.Customer;
import model.Order;
import model.Instance;
import util.*;

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
        Instance instance = loginController.getInstance();
        if (instance == null){
            Controllers.setCenter(loginController.getScene(),false);
        }else if (instance.getIdentity().equals(Instance.Identity.Customer)){
            CoachInfoController coachInfoController = Controllers.get(CoachInfoController.class);
            reserve(instance, coachInfoController.getCoach(),coachInfoItemTime.getText());
            try {
                coachInfoController.setCoachInfoData(coachInfoController.getCoach());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void reserve(Instance instance, Coach coach, String time){
        String id = System.currentTimeMillis() + "";
        UserDatabase<Customer> database = Databases.getDatabase(Customer.class);
        Customer customer = database.get(instance.getEmail());
        Order order = new Order(id,customer.getId(),coach.getId(), time,coach.getPrice());
        String content;
        if (customer.order(order)) {
            coach.reserve(order);
            OrderDatabase.add(order);
            Controllers.get(OrderController.class).setChanged(true);
            content = "Reserve successfully";
        }else {
            content = "Fail! No enough balance!";
        }
        Tools.openMessage(Alert.AlertType.INFORMATION, "Coach Reservation Center", "Dear " + database.get(order.getCustomer()).getName(), content).showAndWait();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
