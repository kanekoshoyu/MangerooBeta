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

/**
 * Created by shoyu on 16/2/2017.
 */

public class Tab3 extends Fragment{

    private DatabaseReference mDatabase;
    private DatabaseReference mUserRef;
    private FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the View first to facilitate findViewById
        View rootView = inflater.inflate(R.layout.tab3, container, false);
        Button signOut = (Button) rootView.findViewById(R.id.sign_out);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);

        final TextView tv2 = (TextView) rootView.findViewById(R.id.tv_email);
        final TextView tv1 = (TextView) rootView.findViewById(R.id.tv_phone);
        final TextView tv0 = (TextView) rootView.findViewById(R.id.tv_name);

        auth = FirebaseAuth.getInstance();
        final String UID = auth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserRef = mDatabase.child("users").child(UID);

        mUserRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] info=new String[3];

                info[0] = (String) dataSnapshot.child("username").getValue();
                info[1] = (String) dataSnapshot.child("phoneNumber").getValue();
                info[2] = (String) dataSnapshot.child("email").getValue();

                tv0.setText(info[0]);
                tv1.setText(info[1]);
                tv2.setText(info[2]);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
    });


        //creates the adapter for the ListView, and show the ListView
        /*

        tv0.setText(info[0]);
        tv1.setText(info[1]);
        tv2.setText(info[2]);
        */


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        return rootView;

    }
}
