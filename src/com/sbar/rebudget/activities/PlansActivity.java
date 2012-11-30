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
    }
}
