package com.sbar.rebudget.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbar.rebudget.Common;
import com.sbar.rebudget.DatabaseConnector;
import com.sbar.rebudget.R;
import com.sbar.rebudget.views.ChartView;

public class PlansActivity extends Activity {
    LinearLayout m_layout = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plans);
        m_layout = (LinearLayout) findViewById(R.id.plans_layout);
        m_layout.addView(new ChartView(this));

        DatabaseConnector dc = new DatabaseConnector(this);
        dc.open();
        dc.addFilter(-1, true, "1234", "smscontains", "(\\d+?)\\.", "(\\d+?)\\.", "(\\d+?)\\.", "(\\d+?)\\.");
        Cursor c = dc.selectFilters();
        c.moveToFirst();
        do {
            for (int i = 0; i < c.getColumnCount(); ++i) {
                Common.LOGI("''" + i + "''");
                Common.LOGI("'" + c.getColumnName(i) + "' '" + c.getString(i) + "'");
            }
        } while (c.moveToNext());
        dc.close();
    }
}
