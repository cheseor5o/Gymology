package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import model.Coach;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CoachDatabase {
    private static final ObjectMapper mapper;
    private static final File address;
    private static Map<String, Coach> map;
    private static final MapType mapType;

    static {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Coach.class);
        address = new File("src/main/resources/data/coach.json");
        try {
            map = mapper.readValue(address, mapType);
        } catch (IOException e) {
            map = new HashMap<>();
            e.printStackTrace();
        }
    }

    /**
     * 添加新教练
     * @param coach coach
     */
    public static void add(Coach coach) {
        map.put(coach.getId(), coach);
    }

    /**
     * 更新教练信息
     * @param coach 需要被更新的coach
     * @return 更新成功or失败
     */
    public static boolean update(Coach coach){
        Coach oldCoach = map.get(coach.getId());
        if (oldCoach == null) return false;
        else {
            add(coach);
            return true;
        }
    }

    /**
     * 返回指定的coach
     * @param id coach's id
     * @return coach
     */
    public static Coach get(String id){
        return map.get(id);
    }

    /**
     * 
     * @param sort 查找教练的类型(ALL为所有教练）
     * @return 教练的List
     */
    public static List<Coach> getCoaches(Coach.Sort sort){
        return map.values().stream().filter(e -> sort.equals(Coach.Sort.ALL) || e.getSort().equals(sort)).collect(Collectors.toList());
    }

    /**
     * 储存至json
     */
    public static void store() {
        try {
            mapper.writeValue(address,map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    


    public static void show() {
        System.out.println(map);
    }

//    public static void main(String[] args) {
//        show();
//
//        Coach coach = get("1029899255@qq.com");
//        System.out.println(coach);
//
//        Coach coach1 = new Coach();
//
//        coach1.setId("11111");
//        coach1.setOrders(coach.getOrders());
//
//        add(coach1);
//        System.out.println(coach1);
//        store();
//
//
////        coach.setId("123");
////        add(coach);
////        store();
//
//
//
//    }


}
