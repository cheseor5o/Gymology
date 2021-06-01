package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import model.Instance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An instance database keeps every instance that registers to this System,
 * which is, a {@link model.User}
 * or {@link model.Customer}, {@link model.Coach} and {@link model.Manager}, specifically
 */
public class InstanceDataBase {
    /**
     * A mapper that responsible for the data read and write
     */
    private static final ObjectMapper mapper;
    /**
     * Data file's address
     */
    private static final File address;
    /**
     * A list that keeping all the data in the file
     */
    private static List<Instance> instanceList;
    /**
     * Regulate that
     */
    private static final CollectionType listType;

    static {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        address = new File("src/main/resources/data/instance.json");
        listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Instance.class);
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
            instanceList = new ArrayList<>();
        }
    }

    public static boolean add(Instance instance) {
        int index = contains(instance);
        if (!exists(index, instance)) {//Not exist this instance
            synchronized (InstanceDataBase.class){
                instanceList.add(index, instance);
            }
            return true;
        }
        return false;
    }
    
    public static Instance get(int index){
        return instanceList.get(index);
    }

    public static boolean login(int index, Instance instance){
        if (index == instanceList.size()) return false;
        Instance in = instanceList.get(index);
        return in.getEmail().equals(instance.getEmail()) && in.getPassword().equals(instance.getPassword());
    }

    /**
     * 判断是否有该instance
     * @param index {@link #contains(Instance)} 返回的index
     * @param instance 新注册instance
     * @return true，如果存在。false，不存在
     */
    public static boolean exists(int index, Instance instance){
        return index != instanceList.size() && instanceList.get(index).getEmail().equals(instance.getEmail());
    }

    /**
     * 查找是否有该instance
     *
     * @param instance 新注册instance
     * @return 若有，返回其坐标；若没有，返回应该插入的坐标
     */
    public static int contains(Instance instance) {
        int left = 0, right = instanceList.size() - 1;
        String instanceEmail = instance.getEmail();
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String midEmail = instanceList.get(mid).getEmail();
            if (instanceEmail.compareTo(midEmail) > 0) {
                left = mid + 1;
            } else if (instanceEmail.compareTo(midEmail) < 0) {
                right = mid - 1;
            } else return mid;
        }
        return left;
    }

    public static void store() throws IOException {
            mapper.writeValue(address, instanceList);
    }

    public static void load() throws IOException {
        instanceList = mapper.readValue(address, listType);
    }
    
}
