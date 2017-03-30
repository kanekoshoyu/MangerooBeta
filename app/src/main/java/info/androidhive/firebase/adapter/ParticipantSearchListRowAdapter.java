package info.androidhive.firebase.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.ConfirmationActivity;
import info.androidhive.firebase.DataTransferInterface;
import info.androidhive.firebase.R;
import info.androidhive.firebase.User;

/**
 * Created by choiwaiyiu on 30/3/2017.
 */

public class ParticipantSearchListRowAdapter extends BaseAdapter {
    DataTransferInterface dtInterface;
    Context context;
    List<User> rowItems;
    List<String> ids;
    ArrayList<String> participantIDs;
    ArrayList<String> checked;

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

    public ParticipantSearchListRowAdapter(Context context, DataTransferInterface dtInterface, List<User> items, List<String> ids, ArrayList<String> participantIDs, ArrayList<String> checked) {
        this.context = context;
        this.dtInterface = dtInterface;
        this.rowItems = items;
        this.ids = ids;
        this.participantIDs = participantIDs;
        //Toast.makeText(context, checked.size()+"", Toast.LENGTH_SHORT).show();
        this.checked = new ArrayList<>(checked);
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
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

        //Toast.makeText(context, position+" "+checked.get(position), Toast.LENGTH_SHORT).show();
        if(checked.get(position).equals("not checked"))
            holder.checkBox.setChecked(false);
        if(checked.get(position).equals("checked"))
            holder.checkBox.setChecked(true);

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


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checked.get(position).equals("not checked")){
                    participantIDs.add(UID);
                    checked.set(position, "checked");
                    ///////////
                    /*
                    for(int i=0; i<participantIDs.size(); i++)
                        Toast.makeText(context, participantIDs.get(i), Toast.LENGTH_SHORT).show();
                    */
                }
                else{
                    for(int i=0; i<participantIDs.size(); i++)
                        if(participantIDs.get(i).equals(UID)){
                            participantIDs.remove(i);
                            break;
                        }
                    checked.set(position, "not checked");
                }
                dtInterface.setValues(checked);
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
