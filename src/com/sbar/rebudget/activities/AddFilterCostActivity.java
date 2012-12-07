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
import com.sbar.rebudget.Common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddFilterCostActivity extends Activity {
    static AddFilterCostActivity instance = null;
    TextView m_smsText = null;
    TextView m_paymentParseExample = null;
    String m_exampleCostInteger = "??";
    String m_exampleCostFrac = "??";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.add_filter_cost);

        m_smsText = (TextView) findViewById(R.id.sms_text);
        if (AddOutcomeFilterActivity.instance != null) {
            String s = AddOutcomeFilterActivity
                            .instance.m_smsSelected.get("text");
            m_smsText.setText(s);
        }

        m_paymentParseExample = (TextView) findViewById(R.id.payment_parse_example);

        EditText smsCostInteger = (EditText) findViewById(R.id.sms_cost_integer_regexp);
        smsCostInteger.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                AddFilterCostActivity.instance.setSmsCostIntegerRegexp(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        EditText smsCostFrac = (EditText) findViewById(R.id.sms_cost_frac_regexp);
        smsCostFrac.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                AddFilterCostActivity.instance.setSmsCostFracRegexp(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    public void setSmsCostIntegerRegexp(String regexp) {
        try {
            Pattern r = Pattern.compile(regexp);
            Matcher m = r.matcher(m_smsText.getText().toString());
            m_exampleCostInteger = m.find() ? m.group(1) : "??";
            int num = Integer.parseInt(m_exampleCostInteger.trim());
        } catch (Throwable e) {
            m_exampleCostInteger = "??";
        }
        updatePaymentParseExample();
    }

    public void setSmsCostFracRegexp(String regexp) {
        try {
            Pattern r = Pattern.compile(regexp);
            Matcher m = r.matcher(m_smsText.getText().toString());
            m_exampleCostFrac = m.find() ? m.group(1) : "??";
            int num = Integer.parseInt(m_exampleCostFrac.trim());
        } catch (Exception e) {
            m_exampleCostFrac = "??";
        }
        updatePaymentParseExample();
    }

    public void updatePaymentParseExample() {
        String text = String.format("Payment costs: %s.%s", m_exampleCostInteger, m_exampleCostFrac);
        m_paymentParseExample.setText(text);
    }
}
