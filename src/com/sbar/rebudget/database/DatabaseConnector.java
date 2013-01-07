package com.sbar.rebudget.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sbar.rebudget.database.DatabaseHelper;
import com.sbar.rebudget.Common;

public class DatabaseConnector {
    public final String DB_NAME = "rebudget";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db = null;

    public DatabaseConnector(Context context) {
        dbHelper = new DatabaseHelper(context, DB_NAME, null, 1);
    }

    public void open() throws SQLException {
        if (db == null)
            db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null)
            db.close();
    }

    public boolean addFilter(String category, boolean outcome, String smsAddress, String smsTextContains,
                          String costIntegerRegexp, String costFracRegexp,
                          String remainingIntegerRegexp, String remainingFracRegexp,
                          String storeRegexp) {
        Common.LOGI("addFilter");
        ContentValues c = new ContentValues();
        c.put("category_id", "(select category_id from categories where category = \"" + category + "\")");
        c.put("outcome", outcome);
        c.put("sms_address", smsAddress);
        c.put("sms_text_contains", smsTextContains);
        c.put("cost_integer_regexp", costIntegerRegexp);
        c.put("cost_frac_regexp", costFracRegexp);
        c.put("remaining_integer_regexp", remainingIntegerRegexp);
        c.put("remaining_frac_regexp", remainingFracRegexp);
        c.put("store_regexp", storeRegexp);

        return db.insert("filters", null, c) != -1;
    }

    public boolean addCategory(String name, int color, float moneyPlanned, float moneySpent) {
        Common.LOGI("addCategory");
        ContentValues c = new ContentValues();

        c.put("name", name);
        c.put("color", color);
        c.put("money_planned", moneyPlanned);
        c.put("money_spent", moneySpent);

        return db.insert("categories", null, c) != -1;
    }

    public boolean addWallet(String name, float money) {
        Common.LOGI("addWallet");
        ContentValues c = new ContentValues();

        c.put("name", name);
        c.put("money", money);

        return db.insert("wallets", null, c) != -1;
    }

    public boolean addStat(long datetime,
                           float moneyPlanned, float moneySpent)
    {
        Common.LOGI("addStat");
        ContentValues c = new ContentValues();

        c.put("datetime", datetime);
        c.put("money_planned", moneyPlanned);
        c.put("money_spent", moneySpent);

        return db.insert("stats", null, c) != -1;
    }

    public Cursor selectFilters() {
        return db.query("filters", new String[] {"sms_address", "sms_text_contains"}, null, null, null, null, null, null);
    }
}
