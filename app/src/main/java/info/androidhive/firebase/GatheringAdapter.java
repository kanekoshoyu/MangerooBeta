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

public class GatheringAdapter extends BaseAdapter {
    Context context;
    List<Gathering> rowItems;

    public GatheringAdapter(Context context, List<Gathering> items) {
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDate;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_gathering, null);
            holder = new ViewHolder();
            holder.txtDate = (TextView) convertView.findViewById(R.id.listGatheringDate);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.listGatheringTitle);
            holder.imageView = (ImageView) convertView.findViewById(R.id.listGatheringIcon);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Gathering rowItem = (Gathering) getItem(position);

        holder.txtDate.setText(rowItem.getDate());
        holder.txtTitle.setText(rowItem.getTitle());
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