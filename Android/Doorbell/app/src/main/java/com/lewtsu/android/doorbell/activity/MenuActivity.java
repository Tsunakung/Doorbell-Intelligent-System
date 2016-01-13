package com.lewtsu.android.doorbell.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.adapter.IHandleItem;
import com.lewtsu.android.doorbell.adapter.IconText;
import com.lewtsu.android.doorbell.adapter.data.Camera;
import com.lewtsu.android.doorbell.adapter.data.MapIconText;
import com.lewtsu.android.doorbell.adapter.data.MissedCall;
import com.lewtsu.android.doorbell.adapter.data.Options;
import com.lewtsu.android.doorbell.adapter.data.ViewLog;

public class MenuActivity extends Activity {

    //private static String[] itemName = new String[] {"Camera", "Missed Call", "Log", "Options"};
    private static MapIconText[] iconTexts = new MapIconText[]{
            new Camera(R.drawable.lock, "Camera"),
            new MissedCall(R.drawable.lock, "Missed Call"),
            new ViewLog(R.drawable.lock, "View Log"),
            new Options(R.drawable.lock, "Options")
    };
    private static IconText arrayAdapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        arrayAdapter = new IconText(this, R.layout.list_image_text, iconTexts);


        listView = (ListView) findViewById(R.id.list_menu_1);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((IHandleItem) iconTexts[position]).hanndle(parent, view, position, id);
            }
        });

    }

}
