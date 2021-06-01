package model;

/**
 * A POJO represents order
 * @since  2021/5/9 13:40
 */
public class Order {
    
    public enum Status{
        PROCESSING, CANCELED, FINISH
    }

    private String id;
    private Status status;
    private String customer;
    private String coach;
    private String time;
    private double price;

    public Order() {
    }

    public Order(String id, String customer, String coach, String time, double price) {
        this.id = id;
        this.customer = customer;
        this.coach = coach;
        this.time = time;
        this.price = price;
        this.status = Status.PROCESSING;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", customer='" + customer + '\'' +
                ", coach='" + coach + '\'' +
                ", time='" + time + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
