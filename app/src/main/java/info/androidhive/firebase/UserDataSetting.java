package info.androidhive.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDataSetting extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mUserRef;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_userdata);
        Button signOut = (Button) findViewById(R.id.sign_out);

        final TextView tv2 = (TextView) findViewById(R.id.tv_email);
        final TextView tv1 = (TextView) findViewById(R.id.tv_phone);
        final TextView tv0 = (TextView) findViewById(R.id.tv_name);

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

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(UserDataSetting.this, LoginActivity.class));
                finish();
            }
        });
    }
}