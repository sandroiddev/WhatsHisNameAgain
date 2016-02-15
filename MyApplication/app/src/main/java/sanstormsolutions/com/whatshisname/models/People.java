package sanstormsolutions.com.whatshisname.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jsandersii on 2/11/16.
 * The primary use for this class is to act as the model for the 'Person' Node on Firebase.
 */
public class People implements Parcelable{

    //Vars
    private String firstName = null; //First Name of the person
    private String lastName = null; // Last Name of the person
    private String birthday = null; // DOB of the person
    private String zipCode; // Home Zip Code of the person
    private String id = null; // The unique ID for this person

    //Default Constructor for firebase object mapping.
    public People(){

    }
    protected People(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        birthday = in.readString();
        zipCode = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(birthday);
        dest.writeString(zipCode);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<People> CREATOR = new Creator<People>() {
        @Override
        public People createFromParcel(Parcel in) {
            return new People(in);
        }

        @Override
        public People[] newArray(int size) {
            return new People[size];
        }
    };

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

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}
