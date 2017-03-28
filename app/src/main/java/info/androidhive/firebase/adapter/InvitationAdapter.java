package info.androidhive.firebase.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.androidhive.firebase.Invitation;
import info.androidhive.firebase.R;

/**
 * Created by choiwaiyiu on 18/3/2017.
 */

public class InvitationAdapter extends BaseAdapter {
    Context context;
    List<Invitation> rowItems;

    public InvitationAdapter(Context context, List<Invitation> items) {
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtName;
        TextView txtDateTime;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        InvitationAdapter.ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_invitation, null);
            holder = new InvitationAdapter.ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.listInvitationUserName);
            holder.txtDateTime = (TextView) convertView.findViewById(R.id.listInvitationDateTime);
            holder.imageView = (ImageView) convertView.findViewById(R.id.listInvitationIcon);
            convertView.setTag(holder);
        }
        else {
            holder = (InvitationAdapter.ViewHolder) convertView.getTag();
        }

        Invitation rowItem = (Invitation) getItem(position);

        holder.txtName.setText(rowItem.getUserName());
        holder.txtDateTime.setText(rowItem.getDateTime());
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