package com.lewtsu.android.doorbell.activity.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.adapter.IHandleItem;
import com.lewtsu.android.doorbell.adapter.IconText;
import com.lewtsu.android.doorbell.adapter.data.ChangePasswordDevice;
import com.lewtsu.android.doorbell.adapter.data.ChangePin;
import com.lewtsu.android.doorbell.adapter.data.DeleteDevice;
import com.lewtsu.android.doorbell.adapter.data.MapIconText;

public class OptionsActivity extends Activity {

    private static MapIconText[] iconTexts = new MapIconText[]{
            new ChangePasswordDevice(R.drawable.lock, "Change password device"),
            new ChangePin(R.drawable.lock, "Change PIN"),
            new DeleteDevice(R.drawable.lock, "Delete device")
    };

    private ListView listView;
    private IconText arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        arrayAdapter = new IconText(this, R.layout.list_image_text_horizontal, iconTexts);

        listView = (ListView) findViewById(R.id.list_options_1);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter adapt = (ArrayAdapter) parent.getAdapter();
                if (adapt instanceof IconText) {
                    MapIconText mapIconText = ((IconText) adapt).getItem(position);
                    if (mapIconText instanceof IHandleItem)
                        ((IHandleItem) mapIconText).hanndle(parent, view, position, id);
                }
            }
        });
    }

}
