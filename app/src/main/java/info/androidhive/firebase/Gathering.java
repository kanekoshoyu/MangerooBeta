package info.androidhive.firebase;


import java.util.ArrayList;
import java.util.List;

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
    private List<String> participantIDs = new ArrayList<>();

    public Gathering(){
        this.HolderID = "";
        this.Title = "";
        this.Date = "";
        this.StartTime = "";
        this.EndTime = "";
        this.Place = "";
    }

    public Gathering(String HolderID, String Title, String Date, String StartTime, String EndTime, String Place, List<String> ParticipantIDs){
        this.HolderID = HolderID;
        this.Title = Title;
        this.Date = Date;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.Place = Place;
        this.participantIDs = participantIDs;
    }

    public String getHolderID(){return HolderID;}
    public String getTitle(){return Title;}
    public String getDate(){return Date;}
    public String getStartTime(){return StartTime;}
    public String getEndTime(){return EndTime;}
    public String getPlace(){return Place;}
    public List<String> getParticipantIDs(){return participantIDs;}
}