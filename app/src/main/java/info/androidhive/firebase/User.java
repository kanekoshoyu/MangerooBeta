package info.androidhive.firebase;

/**
 * Created by choiwaiyiu on 4/3/2017.
 */
public class User {

    public String username;
    public String email;
    public String free;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.free = "free";
    }

}