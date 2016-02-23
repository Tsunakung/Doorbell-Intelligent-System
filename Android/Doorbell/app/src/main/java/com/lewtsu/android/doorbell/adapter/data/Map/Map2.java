package com.lewtsu.android.doorbell.adapter.data.Map;

public abstract class Map2 {

    public String ssid, encrypt;
    public int feq;

    public Map2(String v1, String v2, int v3) {
        ssid = v1;
        encrypt = v2;
        feq = v3;
    }

}
