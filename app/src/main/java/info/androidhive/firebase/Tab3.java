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
    /*String[] info={
            "Astro",
            "Bender",
            "Cupcake",
            "Donut",
            "Eclair",
            "Donut",
            "Eclair",
            "Froyo",
            "Gingerbread",
            "Honeycomb",
            "Kitkat",
            "Lollipop",
            "Marshmallow",
            "Nougat"};*/

    String[] info={"","",""};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        final String UID = auth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserRef = mDatabase.child("users").child(UID);

        mUserRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                info[0] = (String) dataSnapshot.child("username").getValue();
                info[1] = (String) dataSnapshot.child("phoneNumber").getValue();
                info[2] = (String) dataSnapshot.child("email").getValue();
                // do your stuff here with value

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

        //Inflate the View first to facilitate findViewById
        View rootView = inflater.inflate(R.layout.tab3, container, false);
        Button signOut = (Button) rootView.findViewById(R.id.sign_out);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        //creates the adapter for the ListView, and show the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, info);
        listView.setAdapter(adapter);

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
