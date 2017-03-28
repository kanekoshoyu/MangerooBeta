package info.androidhive.firebase;

import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.PendingIntent.getActivity;

public class DetailUserActivity extends AppCompatActivity {
    private String UID;
    private DatabaseReference mDatabase;
    private DatabaseReference mUserRef;
    private FirebaseAuth auth;
    private String myUID;
    private String friendKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.detail_user);
        Bundle extras = getIntent().getExtras();
        final String name;

        auth = FirebaseAuth.getInstance();

        final TextView tv_name = (TextView) findViewById(R.id.tv_name);
        final TextView tv_email = (TextView) findViewById(R.id.tv_email);
        final TextView tv_phone = (TextView) findViewById(R.id.tv_phone);
        final TextView tv_free = (TextView) findViewById(R.id.tv_free);
        final TextView tv_lastInvitation = (TextView) findViewById(R.id.tv_lastInvitation);
        final TextView tv_unfriend = (TextView) findViewById(R.id.tv_unfriend);
        final Button btn_Invite = (Button) findViewById(R.id.btn_invite);
        final Button btn_Add = (Button) findViewById(R.id.btn_add);

        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        name = extras.getString("NAME");
        UID = extras.getString("UID");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserRef = mDatabase.child("users").child(UID);
        tv_name.setText(name);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_free.setText(dataSnapshot.getValue(User.class).getFree());
                //Toast.makeText(DetailUserActivity.this, dataSnapshot.child("invitations").child(myUID).getValue(String.class) , Toast.LENGTH_SHORT).show();
                if(dataSnapshot.child("invitations").child(myUID).exists())
                    tv_lastInvitation.setText("Last Invitation: " + dataSnapshot.child("invitations").child(myUID).getValue(String.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


        mUserRef.child("profileModified").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String date = dataSnapshot.getValue(String.class);
                StorageReference pathReference = null;
                if(date != null) {
                    String path = "images/" + UID + ".jpg";
                    pathReference = FirebaseStorage.getInstance().getReference().child(path);
                }else{
                    date = "0";
                }

                if(pathReference == null)pathReference = FirebaseStorage.getInstance().getReference().child("images/default.jpg");

                Glide.with(DetailUserActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(pathReference)
                        .centerCrop()
                        .override(150, 150)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        //.skipMemoryCache(true)
                        .signature(new StringSignature(date))
                        .into((ImageView) findViewById(R.id.iv_icon));
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

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

        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.child("friends").getChildren()) {
                    if(postSnapshot.getValue(String.class).equals(UID))
                    {
                        btn_Add.setVisibility(View.GONE);
                        tv_unfriend.setVisibility(View.VISIBLE);
                        friendKey = postSnapshot.getKey();
                        return;
                    }
                }
                btn_Add.setVisibility(View.VISIBLE);
                tv_unfriend.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Invitation has been sent", Toast.LENGTH_LONG);
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("users").child(myUID);
                //myRef.child("friends").child(UID).setValue("");
                myRef.child("friends").push().setValue(UID);
                Toast.makeText(getApplicationContext(),name + "is now your friend", Toast.LENGTH_LONG).show();

                btn_Add.setVisibility(View.GONE);
            }
        });

        btn_Invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                String strDate = sdf.format(now);
                mDatabase.child("users").child(UID).child("invitations").child(myUID).setValue(strDate);
                mDatabase.child("users").child(UID).child("notifications").child("invitations").child(myUID).child("time").setValue(strDate);
                mDatabase.child("users").child(UID).child("notifications").child("invitations").child(myUID).child("username").setValue(Me.myName);

                Snackbar.make(v, "You have sent an invitation!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

        tv_unfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailUserActivity.this, "unfriend " + name, Toast.LENGTH_SHORT).show();
                mDatabase.child("users").child(myUID).child("friends").child(friendKey).removeValue();
            }
        });
    }
}