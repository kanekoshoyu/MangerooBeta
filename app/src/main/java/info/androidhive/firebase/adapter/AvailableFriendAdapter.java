package info.androidhive.firebase.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import info.androidhive.firebase.ConfirmationActivity;
import info.androidhive.firebase.Me;
import info.androidhive.firebase.R;
import info.androidhive.firebase.User;

/**
 * Created by choiwaiyiu on 18/3/2017.
 */

public class AvailableFriendAdapter extends BaseAdapter {
    Context context;
    List<User> rowItems;
    List<String> invitations;
    List<String> ids;

    private DatabaseReference mDatabase;
    private DatabaseReference mUserRef;
    private DatabaseReference selfRef;
    private FirebaseAuth auth;
    private String myUID;



    public AvailableFriendAdapter(Context context, List<User> items, List<String> invitations, List<String> ids) {
        this.context = context;
        this.rowItems = items;
        this.invitations = invitations;
        this.ids = ids;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtName;
        Button inviteButton;
        Button acceptButton;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final AvailableFriendAdapter.ViewHolder holder;
        final User rowItem = (User) getItem(position);
        final String UID = ids.get(position);

        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserRef = mDatabase.child("users").child(UID);
        selfRef = mDatabase.child("users").child(myUID);

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_availablefriend, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.listFriendName);
            holder.imageView = (ImageView) convertView.findViewById(R.id.listFriendIcon);
            holder.acceptButton = (Button) convertView.findViewById(R.id.button_accept);
            holder.inviteButton = (Button) convertView.findViewById(R.id.button_invite);

            convertView.setTag(holder);
        }
        else {
            holder = (AvailableFriendAdapter.ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(rowItem.getUsername());
        mUserRef.child("profileModified").addValueEventListener(new ValueEventListener(){
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

                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(pathReference)
                        .centerCrop()
                        .override(50, 50)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        //.skipMemoryCache(true)
                        .signature(new StringSignature(date))
                        .into(holder.imageView);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
        //final ViewHolder finalHolder = holder;
        holder.inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context, ConfirmationActivity.class));
                //Toast.makeText(context, finalHolder.txtName.getText().toString() , Toast.LENGTH_SHORT).show();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                String strDate = sdf.format(now);
                mDatabase.child("users").child(UID).child("invitations").child(myUID).setValue(strDate);
                mDatabase.child("users").child(UID).child("notifications").child("invitations").child(myUID).child("time").setValue(strDate);
                mDatabase.child("users").child(UID).child("notifications").child("invitations").child(myUID).child("username").setValue(Me.myName);

                Snackbar.make(v, "You have sent an invitation!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

        //see if there is invitation


        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
                context.startActivity(new Intent(context, ConfirmationActivity.class));
            }
        });

        selfRef.child("invitations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(invitations.contains(UID)){
                    holder.inviteButton.setVisibility(View.GONE);
                    holder.acceptButton.setVisibility(View.VISIBLE);
                }
                else{
                    holder.acceptButton.setVisibility(View.GONE);
                    holder.inviteButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }
}