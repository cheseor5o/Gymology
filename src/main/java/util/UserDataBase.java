package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDataBase {
    private static final ObjectMapper mapper;
    private static final File address;
    private static List<User> userList;
    private static final CollectionType listType;

    static {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        address = new File("src/main/resources/data/user.json");
        listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, User.class);
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
            userList = new ArrayList<>();
        }
    }

    public static boolean add(User user) {
        int index = contains(user);
        if (!exists(index,user)) {//Not exist this user
            synchronized (UserDataBase.class){
                userList.add(index, user);
            }
            return true;
        }
        return false;
    }
    
    public static User get(int index){
        return userList.get(index);
    }

    /**
     * 判断是否有该user
     * @param index {@link #contains(User)} 返回的index
     * @param user 新注册user
     * @return true，如果存在。false，不存在
     */
    public static boolean exists(int index, User user){
        return index != userList.size() && userList.get(index).getEmail().equals(user.getEmail());
    }

    /**
     * 查找是否有该user
     *
     * @param user 新注册user
     * @return 若有，返回其坐标；若没有，返回应该插入的坐标
     */
    public static int contains(User user) {
        int left = 0, right = userList.size() - 1;
        String userEmail = user.getEmail();
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String midEmail = userList.get(mid).getEmail();
            if (userEmail.compareTo(midEmail) > 0) {
                left = mid + 1;
            } else if (userEmail.compareTo(midEmail) < 0) {
                right = mid - 1;
            } else return mid;
        }
        return left;
    }

    public static void store() throws IOException {
            mapper.writeValue(address, userList);
    }

    public static void load() throws IOException {
        userList = mapper.readValue(address, listType);
    }
    
}
