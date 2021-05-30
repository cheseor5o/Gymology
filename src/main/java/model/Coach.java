package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import util.Tools;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cheese
 * @since  2021/5/9 13:40
 */
public class Coach {
    public enum Sort{
        ALL, LOSE_WEIGHT
    }
    
    private String id;
    private String name;
    private Sort sort;
    private String picture;
    private List<Schedule> time;
    private String gender;
    private double price;
    private String description;
    private String height;
    private String weight;
    private List<String> orders;
    private String phone;
    @JsonIgnore
    private List<Schedule> available;
    public Coach() {
    }

    public Coach(User user) {
        this.id = user.getEmail();
    }
    
    public static class Schedule{
        private String time;
        private boolean ordered;

        public Schedule() {
        }

        public Schedule(String time, boolean ordered) {
            this.time = time;
            this.ordered = ordered;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean isOrdered() {
            return ordered;
        }

        public void setOrdered(boolean ordered) {
            this.ordered = ordered;
        }

        @Override
        public String toString() {
            return "Schedule{" +
                    "time='" + time + '\'' +
                    ", ordered=" + ordered +
                    '}';
        }
    }

    
    /**
     * 预定功能,先获得可预订的list，再查找到那个时间
     * @param order order
     */
    public void reserve(Order order){
        int left = 0, right = available.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String midTime = available.get(mid).getTime();
            if (order.getTime().compareTo(midTime) > 0) {
                left = mid + 1;
            } else if (order.getTime().compareTo(midTime) < 0) {
                right = mid - 1;
            } else {//找到了
                available.get(mid).setOrdered(true);
                available.remove(mid);
                orders.add(order.getId());
                break;
            }
        }
    }
    
    public int findSchedule(List<Schedule> schedules, String time){
        int left = 0, right = schedules.size() - 1;
        while (left <= right){
            int mid = left + (right - left) / 2;
            String midTime = schedules.get(mid).getTime();
            if (time.compareTo(midTime) > 0) {
                left = mid + 1;
            } else if (time.compareTo(midTime) < 0) {
                right = mid - 1;
            } else {//找到了
                return mid;
            }
        }
        return left;
    }
    
    public void cancelOrder(Order order){
        int index = Tools.findIndex(orders, order.getId());
        if (exists(orders,index,order.getId())){
            orders.remove(index);
            int scheduleIndex = findSchedule(time,order.getTime());
            if (scheduleIndex != time.size() && time.get(scheduleIndex).getTime().equals(order.getTime())){
                time.get(scheduleIndex).setOrdered(false);
                int schedule = findSchedule(available, order.getTime());
                available.add(schedule,time.get(scheduleIndex));
            }
        }
    }

    private boolean exists(List<String> list, int index, String content){
        return index != list.size() && list.get(index).equals(content);
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public List<Schedule> getTime() {
        return time;
    }

    public void setTime(List<Schedule> time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取可预订时间段
     * @return 含有可用schedule的list
     */
    public List<Schedule> getAvailable() {
        if (available == null){
            available = time.stream().filter(e -> !e.ordered).collect(Collectors.toList());
        }
        return available;
    }

    
    public void setAvailable(List<Schedule> available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Coach{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", picture='" + picture + '\'' +
                ", time=" + time +
                ", gender='" + gender + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", orders=" + orders +
                ", phone='" + phone + '\'' +
                ", available=" + available +
                '}';
    }
}
