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

/**
 * Created by shoyu on 16/2/2017.
 */

public class Tab3 extends Fragment{


    String[] android_version={
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
            "Nougat"};

    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the View first to facilitate findViewById
        View rootView = inflater.inflate(R.layout.tab3, container, false);
        Button signOut = (Button) rootView.findViewById(R.id.sign_out);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        //creates the adapter for the ListView, and show the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android_version);
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
