package com.sbar.rebudget.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sbar.rebudget.R;
import com.sbar.rebudget.activities.AddOutcomeFilterActivity;
import com.sbar.rebudget.activities.MainTabActivity;

public class AddFilterCostActivity extends Activity {
    static AddFilterCostActivity instance = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.add_filter_cost);

        if (AddOutcomeFilterActivity.instance != null) {
            String s = AddOutcomeFilterActivity
                            .instance.m_smsSelected.get("text");
            TextView smsText = (TextView) findViewById(R.id.sms_text);
            smsText.setText(s);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
