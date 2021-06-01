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
import util.Controllers;
import util.Databases;
import util.Tools;

import java.net.URL;
import java.util.ResourceBundle;


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
    public void finish(){
        Customer customer = Databases.getDatabase(Customer.class).get(order.getCustomer());
        Alert alert = Tools.openMessage(Alert.AlertType.CONFIRMATION, "Order Center", "Dear " + customer.getName(), "Are you sure you have finished this course?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            order.setStatus(Order.Status.FINISH);
            status.setText(order.getStatus().toString());
            Tools.openMessage(Alert.AlertType.INFORMATION, "Order Center", "Dear " + customer.getName(), "Finish! Thanks you!");
            refresh();
        }
    }

    @FXML
    public void cancel(){
        Customer customer = Databases.getDatabase(Customer.class).get(order.getCustomer());
        Alert alert = Tools.openMessage(Alert.AlertType.CONFIRMATION, "Order Center", "Dear " + customer.getName(), "Are you sure you want to cancel this course?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            String content;
            if (order.getTime().compareTo(Tools.now())>0){//可以退
                Coach coach = Databases.getDatabase(Coach.class).get(order.getCoach());
                customer.refund(order);
                coach.cancelOrder(order);
                order.setStatus(Order.Status.CANCELED);
                status.setText(order.getStatus().toString());
                content = "Cancel successfully!";
                refresh();
            }else {
                content = "Cannot be canceled!";
            }
            Tools.openMessage(Alert.AlertType.INFORMATION, "Order Center", "Dear " + customer.getName(), content).showAndWait();
        }
    }
    
    private void refresh(){
        OrderController orderController = Controllers.get(OrderController.class);
        orderController.setChanged(true);
        orderController.scene();
    }

    public void setAll(Order order, Class<? extends User> clazz){
        setOrder(order);
        id.setText(order.getId());
        time.setText(order.getTime());
        status.setText(order.getStatus().toString());
        boolean visible = order.getStatus().equals(Order.Status.PROCESSING);
        cancel.setVisible(visible);
        finish.setVisible(visible);
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
