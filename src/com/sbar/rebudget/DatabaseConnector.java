package com.sbar.rebudget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sbar.rebudget.DatabaseHelper;
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

    public void addFilter(long categoryId, boolean outcome, String smsAddress, String smsTextContains,
                          String costIntegerRegexp, String costFracRegexp,
                          String remainingIntegerRegexp, String remainingFracRegexp) {
        Common.LOGI("addFilter");
        ContentValues c = new ContentValues();
        c.put("category_id", categoryId);
        c.put("outcome", outcome);
        c.put("sms_address", smsAddress);
        c.put("sms_text_contains", smsTextContains);
        c.put("cost_integer_regexp", costIntegerRegexp);
        c.put("cost_frac_regexp", costFracRegexp);
        c.put("remaining_integer_regexp", remainingIntegerRegexp);
        c.put("remaining_frac_regexp", remainingFracRegexp);

        //open();
        db.insert("filters", null, c);
        //close();
    }

    public Cursor selectFilters() {
        return db.query("filters", new String[] {"sms_address", "sms_text_contains"}, null, null, null, null, null, null);
    }
}
