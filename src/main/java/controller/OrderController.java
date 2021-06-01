package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.*;
import util.Controllers;
import util.Databases;
import util.OrderDatabase;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Show up orders
 */
public class OrderController extends AbstractController {

    @FXML
    public Label userLabel;
    private Boolean changed;
    @FXML
    public VBox orderInfoVbox;

    @Override
    public void scene() {
        if (Controllers.get(LoginController.class).hasLogging()) {
            if (changed == null || changed){
                Instance instance = Controllers.get(LoginController.class).getInstance();
                switch (instance.getIdentity()){
                    case Coach:
                        userLabel.setText("Customer");
                        Coach coach = Databases.getDatabase(Coach.class).get(instance.getEmail());
                        List<Order> coachOrders = OrderDatabase.getOrders(coach);
                        displayOrder(coachOrders, Customer.class);
                        break;
                    case Customer:
                        userLabel.setText("Coach");
                        Customer customer = Databases.getDatabase(Customer.class).get(instance.getEmail());
                        List<Order> customerOrders = OrderDatabase.getOrders(customer);
                        displayOrder(customerOrders, Coach.class);
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

    private void displayOrder(List<Order> orders, Class<? extends User> clazz) {
        orderInfoVbox.getChildren().clear();
        for (Order order : orders) {
            FXMLLoader orderInfoItemLoader = new FXMLLoader();
            orderInfoItemLoader.setLocation(getClass().getClassLoader().getResource("fxml/orderItem.fxml"));
            try {
                Pane orderInfoItemPane = orderInfoItemLoader.load();
                OrderItemController controller = orderInfoItemLoader.getController();
                controller.setAll(order, clazz);
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
