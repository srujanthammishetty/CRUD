import java.io.Serializable;

/**
 * Created by srujant on 22/6/16.
 */
public class Employee {
    int id;
    String firstName;
    String lastName;
    String emailId;
    int age;


    public Employee() {
    }

    Employee(int id, String firstName, String lastName, String emailId, int age){
        this.firstName=firstName;
        this.lastName=lastName;
        this.age=age;
        this.emailId=emailId;
        this.id=id;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

}
