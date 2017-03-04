package info.androidhive.firebase;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shoyu on 16/2/2017.
 */

public class Tab2 extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the View first to facilitate findViewById
        View rootView = inflater.inflate(R.layout.tab2, container, false);

        return rootView;
    }
}
