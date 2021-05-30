package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import model.Coach;
import model.Customer;
import model.Order;
import util.CoachDatabase;
import util.Controllers;
import util.CustomerDatabase;
import util.OrderDatabase;

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
    private Label user;

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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        System.out.println(order);
        Customer customer = CustomerDatabase.get(order.getCustomer());
        alert.setTitle("Dear "+ customer.getName() + ", ");
        alert.setContentText("Are you sure you have finished this course?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            System.out.println("Yes");
        }else {
            System.out.println("No");
        }
        return true;
    }

    @FXML
    public boolean cancel(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Customer customer = CustomerDatabase.get(order.getCustomer());
        alert.setTitle("Dear "+ customer.getName() + ", ");
        alert.setContentText("Are you sure you want to cancel this course?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Dear "+ customer.getName() + ", ");
            if (order.getTime().compareTo(OrderDatabase.now())>0){//可以退
                Coach coach = CoachDatabase.get(order.getCoach());
                customer.cancelOrder(order);
                coach.cancelOrder(order);
                OrderDatabase.delete(order.getId());
                alert1.setContentText("Cancel successfully!");
                alert1.showAndWait();
                OrderController orderController = Controllers.get(OrderController.class);
                orderController.setChanged(true);
                orderController.scene();
            }else {
                alert1.setContentText("Cannot be canceled!");
                alert1.showAndWait();
            }
            
        }else {
            System.out.println("No");
        }
        return true;
    }

    public void setAll(Order order){
        setOrder(order);
        id.setText(order.getId());
        time.setText(order.getTime());
        status.setText(order.getStatus().toString());
        user.setText(CustomerDatabase.get(order.getCustomer()).getName());
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

    public Label getUser() {
        return user;
    }

    public void setUser(Label user) {
        this.user = user;
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
