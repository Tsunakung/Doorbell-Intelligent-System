package com.lewtsu.android.doorbell.activity.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.adapter.IHandleItem;
import com.lewtsu.android.doorbell.adapter.AdapterList1;
import com.lewtsu.android.doorbell.adapter.data.ChangePasswordDevice;
import com.lewtsu.android.doorbell.adapter.data.ChangePin;
import com.lewtsu.android.doorbell.adapter.data.DeleteDevice;
import com.lewtsu.android.doorbell.adapter.data.ManageWifi;
import com.lewtsu.android.doorbell.adapter.data.Map.Map1;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

public class OptionsActivity extends Activity {

    private static Map1[] iconTexts = new Map1[]{
            new ManageWifi(R.drawable.null5, "Manage Wifi"),
            new ChangePasswordDevice(R.drawable.null5, "Change password device"),
            new ChangePin(R.drawable.null5, "Change PIN"),
            new DeleteDevice(R.drawable.null5, "Delete device")
    };

    private ListView listView;
    private AdapterList1 arrayAdapter;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        textView = (TextView) findViewById(R.id.txt_options_1);
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
    }

}
