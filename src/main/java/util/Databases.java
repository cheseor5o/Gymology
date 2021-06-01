package util;

import model.User;

import java.util.HashMap;

/**
 * A box of database
 */
public class Databases {
    private static final HashMap<Class<?>,UserDatabase<?>> DATABASE_BOX =new HashMap<>();

    /**
     * Whenever the system needs to access a database (of Customer, Coach or Manage),
     * it check if it is exists
     * If it is not exists, it create a new one then save it to the box
     * @param clazz A class of a specific user
     * @param <T> A User type
     * @return A data base
     */
    public static <T extends User> UserDatabase<T> getDatabase(Class<T> clazz){
        UserDatabase userDatabase = DATABASE_BOX.get(clazz);
        if (userDatabase == null){
            userDatabase = new UserDatabase<>(clazz);
            DATABASE_BOX.put(clazz,userDatabase);
        }
        return userDatabase;
    }

    /**
     * Save the change into local JSON file
     */
    public static void store(){
        DATABASE_BOX.values().forEach(UserDatabase::store);
    }
}
