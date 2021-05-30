package model;

public class User {
    public enum Identity {
        Customer, Coach, Manager
    }
    private String email; //用邮箱登录,后面也要用到邮箱相当于一个id
    private String password;
    private Identity identity;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", identity=" + identity +
                '}';
    }
}
