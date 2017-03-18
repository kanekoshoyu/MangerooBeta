package info.androidhive.firebase;

/**
 * Created by choiwaiyiu on 18/3/2017.
 */

public class Invitation {
    private String UserID;
    private String UserName;
    private String DateTime;


    public Invitation(String UserID, String UserName, String DateTime){
        this.UserID = UserID;
        this.UserName = UserName;
        this.DateTime = DateTime;
    }

    public String getUserID(){return UserID;}
    public String getUserName(){return UserName;}
    public String getDateTime(){return DateTime;}
}
