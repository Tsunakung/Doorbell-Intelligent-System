package com.lewtsu.android.doorbell.config;

import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {

    private static JSONObject config;

    public static void loadConfig() {
        try {
            StringBuilder sb = new StringBuilder();
            File file = new File(Constant.CONFIG_FILE);

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String z;
            while ((z = br.readLine()) != null)
                sb.append(z);
            br.close();

            String str = sb.toString();
            config = new JSONObject(str);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            config = new JSONObject();
            e.printStackTrace();
        }
    }

    public static void writeConfig() {
        if (config == null)
            config = new JSONObject();
        try {
            File file = new File(Constant.CONFIG_FILE);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(config.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getConfig() {
        return config;
    }


}
