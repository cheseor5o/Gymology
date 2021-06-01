//import model.Coach;
//import model.Customer;
//import model.User;
//import org.junit.Test;
//
//import java.util.*;
//
//public class TestCustomerDB {
//    @Test
//    public void testReadAndAdd() throws ClassNotFoundException {
//        User user = new User();
//        user.setPassword("123123");
//        user.setEmail(System.currentTimeMillis()+"");
//        user.setIdentity(User.Identity.Customer);
//        Customer customer = new Customer(user);
//        customer.setName("No.2 Coach");
//        customer.setPhone("15510412588");
//        customer.setGender("M");
//        customer.setHeight("190");
//        customer.setWeight("70kg");
//        List<Map<String,String>> list = new ArrayList<>();
//        Map<String,String> map = new HashMap<>();
//        map.put("1","19:00");
//        map.put("2","20:00");
//        list.add(map);
//        customer.setCourseHistory(list);
//        CustomerDatabase.add(customer);
//        CustomerDatabase.show();
//        CustomerDatabase.store();
//    }
//
//}
