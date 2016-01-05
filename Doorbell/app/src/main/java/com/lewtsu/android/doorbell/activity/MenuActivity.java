package com.lewtsu.android.doorbell.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.activity.menu.Tab1Activity;
import com.lewtsu.android.doorbell.activity.menu.Tab2Activity;
import com.lewtsu.android.doorbell.activity.menu.Tab3Activity;
import com.lewtsu.android.doorbell.constant.Constant;

public class MenuActivity extends TabActivity {

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tabHost = (TabHost) findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Tab1");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Tab2");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Tab3");

        tab1.setIndicator("Tab1");
        tab1.setContent(new Intent(this, Tab1Activity.class));

        tab2.setIndicator("Tab2");
        tab2.setContent(new Intent(this, Tab2Activity.class));

        tab3.setIndicator("Tab3");
        tab3.setContent(new Intent(this, Tab3Activity.class));

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(Constant.TAG, id + " " + R.id.action_settings);
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
