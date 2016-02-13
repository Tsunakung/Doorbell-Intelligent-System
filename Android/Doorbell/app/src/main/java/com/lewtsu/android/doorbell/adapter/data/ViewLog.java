package com.lewtsu.android.doorbell.adapter.data;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.lewtsu.android.doorbell.R;
import com.lewtsu.android.doorbell.activity.menu.ViewLogActivity;
import com.lewtsu.android.doorbell.adapter.IHandleItem;
import com.lewtsu.android.doorbell.adapter.data.Map.Map4;
import com.lewtsu.android.doorbell.aynctask.HTTPDownloadBitmap;
import com.lewtsu.android.doorbell.config.Config;
import com.lewtsu.android.doorbell.constant.Constant;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class ViewLog extends Map4 implements IHandleItem {

    private int numImage = 0;
    private int maxNum = 5;
    private Bitmap[] bitmaps = new Bitmap[maxNum];

    private ViewLogActivity context;
    private int position;
    private View v;
    private ImageView next, prev;
    private Thread threadNextPrev;
    private boolean hasNext = true, hasPrev = false;
    private Toast toast;

    public ViewLog(Bitmap image, String text1, String text2) {
        super(image, text1, text2);
    }

    @Override
    public void hanndle(AdapterView<?> parent, View view, int position, long id) {
        if (!(view.getContext() instanceof ViewLogActivity))
            return;
        context = (ViewLogActivity) view.getContext();
        this.position = position;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.popup_2, null);

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(v);
        dialog.show();

        next = (ImageView) v.findViewById(R.id.imageView2);
        prev = (ImageView) v.findViewById(R.id.imageView3);
        prev.setVisibility(View.INVISIBLE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numImage < maxNum - 1) {
                    numImage++;
                    updatePopup();
                }
                if (numImage == maxNum - 1) {
                    hasNext = false;
                } else {
                    hasNext = true;
                }
                if (numImage == 0) {
                    hasPrev = false;
                } else {
                    hasPrev = true;
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numImage > 0) {
                    numImage--;
                    updatePopup();
                }
                if (numImage == maxNum - 1) {
                    hasNext = false;
                } else {
                    hasNext = true;
                }
                if (numImage == 0) {
                    hasPrev = false;
                } else {
                    hasPrev = true;
                }
            }
        });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNextPrev();
            }
        });

        updatePopup();
    }

    private void updateNextPrev() {
        if (threadNextPrev != null && threadNextPrev.getState() != Thread.State.TERMINATED)
            threadNextPrev.interrupt();

        threadNextPrev = new Thread(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(hasNext)
                            next.setVisibility(View.VISIBLE);
                        if(hasPrev)
                            prev.setVisibility(View.VISIBLE);
                    }
                });
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {

                }
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        next.setVisibility(View.INVISIBLE);
                        prev.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        threadNextPrev.start();
    }

    private void updatePopup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    updateNextPrev();
                    final ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageResource(R.drawable.time_wait);
                            if(toast != null)
                                toast.cancel();
                            toast = Toast.makeText(context, "Image " + (numImage + 1), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                    if (bitmaps[numImage] == null) {
                        final Bitmap bitmap = new HTTPDownloadBitmap(false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                "http://" + Config.getConfig().getString(Constant.CONNECT_IP) +
                                        "/img/" + context.getMap()[position].str1 +
                                        "/" + numImage + ".jpg").get();
                        bitmaps[numImage] = bitmap;
                    } else {
                        Thread.sleep(1000);
                    }

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmaps[numImage]);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
