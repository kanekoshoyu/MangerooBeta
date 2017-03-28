package info.androidhive.firebase;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import info.androidhive.firebase.adapter.NewFriendAdapter;

public class SearchFriendActivity extends AppCompatActivity {


    private DatabaseReference mDatabase;
    private DatabaseReference mFreeRef;
    private FirebaseAuth auth;
    private ArrayList<String> userNames = new ArrayList<>();
    private ArrayList<String> userIds = new ArrayList<>();
    private String myUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_search_friend);

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNames);
        ListView friendListView = (ListView) findViewById(R.id.friendSearch_List);
        NewFriendAdapter useradapter;

        friendListView.setAdapter(adapter);


        auth = FirebaseAuth.getInstance();
        final String UID = auth.getCurrentUser().getUid();
        mFreeRef = mDatabase.child("users").child(UID).child("free");


        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView)view).getText().toString();
                String status = "not friend";
                Intent intent = new Intent(SearchFriendActivity.this, DetailUserActivity.class);
                intent.putExtra("NAME", item);
                intent.putExtra("STATUS", status);
                intent.putExtra("UID", userIds.get(position));
                startActivity(intent);


            }
        });


        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userNames.clear();
                userIds.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        if(!postSnapshot.getKey().equals(myUID)) {
                            userNames.add(postSnapshot.getValue(User.class).getUsername() + "");
                            adapter.notifyDataSetChanged();
                            userIds.add(postSnapshot.getKey());
                        }
                }
                progressBar.setVisibility(View.GONE);
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
