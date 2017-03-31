package info.androidhive.firebase;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.adapter.ParticipantAdapter;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AddGatheringActivity extends AppCompatActivity {
    private String trigger;
    ///////////////////////////
    private DatabaseReference mDatabase, mUsers;
    private FirebaseAuth auth;

    //private List<String> myParticipants = new ArrayList<>();
    private List<String> userIds = new ArrayList<>();
    private List<User> ParticipantArrayList = new ArrayList<>();

    Calendar calendarDate = Calendar.getInstance();
    Calendar calendarStartTime = Calendar.getInstance();
    Calendar calendarEndTime = Calendar.getInstance();
    private EditText TitleRef, PlaceRef;
    private TextView GatheringDate, GatheringStartTime, GatheringEndTime, InviteParticipantButton;
    private Button Organise;

    private String mTitle, mDate, mStartTime, mEndTime, mPlace;
    private List<String> participantIDs = new ArrayList<>();
    private List<String> checked = new ArrayList<>();
    ParticipantAdapter adapter;

    SimpleDateFormat Dformat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat Tformat = new SimpleDateFormat("HH:mm");

    private void addNewGathering(String HolderID, String Title, String Date, String StartTime, String EndTime, String Place, List<String> participantIDs) {
        Gathering gathering = new Gathering(HolderID, Title, Date, StartTime, EndTime, Place, participantIDs);
        String key = mDatabase.child("gathering").push().getKey();
        mDatabase.child("gathering").child(key).setValue(gathering);

        ///////////////////////////////////////////
        for(int i=0; i<participantIDs.size(); i++){
            String pKey = mDatabase.child("gathering").child(key).child("participants").push().getKey();
            mDatabase.child("gathering").child(key).child("participants").child(pKey).setValue(participantIDs.get(i));
        }
        //////////////////////////////////////////
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_add_gathering);


        auth = FirebaseAuth.getInstance();
        mUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String UID = auth.getCurrentUser().getUid();

        ListView participantList = (ListView) findViewById(R.id.participantList);

        adapter = new ParticipantAdapter(AddGatheringActivity.this, ParticipantArrayList, userIds);
        participantList.setAdapter(adapter);

        mUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                if(!dataSnapshot.hasChild("Trigger"))
                    mUsers.child("Trigger").setValue("0");
                    */
                ////////////////////////////////////////
                userIds.clear();
                //myParticipants.clear();
                ParticipantArrayList.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //Updates Friend List here
                    if(participantIDs.contains(postSnapshot.getKey())){
                        userIds.add(postSnapshot.getKey());
                        ParticipantArrayList.add(postSnapshot.getValue(User.class));

                        adapter.notifyDataSetChanged();
                    }
                    /*
                    if(postSnapshot.getKey().equals("Trigger"))
                        trigger = postSnapshot.getValue(String.class);
                        */
                }
                /*
                int tempTrigger = Integer.parseInt(trigger);
                tempTrigger = (tempTrigger+1)%2;
                trigger = String.valueOf(tempTrigger);
                */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        mDatabase.child("users").child(UID).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long friendCount = dataSnapshot.getChildrenCount();
                for(int i=0; i<friendCount; i++)
                    checked.add(new String("not checked"));
                //Toast.makeText(AddGatheringActivity.this, checked.size()+"HEY", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Organise = (Button) findViewById(R.id.OrganiseButton);
        TitleRef = (EditText) findViewById(R.id.GatheringTitle);
        PlaceRef = (EditText) findViewById(R.id.GatheringPlace);
        TitleRef.requestFocus();
        InviteParticipantButton = (TextView) findViewById(R.id.Button_InviteParticipant);

        GatheringDate = (TextView) findViewById(R.id.tv_gatheringdate);
        GatheringDate.setText(Dformat.format(calendarDate.getTime()));

        GatheringStartTime = (TextView) findViewById(R.id.tv_gatheringstarttime);
        GatheringEndTime = (TextView) findViewById(R.id.tv_gatheringendtime);

        GatheringDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });
        GatheringStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStartTime();
            }
        });
        GatheringEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEndTime();
            }
        });

        InviteParticipantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddGatheringActivity.this, SearchParticipantActivity.class);
                intent.putStringArrayListExtra("participantIDs",(ArrayList<String>) participantIDs);
                //Toast.makeText(AddGatheringActivity.this, checked.size()+"", Toast.LENGTH_SHORT).show();
                intent.putStringArrayListExtra("checked", (ArrayList<String>) checked);
                startActivityForResult(intent, 1);
            }
        });

        Organise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle = TitleRef.getText().toString().trim();
                mDate = GatheringDate.getText().toString().trim();
                mStartTime = GatheringStartTime.getText().toString().trim();
                mEndTime = GatheringEndTime.getText().toString().trim();
                mPlace = PlaceRef.getText().toString().trim();
                addNewGathering(UID, mTitle, mDate, mStartTime, mEndTime, mPlace, participantIDs);
                startActivity(new Intent(AddGatheringActivity.this, MainActivity.class));
            }
        });

    }

    private void updateDate(){
        new DatePickerDialog(this, d, calendarDate.get(Calendar.YEAR),calendarDate.get(Calendar.MONTH),calendarDate.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void updateStartTime(){
        new TimePickerDialog(this, (TimePickerDialog.OnTimeSetListener) tStart, calendarStartTime.get(Calendar.HOUR_OF_DAY), calendarStartTime.get(Calendar.MINUTE), true).show();
    }
    private void updateEndTime(){
        new TimePickerDialog(this, tEnd, calendarEndTime.get(Calendar.HOUR_OF_DAY), calendarEndTime.get(Calendar.MINUTE), true).show();
    }
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendarDate.set(Calendar.YEAR, year);
            calendarDate.set(Calendar.MONTH, monthOfYear);
            calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            GatheringDate.setText(Dformat.format(calendarDate.getTime()));
        }
    };
    TimePickerDialog.OnTimeSetListener tStart = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendarStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarStartTime.set(Calendar.MINUTE, minute);
            GatheringStartTime.setText(Tformat.format(calendarStartTime.getTime()));
            GatheringStartTime.setTextColor(Color.BLACK);
        }
    };
    TimePickerDialog.OnTimeSetListener tEnd = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendarEndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarEndTime.set(Calendar.MINUTE, minute);
            GatheringEndTime.setText(Tformat.format(calendarEndTime.getTime()));
            GatheringEndTime.setTextColor(Color.BLACK);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null&&requestCode==1){
            participantIDs = data.getStringArrayListExtra("participantIDs");
            checked = data.getStringArrayListExtra("checked");

            mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                if(!dataSnapshot.hasChild("Trigger"))
                    mUsers.child("Trigger").setValue("0");
                    */
                    ////////////////////////////////////////
                    userIds.clear();
                    //myParticipants.clear();
                    ParticipantArrayList.clear();

                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        //Updates Friend List here
                        if(participantIDs.contains(postSnapshot.getKey())){
                            userIds.add(postSnapshot.getKey());
                            ParticipantArrayList.add(postSnapshot.getValue(User.class));

                            adapter.notifyDataSetChanged();
                        }
                    /*
                    if(postSnapshot.getKey().equals("Trigger"))
                        trigger = postSnapshot.getValue(String.class);
                        */
                    }
                /*
                int tempTrigger = Integer.parseInt(trigger);
                tempTrigger = (tempTrigger+1)%2;
                trigger = String.valueOf(tempTrigger);
                */
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });

        }
        /*
        mUsers.child("Trigger").setValue(trigger);
        */
    }
}