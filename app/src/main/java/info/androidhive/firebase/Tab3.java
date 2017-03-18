package info.androidhive.firebase;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.fcm.MyFirebaseMessagingService;

/**
 * Created by shoyu on 16/2/2017.
 */

public class Tab3 extends Fragment {
    private DatabaseReference mUsers;
    private DatabaseReference mInvitationRef;
    private FirebaseAuth auth;
    private ArrayList<String> myInvitations = new ArrayList<>();

    private List<Invitation> InvitationArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the View first to facilitate findViewById
        View rootView = inflater.inflate(R.layout.tab3, container, false);
        mUsers = FirebaseDatabase.getInstance().getReference().child("users");

        ListView invitationListView = (ListView) rootView.findViewById(R.id.invitationList_view);
        final InvitationAdapter adapter = new InvitationAdapter(getActivity(), InvitationArrayList);
        invitationListView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        final String UID = auth.getCurrentUser().getUid();
        mInvitationRef = mUsers.child(UID).child("invitations");

        mUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myInvitations.clear();
                InvitationArrayList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.child(UID).child("invitations").getChildren()) {
                    String InvitationUID = postSnapshot.getKey();
                    String InvitationTime = postSnapshot.getValue(String.class);
                    DataSnapshot tempSnapshot = dataSnapshot.child(InvitationUID);
                    String InvitationUserName = tempSnapshot.getValue(User.class).getUsername();

                    InvitationArrayList.add(new Invitation(InvitationUID, InvitationUserName, InvitationTime));

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        return rootView;
    }
}