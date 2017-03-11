package info.androidhive.firebase;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddGatheringActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    private EditText TitleRef, StartTimeRef, EndTimeRef, PlaceRef;
    private Button Organise;

    private String Title, StartTime, EndTime, Place;

    private void addNewGathering(String HolderID, String Title, String StartTime, String EndTime, String Place) {
        Gathering gathering = new Gathering(HolderID, Title, StartTime, EndTime, Place);
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
        StartTimeRef = (EditText) findViewById(R.id.GatheringStartTime);
        EndTimeRef = (EditText) findViewById(R.id.GatheringEndTime);
        PlaceRef = (EditText) findViewById(R.id.GatheringPlace);

        Organise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Title = TitleRef.getText().toString().trim();
                StartTime = StartTimeRef.getText().toString().trim();
                EndTime = EndTimeRef.getText().toString().trim();
                Place = PlaceRef.getText().toString().trim();

                addNewGathering(UID+"", Title+"", StartTime+"", EndTime+"", Place+"");
                startActivity(new Intent(AddGatheringActivity.this, MainActivity.class));
            }

        });
    }
}