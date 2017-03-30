package info.androidhive.firebase;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailGatheringActivity extends AppCompatActivity {
    private String GID;
    private DatabaseReference mDatabase;
    private DatabaseReference mGathering;
    private String myUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.detail_gathering);
        GID = getIntent().getStringExtra("GID");
        //Toast.makeText(getApplicationContext(), GID,Toast.LENGTH_SHORT).show();

        final TextView tv_organiser = (TextView) findViewById(R.id.tv_gathering_organiser);
        final TextView tv_interested = (TextView) findViewById(R.id.tv_gathering_participant_interested);
        final TextView tv_time = (TextView) findViewById(R.id.tv_gathering_time);
        final TextView tv_title = (TextView) findViewById(R.id.tv_gathering_title);
        final TextView tv_going = (TextView) findViewById(R.id.tv_gathering_participant_going);
        final TextView tv_venue = (TextView) findViewById(R.id.tv_gathering_venue);
        final TextView tv_date = (TextView) findViewById(R.id.tv_gathering_date);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mGathering = mDatabase.child("gathering").child(GID);

        mGathering.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_title.setText(dataSnapshot.child("title").getValue().toString());
                tv_venue.setText(dataSnapshot.child("place").getValue().toString());
                tv_date.setText(dataSnapshot.child("date").getValue().toString());

                //tv_free.setText(dataSnapshot.getValue(User.class).getFree());
                //Toast.makeText(DetailUserActivity.this, dataSnapshot.child("invitations").child(myUID).getValue(String.class) , Toast.LENGTH_SHORT).show();

                DatabaseReference mUserRef = mDatabase.child("users").child(dataSnapshot.child("holderID").getValue().toString());
                mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tv_organiser.setText(dataSnapshot.child("username").getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }


}