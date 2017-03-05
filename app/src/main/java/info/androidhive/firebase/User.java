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
    private ArrayList<String> friendList;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(String username, String email, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.free = "free";
        this.phoneNumber = phoneNumber;
        this.friendList = new ArrayList<String>();
        friendList.add("Hi, this is first");
        friendList.add("Yo, this is second");
        friendList.add("Wow, this is never ends");
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

    public String toString(){
        return username;
    }
}