package cs.dal.a4176_project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aimee on 17/11/18.
 */

//Every list view has an adapter. It is used for calories calculator
public class CustomAdapter extends ArrayAdapter {
    private ArrayList dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        CheckBox checkBox;
    }

    public CustomAdapter(ArrayList data, Context context) {
        super(context, R.layout.itemlayout, data);
        this.dataSet = data;
        this.mContext = context;

    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Item getItem(int position) {
        return (Item) dataSet.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Item item = getItem(position);


        viewHolder.txtName.setText(item.name);
        viewHolder.checkBox.setChecked(item.checked);


        return result;
    }
}
