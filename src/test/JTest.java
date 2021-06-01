import controller.LoginController;
import controller.RegisterController;
import model.Coach;
import model.Course;
import model.Customer;
import org.junit.Assert;
import org.junit.Test;

public class JTest {
    // This part we mainly test the basic functions of the controller level.
    @Test
    public void Test_Email(){
        boolean v1 = testEmail(" 123");
        Assert.assertTrue(v1);
        boolean v2 = testEmail("   ");
        Assert.assertFalse(v2);
        boolean v3 = testEmail("asdfkj1234");
        Assert.assertTrue(v3);
    }

    private boolean testEmail(String s){
        return RegisterController.validField(s);
    }

    @Test
    public void Test_Password(){
        boolean v1 = testPassword("jkwjkw842");
        Assert.assertTrue(v1);
        boolean v2 = testPassword("$wsf234");
        Assert.assertTrue(v2    );
        boolean v3 = testPassword("asdf123asdf");
        Assert.assertTrue(v3);
    }

    private boolean testPassword(String s){
        return RegisterController.validField(s);
    }

    @Test
    public void Test_Phone(){
        boolean v1 = testPhone("12312341312");
        Assert.assertTrue(v1);
        boolean v2 = testPhone("awfsda");
        Assert.assertTrue(v2);
        boolean v3 = testPhone("123212123");
        Assert.assertTrue(v3);
    }

    private boolean testPhone(String s){
        return RegisterController.validField(s);
    }


    @Test
    public void Test_hasLogging(){
        LoginController l = new LoginController();
        Assert.assertFalse(l.hasLogging());
        System.out.println("Success");
    }

    // This part we mainly test the basic functions of the model level.
    @Test
    public void Test_coach(){
        Coach c = new Coach();
        c.setId("12345");
        c.setHeight("123");
        Assert.assertNotNull(c.getId());
        System.out.println("Success");
    }

    @Test
    public void Test_course(){
        Course c = new Course();
        c.setId("12345");
        c.setLocation("C://user");
        Assert.assertNotNull(c.getId());
        System.out.println("Success");
    }

    @Test
    public void Test_customer(){
        Customer c = new Customer();
        c.setId("12345");
        c.setName("abcde");
        Assert.assertNotNull(c.getId());
        System.out.println("Success");
    }
}