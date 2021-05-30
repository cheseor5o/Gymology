package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Coach;
import model.Customer;
import model.Order;
import model.User;
import util.CoachDatabase;
import util.Controllers;
import util.CustomerDatabase;
import util.OrderDatabase;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Cheese
 * @date 2021/5/28 12:14
 */
public class OrderController extends AbstractController {
    
    private Boolean changed;

    @FXML
    public VBox orderInfoVbox;

    @Override
    public void scene() {
        if (Controllers.get(LoginController.class).hasLogging()) {
            if (changed == null || changed){
                User user = Controllers.get(LoginController.class).getUser();
                switch (user.getIdentity()){
                    case Coach:
                        Coach coach = CoachDatabase.get(user.getEmail());
                        List<Order> coachOrders = OrderDatabase.getOrders(coach);
                        displayOrder(coachOrders);
                        break;
                    case Customer:
                        Customer customer = CustomerDatabase.get(user.getEmail());
                        List<Order> customerOrders = OrderDatabase.getOrders(customer);
                        displayOrder(customerOrders);
                        break;
                    default:
                        System.out.println("ERROR");
                }
                changed = false;
            }
            Controllers.setCenter(super.getScene());
        }else {
            Controllers.setCenter(LoginController.class,false);
        }
    }

    private void displayOrder(List<Order> orders) {
        orderInfoVbox.getChildren().clear();
        for (Order order : orders) {
            FXMLLoader orderInfoItemLoader = new FXMLLoader();
            orderInfoItemLoader.setLocation(getClass().getClassLoader().getResource("fxml/orderItem.fxml"));
            try {
                Pane orderInfoItemPane = orderInfoItemLoader.load();
                OrderItemController controller = orderInfoItemLoader.getController();
                controller.setAll(order);
                orderInfoVbox.getChildren().add(orderInfoItemPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean getChanged() {
        return changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
