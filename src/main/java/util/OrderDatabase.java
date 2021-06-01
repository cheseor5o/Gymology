package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;

import model.Coach;
import model.Customer;
import model.Order;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDatabase {
    private static final ObjectMapper mapper;
    private static final File address;
    private static Map<String, Order> map;
    private static final MapType mapType;

    static {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        address = new File("src/main/resources/data/order.json");
        mapType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Order.class);
        try {
            map = mapper.readValue(address, mapType);
        } catch (IOException e) {
            map = new HashMap<>();
            System.out.println("This is not a serious problem, only the initial read fail!");
            e.printStackTrace();
        }
    }

    public static void add(Order order) {
        map.put(order.getId(), order);
    }

    public static Order get(String id) {
        return map.get(id);
    }

    public static List<Order> getOrders() {
        return new ArrayList<>(map.values());
    }

    /**
     * 查找并返回指定顾客的orders
     *
     * @param customer 指定的顾客
     * @return 指定顾客的orders
     */
    public static List<Order> getOrders(Customer customer) {
        List<Order> orders = new ArrayList<>();
        for (String order : customer.getOrders()) {
            Order customerOrder = map.get(order);
            if (customerOrder != null) orders.add(customerOrder);
        }
        return orders;
    }

    /**
     * 查找并返回指定顾客的orders
     *
     * @param coach 指定的顾客
     * @return 指定顾客的orders
     */
    public static List<Order> getOrders(Coach coach) {
        List<Order> orders = new ArrayList<>();
        for (String order : coach.getOrders()) {
            Order coachOrder = map.get(order);
            if (coachOrder != null) orders.add(coachOrder);
        }
        return orders;
    }


    public static void delete(String id) {
        map.remove(id);
    }

    public static void store() {
        try {
            mapper.writeValue(address, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

}
