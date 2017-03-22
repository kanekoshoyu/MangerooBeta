package info.androidhive.firebase;

import java.util.ArrayList;

/**
 * Created by choiwaiyiu on 4/3/2017.
 */
public class User {

    private String username;
    private String email;
    private String free;
    private String phoneNumber;
    private String token;
    private String uid;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(String uid, String username, String email, String phoneNumber) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.free = "free";
        this.phoneNumber = phoneNumber;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getFree(){
        return free;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getToken(){
        return token;
    }

    public String toString(){
        return username;
    }

    public String getUid(){return uid;}
}