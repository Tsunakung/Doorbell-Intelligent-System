package com.lewtsu.android.doorbell.activity.menu;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.adapter.AdapterList1;
import com.lewtsu.android.doorbell.adapter.AdapterList3;
import com.lewtsu.android.doorbell.adapter.IHandleItem;
import com.lewtsu.android.doorbell.adapter.data.Map.Map1;
import com.lewtsu.android.doorbell.adapter.data.Map.Map3;
import com.lewtsu.android.doorbell.aynctask.HTTPGetMissedCall;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MissedCallActivity extends Activity {

    private ListView listView;
    private TextView textView;
    private AdapterList3 arrayAdapter;
    private Map3[] iconTexts;
    private HTTPGetMissedCall getMissedCall;
    private boolean isComplete;

    private Thread threadScanMissedCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missedcall);

        listView = (ListView) findViewById(R.id.list_missedcall_1);
        textView = (TextView) findViewById(R.id.txt_missedcall_1);

        startScanMissedCall();
    }

    private void startScanMissedCall() {
        if (threadScanMissedCall != null && threadScanMissedCall.getState() != Thread.State.TERMINATED)
            return;
        threadScanMissedCall = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        manageMissedCallStart();
                    }
                });
                isComplete = false;

                List<Map3> list = null;
                getMissedCall = new HTTPGetMissedCall();
                try {
                    list = getMissedCall.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                iconTexts = new Map3[list != null ? list.size() : 0];
                for (int i = 0; i < (list != null ? list.size() : 0); ++i)
                    iconTexts[i] = list.get(i);

                isComplete = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        manageMissedCallComplete();
                    }
                });
            }
        });
        threadScanMissedCall.start();
    }

    private void manageMissedCallStart() {
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

    private void manageMissedCallComplete() {
        if (iconTexts.length <= 1) {
            textView.setText("Not Found Missed Call");
        } else {
            textView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }

        arrayAdapter = new AdapterList3(this, R.layout.list_4, iconTexts);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter adapt = (ArrayAdapter) parent.getAdapter();
                if (adapt instanceof AdapterList3) {
                    Map3 mapIconText = ((AdapterList3) adapt).getItem(position);
                    if (mapIconText instanceof IHandleItem) {
                        ((IHandleItem) mapIconText).hanndle(parent, view, position, id);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_missedcall, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remove_misscall) {
            item.setIcon(R.drawable.btn_bin_hold);
            Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
