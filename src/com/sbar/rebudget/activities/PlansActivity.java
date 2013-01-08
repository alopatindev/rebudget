package com.sbar.rebudget.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sbar.rebudget.Common;
import com.sbar.rebudget.R;
import com.sbar.rebudget.views.ChartView;

public class PlansActivity extends Activity {
    LinearLayout m_layout = null;
    ChartView m_chartView = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plans);

        ScrollView scrollView = (ScrollView) findViewById(R.id.chart_scrollview);
        m_chartView = new ChartView(this);
        updateChartView();
        scrollView.addView(m_chartView);
    }

    public void updateChartView() {
        Cursor c = MainTabActivity.s_dc.selectCategories();
        if (c.moveToFirst()) {
            do {
                m_chartView.addPiece(
                    c.getString(0), c.getInt(1),
                    c.getFloat(2), c.getFloat(3)
                );
            } while (c.moveToNext());
            m_chartView.sortPieces();
        } else {
            if (MainTabActivity.s_dc.addDefaultCategories())
                updateChartView();
            else
                Common.LOGE("failed to create default categories");
        }
    }
}
