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
import com.lewtsu.android.doorbell.adapter.data.Map.Map3;
import com.lewtsu.android.doorbell.adapter.holder.Holder1;
import com.lewtsu.android.doorbell.aynctask.HTTPDownloadBitmap2;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

public class AdapterList3 extends ArrayAdapter<Map3> {

    private final Context context;
    private Map3[] mapIconTexts;
    private int resource;
    private LayoutInflater inflater;

    public AdapterList3(Context context, int resource, Map3[] pmapIconTexts) {
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
                        mapIconTexts[i].download = new HTTPDownloadBitmap2(AdapterList3.this, i, true);
                        mapIconTexts[i].download.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://" + Config.getConfig().getString(Constant.CONNECT_IP) + "/img/" + mapIconTexts[i].str + "/2.jpg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder1 holder;

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);

            holder = new Holder1();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.text = (TextView) convertView.findViewById(R.id.text);

            convertView.setTag(holder);
        } else {
            holder = (Holder1) convertView.getTag();
        }

        Map3 iconText = mapIconTexts[position];

        holder.text.setText(iconText.str);
        if (iconText.icon != null)
            holder.icon.setImageBitmap(iconText.icon);
        else
            holder.icon.setImageResource(R.drawable.time_wait);

        return convertView;
    }

    /*
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder1 holder;

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

        final View tempView = convertView;
        final Map3 iconText = mapIconTexts[position];

        if (!iconText.isDownload) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        iconText.isDownload = true;
                        iconText.icon = new HTTPDownloadBitmap().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://" + Config.getConfig().getString(Constant.CONNECT_IP) + "/img/" + iconText.str + "/2.jpg").get();
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
                            holder.icon.setImageBitmap(iconText.icon);
                        }
                    });
                }
            }).start();
        } else {
            holder.icon.setImageBitmap(iconText.icon);
        }
        holder.text.setText(iconText.str);

        return convertView;
    }
    */

}
