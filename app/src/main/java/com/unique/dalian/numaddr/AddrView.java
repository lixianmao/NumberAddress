package com.unique.dalian.numaddr;

import android.content.Context;
import android.graphics.Color;
import android.provider.Telephony;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by dalian on 4/4/15.
 */
public class AddrView {

    private Context context;
    private WindowManager wm;
    private WindowManager.LayoutParams wmParams;
    private LinearLayout layout = null;
    private static AddrView singleView = null;

    public AddrView(Context context) {
        this.context = context;

        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = 60;
        wmParams.y = 245;
        wmParams.alpha = 0.9f;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

    }

    public static AddrView getInstachce(Context context) {
        if(singleView == null) {
            singleView = new AddrView(context);
        }
        return singleView;
    }

    public boolean isVisible() {
        return layout != null;
    }

    public void display(String addr) {

        if (layout != null)
            return;
        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout) inflater.inflate(R.layout.view_addr, null);
        Log.e("display", addr);
        TextView addrView = (TextView) layout.findViewById(R.id.address);
        addrView.setText(addr);

        wm.addView(layout, wmParams);
    }

    public void disappear() {
        Log.e("haha", "disappear");
        if (layout != null) {
            wm.removeView(layout);
            layout = null;
        }
    }

}
