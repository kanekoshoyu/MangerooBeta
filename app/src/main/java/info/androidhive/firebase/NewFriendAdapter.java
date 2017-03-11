package info.androidhive.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoyu on 5/3/2017.
 */

public class NewFriendAdapter extends BaseAdapter implements Filterable{

    private Context context;
    private ArrayList<String> userList;

    public NewFriendAdapter(Context mContext, ArrayList<String> mUserList){
        this.context = mContext;
        this.userList = mUserList;
    }

    @Override
    public int getCount() {

        //outout number of users here
        return userList.size();
    }
    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public void add(String newUsername){
        this.userList.add(newUsername);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.row_newfriend, null);
        TextView newFriendName = (TextView) v.findViewById(R.id.new_friendName);
        newFriendName.setText((CharSequence) userList);
        Button newButton = (Button)v.findViewById(R.id.new_friendAddButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something to add the friend here
            }
        });
        return null;
    }

    @Override
    public Filter getFilter() {
        //filter out the  data
        return null;
    }
}
