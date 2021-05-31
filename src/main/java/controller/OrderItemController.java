package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import model.Coach;
import model.Customer;
import model.Order;
import model.User;
import util.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Cheese
 * @date 2021/5/28 16:26
 */
public class OrderItemController extends AbstractController {
    
    @FXML
    private Label id;
    @FXML
    private Label time;
    @FXML
    private Label status;
    @FXML
    private Label instance;

    @FXML
    private Button finish;
    @FXML
    private Button cancel;
    
    private Order order;
    
    public Label getId() {
        return id;
    }
    
    @FXML
    public boolean finish(){
        
        Customer customer = Databases.getDatabase(Customer.class).get(order.getCustomer());
        Alert alert = Tools.openMessage(Alert.AlertType.CONFIRMATION, "Order Center", "Dear " + customer.getName(), "Are you sure you have finished this course?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            System.out.println("Yes");
        }else {
            System.out.println("No");
        }
        return true;
    }

    @FXML
    public void cancel(){
        Customer customer = Databases.getDatabase(Customer.class).get(order.getCustomer());
        Alert alert = Tools.openMessage(Alert.AlertType.CONFIRMATION, "Order Center", "Dear " + customer.getName(), "Are you sure you want to cancel this course?");
        if (alert.getResult() == ButtonType.OK){
            String content;
            if (order.getTime().compareTo(OrderDatabase.now())>0){//可以退
                Coach coach = Databases.getDatabase(Coach.class).get(order.getCoach());
                customer.cancelOrder(order);
                coach.cancelOrder(order);
                OrderDatabase.delete(order.getId());
                content = "Cancel successfully!";
                OrderController orderController = Controllers.get(OrderController.class);
                orderController.setChanged(true);
                orderController.scene();
            }else {
                content = "Cannot be canceled!";
            }
            Tools.openMessage(Alert.AlertType.INFORMATION, "Order Center", "Dear " + customer.getName(), content).showAndWait();
        }
    }

    public void setAll(Order order, Class<? extends User> clazz){
        setOrder(order);
        id.setText(order.getId());
        time.setText(order.getTime());
        status.setText(order.getStatus().toString());
        instance.setText(Databases.getDatabase(clazz).get(clazz == Customer.class ? order.getCustomer() : order.getCoach()).getId());
    }
    
    public void setId(Label id) {
        this.id = id;
    }

    public Label getTime() {
        return time;
    }

    public void setTime(Label time) {
        this.time = time;
    }

    public Label getStatus() {
        return status;
    }

    public void setStatus(Label status) {
        this.status = status;
    }

    public Label getInstance() {
        return instance;
    }

    public void setInstance(Label instance) {
        this.instance = instance;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }
}
