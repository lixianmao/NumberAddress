package com.unique.dalian.numaddr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dalian on 4/5/15.
 */
public class DBHelper {

    public static final String DB_NAME = "num_addr.db";
    public static final String PACKAGE_NAME = "com.unique.dalian.numaddr";
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME;

    private Context context;

    public DBHelper(Context context) {
        this.context = context;
    }

    /**
    public void createDatabase() {
        try {
            File file = new File(DB_PATH + "/" + DB_NAME);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);

            InputStream is = context.getResources().openRawResource(R.raw.numaddr);
            int hasRead = 0;
            byte[] buf = new byte[1024 * 10];
            while((hasRead = is.read(buf)) > 0) {
                fos.write(buf, 0, hasRead);
            }
            fos.flush();
            fos.close();
            is.close();
        }  catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

    } **/

    public static String getNumberAddr(String phoneNumber) {
        String key;
        String sql;
        String addr = "Unknown Area";
        if (phoneNumber.startsWith("1") && phoneNumber.length() >= 7) {
            key = phoneNumber.substring(0, 7);
            sql = "select MobileArea from Dm_Mobile where MobileNumber = '" + key + "'";
        } else if (phoneNumber.startsWith("0") && phoneNumber.length() >= 3) {
            key = phoneNumber.substring(0, 3);
            sql = "select MobileArea, AreaCode from Dm_Mobile where AreaCode like '" + key + "%'";
        } else {
            return addr;
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + "/" + DB_NAME, null);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            addr = cursor.getString(cursor.getColumnIndex("MobileArea"));
        } else if (cursor.getCount() > 1) {
            key = phoneNumber.substring(0, 4);
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("AreaCode")).equals(key)) {
                    addr = cursor.getString(cursor.getColumnIndex("MobileArea"));
                    break;
                }
            }
        }
        cursor.close();
        db.close();
        return addr;
    }
}
