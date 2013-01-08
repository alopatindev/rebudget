package com.sbar.rebudget.database;

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
        db.execSQL("create table filters (id integer primary key autoincrement, category_id integer, wallet_id integer, outcome boolean, sms_address, sms_text_contains, cost_integer_regexp, cost_frac_regexp, remaining_integer_regexp, remaining_frac_regexp, store_regexp);");
        db.execSQL("create table categories (id integer primary key autoincrement, name, color integer, money_planned float, money_spent float, unique (name collate nocase asc));");
        db.execSQL("create table wallets (id integer primary key autoincrement, name, money float, unique (name collate nocase asc));");
        db.execSQL("create table stats (id integer primary key autoincrement, date integer, money_planned float, money_spent float);");
        db.execSQL("create table stores (id integer primary key autoincrement, name, category_id integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Common.LOGI("DatabaseHelper.onUpgrade " + oldVersion + " -> " + newVersion);
    }
}

// vim: textwidth=0
