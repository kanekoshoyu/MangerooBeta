package info.androidhive.firebase;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFriend extends AppCompatActivity {


    private DatabaseReference mDatabase;
    private DatabaseReference mFreeRef;
    private FirebaseAuth auth;
    private ArrayList<String> userNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_search_friend);

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        String query;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ListView friendListView = (ListView) findViewById(R.id.friendSearch_List);
        //creates the adapter for the ListView, and show the ListView
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNames);
        friendListView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        final String UID = auth.getCurrentUser().getUid();

        mFreeRef = mDatabase.child("users").child(UID).child("free");

        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userNames.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if(postSnapshot.getValue(User.class).getFree().equals("free")){
                        userNames.add(postSnapshot.getValue(User.class).getUsername() + "");
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
