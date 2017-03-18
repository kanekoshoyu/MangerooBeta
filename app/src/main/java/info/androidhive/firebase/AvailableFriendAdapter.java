package info.androidhive.firebase;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by choiwaiyiu on 18/3/2017.
 */

public class AvailableFriendAdapter extends BaseAdapter {
    Context context;
    List<User> rowItems;

    public AvailableFriendAdapter(Context context, List<User> items) {
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtName;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AvailableFriendAdapter.ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_availablefriend, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.listFriendName);
            holder.imageView = (ImageView) convertView.findViewById(R.id.listFriendIcon);
            convertView.setTag(holder);
        }
        else {
            holder = (AvailableFriendAdapter.ViewHolder) convertView.getTag();
        }

        User rowItem = (User) getItem(position);

        holder.txtName.setText(rowItem.getUsername());
        holder.imageView.setImageResource(R.drawable.logo); //

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