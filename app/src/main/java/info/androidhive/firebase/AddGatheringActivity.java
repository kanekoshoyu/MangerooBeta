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
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AddGatheringActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    Calendar calendarDate = Calendar.getInstance();
    Calendar calendarStartTime = Calendar.getInstance();
    Calendar calendarEndTime = Calendar.getInstance();
    private EditText TitleRef, PlaceRef;
    private TextView GatheringDate, GatheringStartTime, GatheringEndTime;
    private Button Organise;

    private String mTitle, mDate, mStartTime, mEndTime, mPlace;

    SimpleDateFormat Dformat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat Tformat = new SimpleDateFormat("HH:mm");

    private void addNewGathering(String HolderID, String Title, String Date, String StartTime, String EndTime, String Place) {
        Gathering gathering = new Gathering(HolderID, Title, Date, StartTime, EndTime, Place);
        String key = mDatabase.child("gathering").push().getKey();
        mDatabase.child("gathering").child(key).setValue(gathering);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_add_gathering);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String UID = auth.getCurrentUser().getUid();
        Organise = (Button) findViewById(R.id.OrganiseButton);
        TitleRef = (EditText) findViewById(R.id.GatheringTitle);
        PlaceRef = (EditText) findViewById(R.id.GatheringPlace);
        TitleRef.requestFocus();

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


        Organise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle = TitleRef.getText().toString().trim();
                mDate = GatheringDate.getText().toString().trim();
                mStartTime = GatheringStartTime.getText().toString().trim();
                mEndTime = GatheringEndTime.getText().toString().trim();
                mPlace = PlaceRef.getText().toString().trim();
                addNewGathering(UID, mTitle, mDate, mStartTime, mEndTime, mPlace);
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
}