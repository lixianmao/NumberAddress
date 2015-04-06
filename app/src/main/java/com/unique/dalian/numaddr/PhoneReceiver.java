package com.unique.dalian.numaddr;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dalian on 4/4/15.
 */
public class PhoneReceiver extends BroadcastReceiver {

    private String phoneNumber;
    public static final int DIAL_STATE = 1;
    public static final int RING_STATE = 2;
    public static final int DIAL_IDLE_STATE = 3;
    public static final int DEFAULT_STATE = 0;
    public static int phoneState;

    public void onReceive(final Context context, Intent intent) {


        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.e("state", "ringing");
                        phoneState = RING_STATE;
                        if(!AddrView.getInstachce(context).isVisible()) {
                            AddrView.getInstachce(context).display(DBHelper.getNumberAddr(incomingNumber));
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.e("state", "offhook");
                        if(phoneState == RING_STATE) {
                            phoneState = DEFAULT_STATE;
                            AddrView.getInstachce(context).disappear();
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.e("state", "idle");
                        if(phoneState == RING_STATE || phoneState == DIAL_IDLE_STATE) {
                            phoneState = DEFAULT_STATE;
                            AddrView.getInstachce(context).disappear();
                        } else if(phoneState == DIAL_STATE) {
                            phoneState = DIAL_IDLE_STATE;
                        }
                        break;
                }


            }
        }, PhoneStateListener.LISTEN_CALL_STATE);

        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            //dial out
            phoneState = DIAL_STATE;
            phoneNumber = getResultData();
            Log.e("state", "dialing");
            AddrView.getInstachce(context).display(DBHelper.getNumberAddr(phoneNumber));
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    phoneState = DEFAULT_STATE;
                    AddrView.getInstachce(context).disappear();
                    Log.e("timertask", "runTimer");
                }
            }, 20000);
        }
    }
}
