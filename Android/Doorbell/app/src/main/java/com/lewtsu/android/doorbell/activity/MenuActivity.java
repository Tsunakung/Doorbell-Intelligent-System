package com.lewtsu.android.doorbell.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.activity.menu.HomeActivity;
import com.lewtsu.android.doorbell.activity.menu.OptionsActivity;
import com.lewtsu.android.doorbell.activity.menu.ViewLogActivity;

import static android.widget.Toast.LENGTH_SHORT;

public class MenuActivity extends TabActivity {

    private TabHost tabHost;
    private TabHost.TabSpec tab1, tab2, tab3;
    private View view1, view2, view3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);

        tab1 = tabHost.newTabSpec("Home");
        tab2 = tabHost.newTabSpec("ViewLog");
        tab3 = tabHost.newTabSpec("Setting");

        view1 = LayoutInflater.from(this).inflate(R.layout.tabicon, getTabWidget(), false);
        view2 = LayoutInflater.from(this).inflate(R.layout.tabicon, getTabWidget(), false);
        view3 = LayoutInflater.from(this).inflate(R.layout.tabicon, getTabWidget(), false);

        ((ImageView) view1.findViewById(R.id.imageView)).setImageResource(R.drawable.btn_home_hold);
        ((ImageView) view2.findViewById(R.id.imageView)).setImageResource(R.drawable.btn_time);
        ((ImageView) view3.findViewById(R.id.imageView)).setImageResource(R.drawable.btn_setting);

        tab1.setIndicator(view1);
        tab1.setContent(new Intent(this, HomeActivity.class));

        tab2.setIndicator(view2);
        tab2.setContent(new Intent(this, ViewLogActivity.class));

        tab3.setIndicator(view3);
        tab3.setContent(new Intent(this, OptionsActivity.class));

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                invalidateOptionsMenu();

                ImageView imageView = null;
                int image = 0;

                if (tabId.equalsIgnoreCase("Home")) {
                    imageView = ((ImageView) view1.findViewById(R.id.imageView));
                    image = R.drawable.btn_home_hold;
                } else if (tabId.equalsIgnoreCase("ViewLog")) {
                    imageView = ((ImageView) view2.findViewById(R.id.imageView));
                    image = R.drawable.btn_time_hold;
                } else if (tabId.equalsIgnoreCase("Setting")) {
                    imageView = ((ImageView) view3.findViewById(R.id.imageView));
                    image = R.drawable.btn_setting_hold;
                }

                ((ImageView) view1.findViewById(R.id.imageView)).setImageResource(R.drawable.btn_home);
                ((ImageView) view2.findViewById(R.id.imageView)).setImageResource(R.drawable.btn_time);
                ((ImageView) view3.findViewById(R.id.imageView)).setImageResource(R.drawable.btn_setting);

                if (imageView != null) {
                    imageView.setImageResource(image);
                }
            }
        });

        //startService(new Intent(this, CallingService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (getCurrentActivity() instanceof ITabMenu) {
            inflater.inflate(((ITabMenu) getCurrentActivity()).getOptionMenu(), menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getCurrentActivity() instanceof ITabMenu) {
            ((ITabMenu) getCurrentActivity()).handlerOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

}
