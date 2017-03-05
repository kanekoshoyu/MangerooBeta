package info.androidhive.firebase;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDataActivity extends AppCompatActivity {
    private String UID;
    private DatabaseReference mDatabase;
    private DatabaseReference mUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        Bundle extras = getIntent().getExtras();
        final String name;
        final String[] status = {"not friend"};

        final TextView tv_name = (TextView) findViewById(R.id.tv_name);
        final TextView tv_email = (TextView) findViewById(R.id.tv_email);
        final TextView tv_phone = (TextView) findViewById(R.id.tv_phone);
        final TextView tv_free = (TextView) findViewById(R.id.tv_free);
        //if(extras !=null) {

            name = extras.getString("NAME");
            UID = extras.getString("UID");
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mUserRef = mDatabase.child("users").child(UID);
            tv_name.setText(name);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_free.setText(dataSnapshot.getValue(User.class).getFree());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                tv_email.setText(u.getEmail());
                tv_phone.setText(u.getPhoneNumber());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //status[0] = "not friend";
                for (DataSnapshot postSnapshot: dataSnapshot.child("friends").getChildren()) {
                    if(postSnapshot.getValue(String.class).equals(UID)){
                        status[0] = "friend";
                        Button btn_Add = (Button)findViewById(R.id.btn_add);
                        btn_Add.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        final Button btn_Add = (Button)findViewById(R.id.btn_add);
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Invitation has been sent", Toast.LENGTH_LONG);
                String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("users").child(myUID);
                //myRef.child("friends").child(UID).setValue("");

                myRef.child("friends").push().setValue(UID);
                Toast.makeText(getApplicationContext(),name + "is now your friend", Toast.LENGTH_LONG).show();

                btn_Add.setVisibility(View.GONE);
                status[0] = "friend";
            }
        });

        Button btn_Invite = (Button)findViewById(R.id.btn_invite);
        btn_Invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //}
    }
}
