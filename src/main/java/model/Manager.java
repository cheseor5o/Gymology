package model;

public class Manager extends User{

    public static boolean isManager(Instance instance){
        return instance.getIdentity().equals(Instance.Identity.Manager);
    }
    
    private double allIncome;

    public Manager() {
    }

    public double getAllIncome() {
        return allIncome;
    }

    public void setAllIncome(double allIncome) {
        this.allIncome = allIncome;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id='" + super.getId() + '\'' +
                ", name='" + super.getName() + '\'' +
                ", gender='" + super.getGender() + '\'' +
                ", phone='" + super.getPhone() + '\'' +
                ", allIncome=" + allIncome +
                '}';
    }
}
