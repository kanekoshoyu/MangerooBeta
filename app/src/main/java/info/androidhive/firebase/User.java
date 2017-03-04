package info.androidhive.firebase;

/**
 * Created by choiwaiyiu on 4/3/2017.
 */
public class User {

    public String username;
    public String email;
    public String free;
    public String phoneNumber;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.free = "free";
        this.phoneNumber = phoneNumber;
    }
    public String toString(){
        return username;
    }
}