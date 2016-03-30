package com.lewtsu.android.doorbell.activity.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.adapter.AdapterList1;
import com.lewtsu.android.doorbell.adapter.IHandleItem;
import com.lewtsu.android.doorbell.adapter.data.ChangePasswordDevice;
import com.lewtsu.android.doorbell.adapter.data.ChangePasswordDoor;
import com.lewtsu.android.doorbell.adapter.data.ChangePin;
import com.lewtsu.android.doorbell.adapter.data.DeleteDevice;
import com.lewtsu.android.doorbell.adapter.data.ManageWifi;
import com.lewtsu.android.doorbell.adapter.data.Map.Map1;
import com.lewtsu.android.doorbell.aynctask.HTTPGetFreeDisk;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

public class OptionsActivity extends Activity {

    private static Map1[] iconTexts = new Map1[]{
            new ManageWifi(R.drawable.null5, "Manage Wifi"),
            new ChangePasswordDevice(R.drawable.null5, "Change password device"),
            new ChangePasswordDoor(R.drawable.null5, "Change password door"),
            new ChangePin(R.drawable.null5, "Change PIN"),
            new DeleteDevice(R.drawable.null5, "Disconnect")
    };

    private ListView listView;
    private AdapterList1 arrayAdapter;
    private TextView textView, textView2;
    private Switch sw;
    private ProgressBar progressBar;
    private double freedisk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        textView = (TextView) findViewById(R.id.txt_options_1);
        textView2 = (TextView) findViewById(R.id.txt_options_2);
        sw = (Switch) findViewById(R.id.list_switch_1);
        progressBar = (ProgressBar) findViewById(R.id.list_progressBar_1);

        try {
            textView.setText("IP: " + Config.getConfig().getString(Constant.CONNECT_IP));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        arrayAdapter = new AdapterList1(this, R.layout.list_2, iconTexts);

        listView = (ListView) findViewById(R.id.list_options_1);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter adapt = (ArrayAdapter) parent.getAdapter();
                if (adapt instanceof AdapterList1) {
                    Map1 mapIconText = ((AdapterList1) adapt).getItem(position);
                    if (mapIconText instanceof IHandleItem)
                        ((IHandleItem) mapIconText).hanndle(parent, view, position, id);
                }
            }
        });

        try {
            sw.setChecked(Config.getConfig().getBoolean(Constant.NOTIFICATION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = sw.isChecked();
                try {
                    Config.getConfig().put(Constant.NOTIFICATION, check);
                    Config.writeConfig();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        startScanFreeDisk();
    }

    public void startScanFreeDisk() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    freedisk = 100 - new HTTPGetFreeDisk().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DecimalFormat df = new DecimalFormat("#.##");
                            textView2.setText(textView2.getText() + " " + df.format(freedisk) + "%");
                            progressBar.setProgress((int) freedisk);
                            if(freedisk < 50)
                                progressBar.setBackgroundColor(0xFF00FF00);
                            else if(freedisk < 70)
                                progressBar.setBackgroundColor(0xFFFFFF00);
                            else if(freedisk < 90)
                                progressBar.setBackgroundColor(0xFFFF8800);
                            else
                                progressBar.setBackgroundColor(0xFFFF0000);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog alert = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_Dialog))
                .create();
        alert.setMessage("Do you want to exit ?");
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);

        alert.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                        finish();
                    }
                });

        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                    }
                });

        alert.show();
    }

}
