package sanstormsolutions.com.whatshisname.models;

/**
 * Created by jsandersii on 2/11/16.
 * The primary use for this class is to act as the model for the 'Person' Node on Firebase.
 */
public class People {

    //Vars
    private String firstName = null; //First Name of the person
    private String lastName = null; // Last Name of the person
    private String birthday = null; // DOB of the person
    private String zipCode; // Home Zip Code of the person

    //Default Constructor for firebase object mapping.
    public People(){

    }


    // Getters & Setters

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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
