package info.androidhive.firebase;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
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

public class DetailGatheringActivity extends AppCompatActivity {
    private String GID;
    private DatabaseReference mDatabase;
    private DatabaseReference mGathering;
    private String myUID;

    private List<String> userIds = new ArrayList<>();
    private List<User> ParticipantArrayList = new ArrayList<>();

    private ListView goingList;

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
        //final TextView tv_going = (TextView) findViewById(R.id.tv_gathering_participant_going);
        goingList = (ListView) findViewById(R.id.gatheringGoingList);
        final TextView tv_venue = (TextView) findViewById(R.id.tv_gathering_venue);
        final TextView tv_date = (TextView) findViewById(R.id.tv_gathering_date);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mGathering = mDatabase.child("gathering").child(GID);

        final ParticipantAdapter adapter = new ParticipantAdapter(DetailGatheringActivity.this, ParticipantArrayList, userIds);
        goingList.setAdapter(adapter);

        mGathering.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_title.setText(dataSnapshot.child("title").getValue().toString());
                tv_venue.setText(dataSnapshot.child("place").getValue().toString());
                tv_date.setText(dataSnapshot.child("date").getValue().toString());

                userIds.clear();
                for(DataSnapshot postDataSnapshot : dataSnapshot.child("participants").getChildren()){
                    userIds.add(postDataSnapshot.getValue(String.class));
                }

                //tv_free.setText(dataSnapshot.getValue(User.class).getFree());
                //Toast.makeText(DetailGatheringActivity.this, dataSnapshot.child("invitations").child(myUID).getValue(String.class) , Toast.LENGTH_SHORT).show();

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

        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ParticipantArrayList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //Updates Friend List here
                    if(userIds.contains(postSnapshot.getKey())){
                        ParticipantArrayList.add(postSnapshot.getValue(User.class));

                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}