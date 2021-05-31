package util;

import model.Instance;
import model.User;

import java.util.HashMap;

public class Databases {
    private static final HashMap<Class<?>,UserDatabase<?>> DATABASE_BOX =new HashMap<>();

    public static <T extends User> UserDatabase<T> getDatabase(Class<T> clazz){
        UserDatabase userDatabase = DATABASE_BOX.get(clazz);
        if (userDatabase == null){
            userDatabase = new UserDatabase<>(clazz);
            DATABASE_BOX.put(clazz,userDatabase);
        }
        return userDatabase;
    }
    
    public static void store(){
        DATABASE_BOX.values().forEach(UserDatabase::store);
    }
}
