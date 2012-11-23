package com.sbar.rebudget.activities;

import android.app.Activity;
import android.os.Bundle;

import com.sbar.rebudget.R;
import com.sbar.rebudget.views.ChartView;
import android.widget.ProgressBar;

import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.app.ActionBar.LayoutParams;
import android.widget.TextView;

public class PlansActivity extends Activity {
    LinearLayout m_layout = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plans);
        m_layout = (LinearLayout) findViewById(R.id.plans_layout);
        m_layout.addView(new ChartView(this));
        addCategory("Reserved: available 122 000,00 of 123 000,00 (99.2%)");
        addCategory("Food: available 0,00 of 0,00 (0%)");
        addCategory("Cash: available 1 000,00 (0.8%)");
    }

    private void addCategory(String name) {
        TextView tv = new TextView(this);
        tv.setText(name);
        m_layout.addView(tv);

        ProgressBar pb = new ProgressBar(
            this, null, android.R.attr.progressBarStyleHorizontal
        );
        pb.setMax(100);
        pb.setProgress(50);
        pb.setLayoutParams(
            new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        );
        m_layout.addView(pb);

        // FIXME
        tv = new TextView(this);
        tv.setText("");
        m_layout.addView(tv);
    }
}
