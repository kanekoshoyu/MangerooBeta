package info.androidhive.firebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserDataSetting extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mUserRef;
    private StorageReference mStorageRef;
    private FirebaseAuth auth;
    private ImageView iv;

    public UserDataSetting() {
    }

    private void downloadImg(ImageView iv){
if(iv!=null) {

// Create a reference with an initial file path and name
    StorageReference pathReference = mStorageRef.child("images/rivers.jpg");

    Glide.with(this /* context */)
            .using(new FirebaseImageLoader())
            .load(pathReference)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(iv);

}
}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_userdata);
        Button signOut = (Button) findViewById(R.id.sign_out);

        final TextView tv2 = (TextView) findViewById(R.id.tv_email);
        final TextView tv1 = (TextView) findViewById(R.id.tv_phone);
        final TextView tv0 = (TextView) findViewById(R.id.tv_name);

        iv = (ImageView) findViewById(R.id.iv_icon);

        auth = FirebaseAuth.getInstance();
        final String UID = auth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
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


        downloadImg(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri img = data.getData();

            //String path = "images/" + auth.getCurrentUser().getUid() + ".jpg";
            StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

            riversRef.putFile(img)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            downloadImg(iv);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        }
    }
}