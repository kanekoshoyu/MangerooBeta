package info.androidhive.firebase;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.adapter.GatheringAdapter;

/**
 * Created by shoyu on 16/2/2017.
 */

public class Tab2 extends Fragment{

    private FirebaseAuth auth;
    private DatabaseReference mGatherings;
    private List<Gathering> gatheringArrayList = new ArrayList<>();
    private List<String> GIDs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the View first to facilitate findViewById
        View rootView = inflater.inflate(R.layout.tab2, container, false);

        auth = FirebaseAuth.getInstance();
        final String UID = auth.getCurrentUser().getUid();

        mGatherings = FirebaseDatabase.getInstance().getReference().child("gathering");

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddGatheringActivity.class));
            }
        });

        ListView gatheringListView = (ListView) rootView.findViewById(R.id.gatheringList);
        final GatheringAdapter adapter = new GatheringAdapter(getActivity(), gatheringArrayList);
        gatheringListView.setAdapter(adapter);

        gatheringListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Toast.makeText(getActivity(),Integer.toString(position), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(),Long.toString(id), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), DetailGatheringActivity.class);
                intent.putExtra("GID", GIDs.get(position));
                //Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

        mGatherings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                gatheringArrayList.clear();
                GIDs.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String tHolderID, tTitle, tDate, tStartTime, tEndTime, tPlace;
                    tHolderID = postSnapshot.child("holderID").getValue(String.class);
                    tTitle = postSnapshot.child("title").getValue(String.class);
                    tDate = postSnapshot.child("date").getValue(String.class);
                    tStartTime = postSnapshot.child("startTime").getValue(String.class);
                    tEndTime = postSnapshot.child("endTime").getValue(String.class);
                    tPlace = postSnapshot.child("place").getValue(String.class);

                    if(tHolderID.equals(UID)) {
                        GIDs.add(postSnapshot.getKey());
                        /////////////////////////////////////////////
                        List<String> nothing = new ArrayList<String>();
                        gatheringArrayList.add(new Gathering(tHolderID, tTitle, tDate, tStartTime, tEndTime, tPlace, nothing));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        return rootView;
    }
}
