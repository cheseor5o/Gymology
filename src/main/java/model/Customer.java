package model;

import util.Tools;

import java.util.List;
import java.util.Map;

public class Customer extends User{
    
    private String height;
    private String weight;
    private Boolean vip;
    private String vipExpired;
    private String vipRank;
    private double balance;
    private List<String> likeCourse;
    private List<String> likeCoach;
    private List<Map<String, String>> courseHistory; //[视频1:时间点,视频2:时间点]
    private List<String> orders;

    public Customer() {
    }

    /**
     * 订购课程
     */
    public boolean order(Order order){
        if (balance >= order.getPrice()){
            balance -= order.getPrice();
            orders.add(order.getId());
            return true;
        }
        return false;
    }
    
    public void cancelOrder(Order order){
        int index = Tools.findIndex(orders, order.getId());
        if (exists(orders,index,order.getId())){
            orders.remove(index);
            balance+=order.getPrice();
        }
    }
    
    public boolean exists(List<String> likeList, String content){
        int favourite = Tools.findIndex(likeList, content);
        return exists(likeList,favourite,content);
    }
    
    private boolean exists(List<String> likeList, int index, String content){
        return index != likeList.size() && likeList.get(index).equals(content);
    }
    
    public void addFavouriteCourse(String id){
        int favourite = Tools.findIndex(likeCourse, id);
        if (!exists(likeCourse, favourite, id)){
            likeCourse.add(favourite, id);
        }
    }

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

    public Boolean getVip() {
        return vip;
    }

    public void setVip(Boolean vip) {
        this.vip = vip;
    }

    public String getVipExpired() {
        return vipExpired;
    }

    public void setVipExpired(String vipExpired) {
        this.vipExpired = vipExpired;
    }

    public String getVipRank() {
        return vipRank;
    }

    public void setVipRank(String vipRank) {
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
