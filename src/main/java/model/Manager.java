package model;

public class Manager extends User{
    
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
