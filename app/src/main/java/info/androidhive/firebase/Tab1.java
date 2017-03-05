package info.androidhive.firebase;

import java.util.*;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by shoyu on 16/2/2017.
 */

public class Tab1 extends Fragment {

    private DatabaseReference mDatabase;
    private DatabaseReference mFreeRef;
    private FirebaseAuth auth;
    private ArrayList<String> userNames = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);
        final Switch mSwitchFree = (Switch) rootView.findViewById(R.id.switchFree);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ListView friendListView = (ListView) rootView.findViewById(R.id.friendList_view);
        //creates the adapter for the ListView, and show the ListView
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, userNames);
        friendListView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        final String UID = auth.getCurrentUser().getUid();

        mFreeRef = mDatabase.child("users").child(UID).child("free");

        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView)view).getText().toString();
                String status = "free friend";
                Intent intent = new Intent(getActivity(), UserDataActivity.class);
                intent.putExtra("NAME", item);
                intent.putExtra("STATUS", status);
                startActivity(intent);

            }
        });

        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userNames.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //Updates Friend List here
                    if(postSnapshot.getKey().equals(UID)){
                        if(postSnapshot.getValue(User.class).getFree().equals("free")){
                            mSwitchFree.setChecked(true);
                        }
                    }else if(postSnapshot.getValue(User.class).getFree().equals("free")){
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



        mFreeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSwitchFree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //Switch is pressed
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mFreeRef.setValue("free");

                else
                    mFreeRef.setValue("not free");
            }
        });

        return rootView;


    }
}
