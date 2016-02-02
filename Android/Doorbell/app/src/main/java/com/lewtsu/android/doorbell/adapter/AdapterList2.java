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
import com.lewtsu.android.doorbell.adapter.data.Map.Map2;
import com.lewtsu.android.doorbell.adapter.holder.Holder2;

public class AdapterList2 extends ArrayAdapter<Map2> {

    private final Context context;
    private Map2[] mapIconTexts;
    private int resource;

    public AdapterList2(Context context, int resource, Map2[] pmapIconTexts) {
        super(context, resource, pmapIconTexts);
        this.context = context;
        this.resource = resource;
        mapIconTexts = pmapIconTexts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder2 holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);

            holder = new Holder2();
            holder.text1 = (TextView) convertView.findViewById(R.id.text1);
            holder.text2 = (TextView) convertView.findViewById(R.id.text2);
            holder.img1 = (ImageView) convertView.findViewById(R.id.img1);

            convertView.setTag(holder);
        } else {
            holder = (Holder2) convertView.getTag();
        }

        Map2 map2 = mapIconTexts[position];
        holder.text1.setText(map2.ssid);
        holder.text2.setText(map2.encrypt);

        if(map2.feq >= 0) {
            if (map2.feq <= 17) {
                if (map2.encrypt.length() > 0)
                    holder.img1.setImageResource(R.drawable.wifi1_lock);
                else
                    holder.img1.setImageResource(R.drawable.wifi1);
            } else if (map2.feq <= 34) {
                if (map2.encrypt.length() > 0)
                    holder.img1.setImageResource(R.drawable.wifi2_lock);
                else
                    holder.img1.setImageResource(R.drawable.wifi2);
            } else if (map2.feq <= 51) {
                if (map2.encrypt.length() > 0)
                    holder.img1.setImageResource(R.drawable.wifi3_lock);
                else
                    holder.img1.setImageResource(R.drawable.wifi3);
            } else {
                if (map2.encrypt.length() > 0)
                    holder.img1.setImageResource(R.drawable.wifi4_lock);
                else
                    holder.img1.setImageResource(R.drawable.wifi4);
            }
        } else {
            holder.img1.setImageResource(R.drawable.null5);
        }




        return convertView;
    }

}
