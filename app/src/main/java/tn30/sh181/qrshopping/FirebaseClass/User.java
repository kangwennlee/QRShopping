package tn30.sh181.qrshopping.FirebaseClass;

import java.util.ArrayList;
import java.util.List;

public class User {
    String uid;
    String name;
    String email;
    double balance;
    String profilePic;
    String phoneNo;
    //User user;
    public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.balance = 0;
        this.profilePic = "";
        this.phoneNo = "";
    }

    public User() {
        this.uid = "";
        this.name = "";
        this.email = "";
        this.balance = 0;
        this.profilePic = "";
        this.phoneNo = "";
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setProfilePic(String profilePic){
        this.profilePic  = profilePic;

    }
    public void setUid(String uid){
        this.uid = uid;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setBalance(double balance){
        this.balance = balance;
    }

    public String getUid(){return  this.uid;}

    public String getProfilePic(){
        return this.profilePic;
    }
    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public double getBalance(){
        return this.balance;
    }
}
