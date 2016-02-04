package com.lewtsu.android.doorbell.activity.menu;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.adapter.AdapterList4;
import com.lewtsu.android.doorbell.adapter.IHandleItem;
import com.lewtsu.android.doorbell.adapter.data.Map.Map3;
import com.lewtsu.android.doorbell.adapter.data.Map.Map4;
import com.lewtsu.android.doorbell.aynctask.HTTPGetMissedCall;
import com.lewtsu.android.doorbell.aynctask.HTTPGetViewLog;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ViewLogActivity extends Activity {

    private ListView listView;
    private TextView textView;
    private AdapterList4 arrayAdapter;
    private Map4[] iconTexts;
    private HTTPGetViewLog getViewLog;
    private boolean isComplete;

    private Thread threadScanViewLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewlog);

        listView = (ListView) findViewById(R.id.list_viewlog_1);
        textView = (TextView) findViewById(R.id.txt_viewlog_1);

        startScanViewLog();
    }

    private void startScanViewLog() {
        if (threadScanViewLog != null && threadScanViewLog.getState() != Thread.State.TERMINATED)
            return;
        threadScanViewLog = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        manageViewLogStart();
                    }
                });
                isComplete = false;

                List<Map4> list = null;
                getViewLog = new HTTPGetViewLog();
                try {
                    list = getViewLog.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                iconTexts = new Map4[list != null ? list.size() : 0];
                for (int i = 0; i < (list != null ? list.size() : 0); ++i)
                    iconTexts[i] = list.get(i);

                isComplete = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        manageViewLogComplete();
                    }
                });
            }
        });
        threadScanViewLog.start();
    }

    private void manageViewLogComplete() {
        if (iconTexts.length <= 1) {
            textView.setText("Not Found Missed Call");
        } else {
            textView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }

        arrayAdapter = new AdapterList4(this, R.layout.list_5, iconTexts);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter adapt = (ArrayAdapter) parent.getAdapter();
                if (adapt instanceof AdapterList4) {
                    Map4 mapIconText = ((AdapterList4) adapt).getItem(position);
                    if (mapIconText instanceof IHandleItem) {
                        ((IHandleItem) mapIconText).hanndle(parent, view, position, id);
                    }
                }
            }
        });
    }

    private void manageViewLogStart() {
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

}
