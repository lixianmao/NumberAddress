package com.unique.dalian.numaddr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

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
     * public void createDatabase() {
     * try {
     * File file = new File(DB_PATH + "/" + DB_NAME);
     * file.createNewFile();
     * FileOutputStream fos = new FileOutputStream(file);
     * <p/>
     * InputStream is = context.getResources().openRawResource(R.raw.numaddr);
     * int hasRead = 0;
     * byte[] buf = new byte[1024 * 10];
     * while((hasRead = is.read(buf)) > 0) {
     * fos.write(buf, 0, hasRead);
     * }
     * fos.flush();
     * fos.close();
     * is.close();
     * }  catch(FileNotFoundException e) {
     * e.printStackTrace();
     * } catch(IOException e) {
     * e.printStackTrace();
     * }
     * <p/>
     * } *
     */

    public static String getNumberAddr(String phoneNumber) {
        String key;
        String sql;
        String addr = "Unknown Area";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + "/" + DB_NAME, null);

        if (phoneNumber.startsWith("1") && phoneNumber.length() >= 7) {
            key = phoneNumber.substring(0, 7);
            sql = "select MobileArea from Dm_Mobile where MobileNumber = '" + key + "'";
        } else if (phoneNumber.startsWith("0") && phoneNumber.length() >= 3) {
            if (phoneNumber.length() == 3) {
                key = phoneNumber.substring(0, 3);
                sql = "select MobileArea from Dm_Mobile where AreaCode = '" + key + "'";
            } else {
                key = phoneNumber.substring(0, 4);
                sql = "select MobileArea from Dm_Mobile where AreaCode = '" + key + "'";
            }

        } else {
            return addr;
        }

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            addr = cursor.getString(cursor.getColumnIndex("MobileArea"));
        } else if (phoneNumber.startsWith("0") && phoneNumber.length() > 3) {
            //AreaCode length is 3
            key = phoneNumber.substring(0, 3);
            sql = "select MobileArea from Dm_Mobile where AreaCode = '" + key + "'";
            Cursor c3 = db.rawQuery(sql, null);
            if (c3.getCount() > 0) {
                c3.moveToNext();
                addr = c3.getString(c3.getColumnIndex("MobileArea"));
                c3.close();
            }
        }
        cursor.close();
        db.close();
        return addr;
    }
}
