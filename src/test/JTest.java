
import org.junit.Test;
import model.*;

public class JTest {
    // This part we mainly test the basic functions of the controller level.
    @Test
    public void Test_getUser(){
        System.out.println("Test start");
        LoginController l = new LoginController();

        assert l.getUser() != null ? true : false;
        assert true;

        System.out.println("Test end");
    }

    @Test
    public void Test_sendEmail(){
        System.out.println("Test start");
        RegisterController r = new RegisterController();
        r.sendTest("1115831817@qq.com");

        assert r != null ? true : false;
        assert true;

        System.out.println("Test end");
    }

    @Test
    public void Test_hasLogging(){
        System.out.println("Test start");
        LoginController l = new LoginController();

        assert l.hasLogging() == false ? true : false;
        assert true;

        System.out.println("Test end");
    }

    // This part we mainly test the basic functions of the model level.
    @Test
    public void Test_coach(){
        System.out.println("Test start");
        Coach c = new Coach();
        c.setId("12345");
        c.setHeight("123");

        assert c.getHeight() != null ? true : false;
        assert true;

        System.out.println("Test end");
    }

    @Test
    public void Test_course(){
        System.out.println("Test start");
        Course c = new Course();
        c.setId("12345");
        c.setLocation("C://user");

        assert c.getLocation() != null ? true : false;
        assert true;

        System.out.println("Test end");
    }

    @Test
    public void Test_customer(){
        System.out.println("Test start");
        Customer c = new Customer();
        c.setId("12345");
        c.setName("abcde");

        assert c.getId() != null ? true : false;
        assert true;

        System.out.println("Test end");
    }
}