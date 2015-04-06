package com.unique.dalian.numaddr;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MyActivity extends Activity {

    private Button searchBtn;
    private EditText et;
    private TextView tv;
    private SQLiteDatabase db;
    private Button cancelBtn;

    private int prevX, prevY;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        searchBtn = (Button) findViewById(R.id.search);
        et = (EditText) findViewById(R.id.et);
        tv = (TextView) findViewById(R.id.tv);
        cancelBtn = (Button) findViewById(R.id.cancel);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String num = et.getText().toString();
                if (num != null && !num.isEmpty()) {
                    handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == 0x123) {
                                tv.setText(msg.obj.toString());
                            }
                        }
                    };
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String addr = DBHelper.getNumberAddr(num);
                            Message msg = new Message();
                            msg.what = 0x123;
                            msg.obj = addr;
                            handler.sendMessage(msg);
                        }
                    }).start();

                } else {
                    Toast.makeText(MyActivity.this, "invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
    }


}
