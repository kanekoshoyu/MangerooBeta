package info.androidhive.firebase;


/**
 * Created by choiwaiyiu on 11/3/2017.
 */

public class Gathering {
    private String HolderID;
    private String Title;
    private String Date;
    private String StartTime;
    private String EndTime;
    private String Place;

    public Gathering(){
        this.HolderID = "";
        this.Title = "";
        this.Date = "";
        this.StartTime = "";
        this.EndTime = "";
        this.Place = "";
    }

    public Gathering(String HolderID, String Title, String Date, String StartTime, String EndTime, String Place){
        this.HolderID = HolderID;
        this.Title = Title;
        this.Date = Date;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.Place = Place;
    }

    public String getHolderID(){return HolderID;}
    public String getTitle(){return Title;}
    public String getDate(){return Date;}
    public String getStartTime(){return StartTime;}
    public String getEndTime(){return EndTime;}
    public String getPlace(){return Place;}
}