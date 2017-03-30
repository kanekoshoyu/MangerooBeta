package info.androidhive.firebase.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.List;

import info.androidhive.firebase.R;
import info.androidhive.firebase.User;

/**
 * Created by choiwaiyiu on 30/3/2017.
 */

public class ParticipantSearchListRowAdapter extends BaseAdapter {
    Context context;
    List<User> rowItems;
    List<String> ids;

    private DatabaseReference mDatabase;
    private DatabaseReference mUserRef;
    private DatabaseReference selfRef;
    private FirebaseAuth auth;
    private String myUID;

    private class ViewHolder {
        ImageView imageView;
        TextView txtName;
        CheckBox checkBox;
    }

    public ParticipantSearchListRowAdapter(Context context, List<User> items, List<String> ids) {
        this.context = context;
        this.rowItems = items;
        this.ids = ids;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ParticipantSearchListRowAdapter.ViewHolder holder;
        final User rowItem = (User) getItem(position);
        final String UID = ids.get(position);

        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserRef = mDatabase.child("users").child(UID);
        selfRef = mDatabase.child("users").child(myUID);

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_participant_search_list, null);
            holder = new ParticipantSearchListRowAdapter.ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.participantSearchListName);
            holder.imageView = (ImageView) convertView.findViewById(R.id.participantSearchListIcon);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            convertView.setTag(holder);
        }
        else {
            holder = (ParticipantSearchListRowAdapter.ViewHolder) convertView.getTag();
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
