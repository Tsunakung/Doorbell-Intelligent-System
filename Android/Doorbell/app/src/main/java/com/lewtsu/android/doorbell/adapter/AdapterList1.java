package com.lewtsu.android.doorbell.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.adapter.data.Map.Map1;
import com.lewtsu.android.doorbell.adapter.holder.Holder1;

public class AdapterList1 extends ArrayAdapter<Map1> {

    private final Context context;
    private Map1[] mapIconTexts;
    private int resource;

    public AdapterList1(Context context, int resource, Map1[] pmapIconTexts) {
        super(context, resource, pmapIconTexts);
        this.context = context;
        this.resource = resource;
        mapIconTexts = pmapIconTexts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder1 holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);

            holder = new Holder1();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.text = (TextView) convertView.findViewById(R.id.text);

            convertView.setTag(holder);
        } else {
            holder = (Holder1) convertView.getTag();
        }

        Map1 iconText = mapIconTexts[position];
        holder.icon.setImageResource(iconText.icon);
        holder.text.setText(iconText.str);
        return convertView;
    }

}
