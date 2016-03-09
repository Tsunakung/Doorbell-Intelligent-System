package com.lewtsu.android.doorbell.adapter.data;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.lewtsu.android.doorbell.MainActivity;
import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.activity.options.ManageWifiActivity;
import com.lewtsu.android.doorbell.adapter.IHandleItem;
import com.lewtsu.android.doorbell.adapter.data.Map.Map2;
import com.lewtsu.android.doorbell.aynctask.SocketManageWifiModify;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

public class ManageWifiListView extends Map2 implements IHandleItem {

    public ManageWifiListView(String v1, String v2, int v3) {
        super(v1, v2, v3);
    }

    @Override
    public void hanndle(AdapterView<?> parent, final View view, int position, long id) {
        View v = LayoutInflater.from(view.getContext()).inflate(R.layout.popup_1, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(v);
        final TextView textView = (TextView) v.findViewById(R.id.textView);
        TextView textView2 = (TextView) v.findViewById(R.id.textView2);
        final EditText editText = (EditText) v.findViewById(R.id.editText);

        textView.setText(ssid);
        if (encrypt.length() == 0) {
            textView2.setVisibility(View.INVISIBLE);
            editText.setVisibility(View.INVISIBLE);
            editText.setEnabled(false);
        }

        builder.setCancelable(true).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = editText.getText().toString();
                try {
                    new SocketManageWifiModify().execute(Config.getConfig().getString(Constant.CONNECT_IP), ssid, password, encrypt);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                view.getContext().startActivity(intent);
                if (view.getContext() instanceof ManageWifiActivity)
                    ((ManageWifiActivity) view.getContext()).onConnect();
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }
}
