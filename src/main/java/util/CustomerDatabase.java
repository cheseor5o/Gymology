package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import model.Customer;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomerDatabase {
    private static final ObjectMapper mapper;
    private static final File address;
    private static Map<String, Customer> map;
    private static final MapType mapType;
    static {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapType = mapper.getTypeFactory().constructMapType(HashMap.class,String.class,Customer.class);
        address = new File("src/main/resources/data/customer.json");
        try {
            map = mapper.readValue(address,mapType);
        } catch (IOException e) {
            map = new HashMap<>();
            e.printStackTrace();
        }
    }
    
    public static void add(Customer customer){
        map.put(customer.getId(), customer);
    }
    
    public static void store() {
        try {
            mapper.writeValue(address,map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Customer get(String id){
        return map.get(id);
    }
    
    public static void show(){
        System.out.println(map);
    }
    
}
