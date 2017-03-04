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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by shoyu on 16/2/2017.
 */

public class Tab1 extends Fragment {

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRef.child("");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);

        Switch mSwitchFree = (Switch) rootView.findViewById(R.id.switchFree);
        final TextView mTextViewList = (TextView) rootView.findViewById(R.id.textViewList);

        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mTextViewList.setText(text);
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
                    mConditionRef.setValue("Shoyu is free now for dining");
                else
                    mConditionRef.setValue("Shoyu is not free");
            }
        });

        return rootView;


    }
}
