package com.lewtsu.android.doorbell.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.adapter.data.MapIconText;
import com.lewtsu.android.doorbell.adapter.holder.HolderIconText;

public class IconText extends ArrayAdapter<MapIconText> {

    private final Context context;
    private MapIconText[] mapIconTexts;
    private int resource;

    public IconText(Context context, int resource, MapIconText[] pmapIconTexts) {
        super(context, resource, pmapIconTexts);
        this.context = context;
        this.resource = resource;
        mapIconTexts = pmapIconTexts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderIconText holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);

            holder = new HolderIconText();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.text = (TextView) convertView.findViewById(R.id.text);

            convertView.setTag(holder);
        } else {
            holder = (HolderIconText) convertView.getTag();
        }

        MapIconText iconText = mapIconTexts[position];
        holder.icon.setImageResource(iconText.icon);
        holder.text.setText(iconText.str);
        return convertView;
    }

}
