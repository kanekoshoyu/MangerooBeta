package info.androidhive.firebase;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by shoyu on 16/2/2017.
 */

public class Tab1 extends Fragment {

    private DatabaseReference mDatabase;
    private DatabaseReference mFreeRef;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        final String UID = auth.getCurrentUser().getUid();

        mFreeRef = mDatabase.child("users").child(UID).child("free");


        Switch mSwitchFree = (Switch) rootView.findViewById(R.id.switchFree);

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
