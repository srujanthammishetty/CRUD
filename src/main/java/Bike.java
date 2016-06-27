import java.io.Serializable;

/**
 * Created by srujant on 23/6/16.
 */
public class Bike {
    String name;
    int  mileage;
    int tankCapacity;
    int price;
    
    Bike(String name,int mileage,int tankCapacity,int price){
        this.name=name;
        this.mileage=mileage;
        this.tankCapacity=tankCapacity;
        this.price=price;
    }
    public Bike(){}


    public int getCapacity() {
        return tankCapacity;
    }

    public void setCapacity(int tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    public int getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(int tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
