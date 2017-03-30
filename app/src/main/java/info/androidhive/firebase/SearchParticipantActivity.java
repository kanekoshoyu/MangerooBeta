package info.androidhive.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

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

import static info.androidhive.firebase.R.id.container;

public class SearchParticipantActivity extends AppCompatActivity {

    private DatabaseReference mUsers;
    private FirebaseAuth auth;

    private List<String> myFriends = new ArrayList<>();
    private List<String> userIds = new ArrayList<>();
    private List<User> FriendArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_participant);


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

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.participantSearchListProgressBar);
        ListView participantList = (ListView) findViewById(R.id.participantSearchList_view);
        final ParticipantSearchListRowAdapter adapter = new ParticipantSearchListRowAdapter(this, FriendArrayList, userIds);
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

    }
}
