package com.sbar.rebudget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.sbar.rebudget.Common;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Common.LOGI("DatabaseHelper.onCreate");
        String createQuery = "create table filters (" +
            "id integer primary key autoincrement," +
            "category_id integer," +
            "outcome boolean," +
            "sms_address," +
            "sms_text_contains," +
            "cost_integer_regexp," +
            "cost_frac_regexp," +
            "remaining_integer_regexp," +
            "remaining_frac_regexp" +
            ");"; 
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Common.LOGI("DatabaseHelper.onUpgrade " + oldVersion + " -> " + newVersion);
    }
}
