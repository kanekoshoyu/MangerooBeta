package info.androidhive.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.R;
import info.androidhive.firebase.adapter.AvailableFriendAdapter;
import info.androidhive.firebase.adapter.ParticipantSearchListRowAdapter;

import static info.androidhive.firebase.R.id.AddParticipantButton;
import static info.androidhive.firebase.R.id.container;

public class SearchParticipantActivity extends AppCompatActivity implements DataTransferInterface {

    private DatabaseReference mUsers;
    private FirebaseAuth auth;

    private List<String> myFriends = new ArrayList<>();
    private List<String> userIds = new ArrayList<>();
    private List<User> FriendArrayList = new ArrayList<>();

    private String mTitle, mDate, mStartTime, mEndTime, mPlace;
    private ArrayList<String> participantIDs;
    private ArrayList<String> checked;

    private Button AddParticipantButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_participant);



        participantIDs = getIntent().getStringArrayListExtra("participantIDs");
        checked = getIntent().getStringArrayListExtra("checked");
        //Toast.makeText(this, checked.size()+"", Toast.LENGTH_SHORT).show();
        /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_participant_search_list, menu);
            return true;
        }
        */

        mUsers = FirebaseDatabase.getInstance().getReference().child("users");
        auth = FirebaseAuth.getInstance();
        final String UID = auth.getCurrentUser().getUid();

        AddParticipantButton = (Button) findViewById(R.id.AddParticipantButton);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.participantSearchListProgressBar);
        ListView participantList = (ListView) findViewById(R.id.participantSearchList_view);
        final ParticipantSearchListRowAdapter adapter = new ParticipantSearchListRowAdapter(this, this, FriendArrayList, userIds, participantIDs, checked);
        participantList.setAdapter(adapter);


        mUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                userIds.clear();
                myFriends.clear();
                FriendArrayList.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.child(UID).child("friends").getChildren()) {
                    myFriends.add(postSnapshot.getValue(String.class));
                }

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //Updates Friend List here
                    if(myFriends.contains(postSnapshot.getKey())){
                        userIds.add(postSnapshot.getKey());
                        FriendArrayList.add(postSnapshot.getValue(User.class));

                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        AddParticipantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("participantIDs",(ArrayList<String>) participantIDs);
                intent.putStringArrayListExtra("checked", (ArrayList<String>) checked);
                setResult(RESULT_OK, intent);
                finish();
                //startActivity(intent);
            }
        });
    }

    @Override
    public void setValues(ArrayList<String> al) {
        checked = al;
    }
}
