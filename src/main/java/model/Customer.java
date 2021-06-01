package model;

import util.Tools;

import java.util.List;
import java.util.Map;
/**
 * A POJO represents customer
 * @since  2021/5/9 13:40
 */
public class Customer extends User{

    public static boolean isCustomer(Instance instance){
        return instance.getIdentity().equals(Instance.Identity.Customer);
    }
    
    private String height;
    private String weight;
    private boolean vip;
    private String vipExpired;
    private int vipRank;
    private double balance;
    private List<String> likeCourse;
    private List<String> likeCoach;
    private List<Map<String, String>> courseHistory; //[视频1:时间点,视频2:时间点]
    private List<String> orders;

    public Customer() {
    }


    public boolean order(Order order){
        if (balance >= order.getPrice()){
            balance -= order.getPrice();
            orders.add(order.getId());
            return true;
        }
        return false;
    }

    /**
     * refund
     * @param order
     */
    public void refund(Order order){
        balance+=order.getPrice();
    }
    
    public void deleteOrder(Order order){
        int index = Tools.findIndex(orders, order.getId());
        if (exists(orders,index,order.getId())){
            orders.remove(index);
            refund(order);
        }
    }

    /**
     * If exist
     * @param likeList list need to be searched
     * @param content content
     * @return result
     */
    public boolean exists(List<String> likeList, String content){
        int favourite = Tools.findIndex(likeList, content);
        return exists(likeList,favourite,content);
    }
    
    private boolean exists(List<String> likeList, int index, String content){
        return index != likeList.size() && likeList.get(index).equals(content);
    }

    /**
     * Add favourite course
     * @param id course id
     */
    public void addFavouriteCourse(String id){
        int favourite = Tools.findIndex(likeCourse, id);
        if (!exists(likeCourse, favourite, id)){
            likeCourse.add(favourite, id);
        }
    }

    /**
     * Add favourite coach
     * @param id coach id
     */
    public void addFavouriteCoach(String id){
        int favourite = Tools.findIndex(likeCoach, id);
        likeCoach.add(favourite, id);
    }

    public void removeFavouriteCourse(String id){
        likeCourse.remove(id);
    }

    public void removeFavouriteCoach(String id){
        likeCoach.remove(id);
    }
    
    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public String getVipExpired() {
        return vipExpired;
    }

    public void setVipExpired(String vipExpired) {
        this.vipExpired = vipExpired;
    }

    public int getVipRank() {
        return vipRank;
    }

    public void setVipRank(int vipRank) {
        this.vipRank = vipRank;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<String> getLikeCourse() {
        return likeCourse;
    }

    public void setLikeCourse(List<String> likeCourse) {
        this.likeCourse = likeCourse;
    }

    public List<String> getLikeCoach() {
        return likeCoach;
    }

    public void setLikeCoach(List<String> likeCoach) {
        this.likeCoach = likeCoach;
    }

    public List<Map<String, String>> getCourseHistory() {
        return courseHistory;
    }

    public void setCourseHistory(List<Map<String, String>> courseHistory) {
        this.courseHistory = courseHistory;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + super.getId() + '\'' +
                ", name='" + super.getName() + '\'' +
                ", gender='" + super.getGender() + '\'' +
                ", phone='" + super.getPhone() + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", vip=" + vip +
                ", vipExpired='" + vipExpired + '\'' +
                ", vipRank='" + vipRank + '\'' +
                ", balance='" + balance + '\'' +
                ", likeCourse=" + likeCourse +
                ", likeCoach=" + likeCoach +
                ", courseHistory=" + courseHistory +
                ", orders=" + orders +
                '}';
    }
}
