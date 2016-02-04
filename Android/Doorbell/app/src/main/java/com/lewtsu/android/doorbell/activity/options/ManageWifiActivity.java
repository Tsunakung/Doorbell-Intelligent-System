package com.lewtsu.android.doorbell.activity.options;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.adapter.AdapterList2;
import com.lewtsu.android.doorbell.adapter.IHandleItem;
import com.lewtsu.android.doorbell.adapter.data.Map.Map2;
import com.lewtsu.android.doorbell.aynctask.SocketManageWifiScan;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ManageWifiActivity extends Activity {

    private ListView listView;
    private TextView textView;
    private AdapterList2 arrayAdapter;
    private Map2[] iconTexts;
    private SocketManageWifiScan socketManageWifi;
    private boolean isComplete;

    private Thread threadScanWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_wifi);

        listView = (ListView) findViewById(R.id.list_managewifi_1);
        textView = (TextView) findViewById(R.id.txt_managewifi_1);

        startScanWifi();
    }

    private void startScanWifi() {
        if (threadScanWifi != null && threadScanWifi.getState() != Thread.State.TERMINATED)
            return;
        threadScanWifi = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        manageWifiStart();
                    }
                });
                isComplete = false;
                List<Map2> list = null;
                socketManageWifi = new SocketManageWifiScan();
                try {
                    list = socketManageWifi.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                isComplete = true;
                iconTexts = new Map2[list != null ? list.size() : 0];
                for (int i = 0; i < (list != null ? list.size() : 0); ++i)
                    iconTexts[i] = list.get(i);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        manageWifiComplete();
                    }
                });
            }
        });
        threadScanWifi.start();
    }

    private void manageWifiStart() {
        listView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isComplete) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String loading = textView.getText().toString();
                            if (loading.length() > 9)
                                textView.setText("Loading");
                            else
                                textView.setText(loading + ".");
                        }
                    });
                    try {
                        Thread.sleep(250L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void manageWifiComplete() {
        if (iconTexts.length <= 1) {
            textView.setText("Not Found Wifi");
        } else {
            textView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }

        arrayAdapter = new AdapterList2(this, R.layout.list_3, iconTexts);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter adapt = (ArrayAdapter) parent.getAdapter();
                if (adapt instanceof AdapterList2) {
                    Map2 mapIconText = ((AdapterList2) adapt).getItem(position);
                    if (!(mapIconText.encrypt.equalsIgnoreCase("Connected") ||
                            mapIconText.encrypt.equalsIgnoreCase("Not Connected") ) &&
                            mapIconText instanceof IHandleItem) {
                        ((IHandleItem) mapIconText).hanndle(parent, view, position, id);
                    }
                }
            }
        });
    }

    public void onConnect() {
        finishAffinity();
    }

}
