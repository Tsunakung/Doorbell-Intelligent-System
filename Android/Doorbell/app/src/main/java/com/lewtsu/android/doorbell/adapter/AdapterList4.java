package com.lewtsu.android.doorbell.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.adapter.data.Map.Map4;
import com.lewtsu.android.doorbell.adapter.holder.Holder2;
import com.lewtsu.android.doorbell.aynctask.HTTPDownloadBitmap3;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

public class AdapterList4 extends ArrayAdapter<Map4> {

    private Context context;
    private Map4[] mapIconTexts;
    private int resource;
    private LayoutInflater inflater;

    public AdapterList4(Context context, int resource, Map4[] pmapIconTexts) {
        super(context, resource, pmapIconTexts);
        this.context = context;
        this.resource = resource;
        this.mapIconTexts = pmapIconTexts;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mapIconTexts.length; ++i) {
                    try {
                        mapIconTexts[i].download = new HTTPDownloadBitmap3(AdapterList4.this, i, true);
                        mapIconTexts[i].download.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://" + Config.getConfig().getString(Constant.CONNECT_IP) + "/img/" + mapIconTexts[i].str1 + "/2.jpg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder2 holder;

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);

            holder = new Holder2();
            holder.img1 = (ImageView) convertView.findViewById(R.id.icon);
            holder.text1 = (TextView) convertView.findViewById(R.id.text1);
            holder.text2 = (TextView) convertView.findViewById(R.id.text2);

            convertView.setTag(holder);
        } else {
            holder = (Holder2) convertView.getTag();
        }

        Map4 iconText = mapIconTexts[position];

        holder.text1.setText(iconText.str1);
        holder.text2.setText(iconText.str2);
        if (iconText.icon != null)
            holder.img1.setImageBitmap(iconText.icon);
        else
            holder.img1.setImageResource(R.drawable.time_wait);

        return convertView;
    }

    /*
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder2 holder;

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);

            holder = new Holder2();
            holder.img1 = (ImageView) convertView.findViewById(R.id.icon);
            holder.text1 = (TextView) convertView.findViewById(R.id.text1);
            holder.text2 = (TextView) convertView.findViewById(R.id.text2);

            convertView.setTag(holder);
        } else {
            holder = (Holder2) convertView.getTag();
        }

        final View tempView = convertView;
        final Map4 iconText = mapIconTexts[position];

        if (!iconText.isDownload) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        iconText.isDownload = true;
                        iconText.icon = new HTTPDownloadBitmap().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://" + Config.getConfig().getString(Constant.CONNECT_IP) + "/img/" + iconText.str1 + "/2.jpg").get();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    ((Activity) tempView.getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.img1.setImageBitmap(iconText.icon);
                        }
                    });
                }
            }).start();
        } else {
            holder.img1.setImageBitmap(iconText.icon);
        }
        holder.text1.setText(iconText.str1);
        holder.text2.setText(iconText.str2);

        return convertView;
    }
    */

}
