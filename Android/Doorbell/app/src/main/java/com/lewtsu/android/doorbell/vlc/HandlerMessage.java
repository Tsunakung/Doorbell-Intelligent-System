package com.lewtsu.android.doorbell.vlc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.videolan.libvlc.EventHandler;

import java.lang.ref.WeakReference;

public class HandlerMessage extends Handler {

    private WeakReference<VideoVLC> mOwner;

    public HandlerMessage(VideoVLC owner) {
        mOwner = new WeakReference<>(owner);
    }

    @Override
    public void handleMessage(Message msg) {
        VideoVLC player = mOwner.get();

        if (msg.what == VideoVLC.VideoSizeChanged) {
            player.setSize(msg.arg1, msg.arg2);
            return;
        }

        Bundle b = msg.getData();
        switch (b.getInt("event")) {
            case EventHandler.MediaPlayerEndReached:
                player.releasePlayer();
                break;
            case EventHandler.MediaPlayerPlaying:
            case EventHandler.MediaPlayerPaused:
            case EventHandler.MediaPlayerStopped:
            default:
                break;
        }
    }
}