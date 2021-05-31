package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserDatabase<T extends User> {
    private final ObjectMapper mapper;
    private final File address;
    private Map<String, T> map;
    private Predicate<T> sifter;
    public UserDatabase(Class<T> clazz) {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, clazz);
        char[] chars = clazz.getSimpleName().toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        address = new File("src/main/resources/data/"+new String(chars)+".json");
        try {
            map = mapper.readValue(address, mapType);
        } catch (IOException e) {
            map = new HashMap<>();
            e.printStackTrace();
        }
    }

    /**
     * 添加新user
     * @param user user
     */
    public void add(T user) {
        map.put(user.getId(), user);
    }

    /**
     * 返回指定的user
     * @param id user's id
     * @return usr
     */
    public T get(String id){
        return map.get(id);
    }

    /**
     * @return user的List
     */
    public List<T> getUsers(){
        return new ArrayList<>(map.values());
    }

    /**
     * 感觉条件筛选user
     * @param predicate 筛选条件
     * @return 筛选的list
     */
    public List<T> getUsers(Predicate<T> predicate){
        return map.values().stream().filter(predicate).collect(Collectors.toList());
    }


    /**
     * 更新user信息
     * @param user 需要被更新的user
     * @return 更新成功or失败
     */
    public boolean update(T user){
        T oldUser = map.get(user.getId());
        if (oldUser == null) return false;
        else {
            add(user);
            return true;
        }
    }
    

    /**
     * 储存至json
     */
    public void store() {
        try {
            mapper.writeValue(address,map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
