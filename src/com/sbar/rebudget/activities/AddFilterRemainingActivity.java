package com.sbar.rebudget.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class AddFilterRemainingActivity extends Activity {
    static AddFilterRemainingActivity instance = null;
    TextView m_smsText = null;
    TextView m_remainingParseExample = null;
    String m_exampleRemainingInteger = "??";
    String m_exampleRemainingFrac = "??";
    Button m_nextButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.add_filter_remaining);

        m_smsText = (TextView) findViewById(R.id.sms_text);
        if (AddOutcomeFilterActivity.instance != null) {
            String s = AddOutcomeFilterActivity
                            .instance.m_smsSelected.get("text");
            m_smsText.setText(s);
        }

        m_remainingParseExample = (TextView) findViewById(R.id.remaining_parse_example);

        EditText smsRemainingInteger = (EditText) findViewById(R.id.sms_remaining_integer_regexp);
        smsRemainingInteger.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                AddFilterRemainingActivity.instance.setSmsRemainingIntegerRegexp(s.toString());
                AddFilterRemainingActivity.instance.m_nextButton.setEnabled(m_exampleRemainingInteger != "??");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        EditText smsRemainingFrac = (EditText) findViewById(R.id.sms_remaining_frac_regexp);
        smsRemainingFrac.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                AddFilterRemainingActivity.instance.setSmsRemainingFracRegexp(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        m_nextButton = (Button) findViewById(R.id.next);
        m_nextButton.setEnabled(false);
        m_nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO: if outcome filter
                Intent intent = new Intent(AddFilterRemainingActivity.instance, AddFilterStoreActivity.class);
                startActivity(intent);
            }
        });

        updateRemainingParseExample();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    public void setSmsRemainingIntegerRegexp(String regexp) {
        try {
            Pattern r = Pattern.compile(regexp);
            Matcher m = r.matcher(m_smsText.getText().toString());
            m_exampleRemainingInteger = m.find() ? m.group(1) : "??";
            int num = Integer.parseInt(m_exampleRemainingInteger.trim());
        } catch (Exception e) {
            m_exampleRemainingInteger = "??";
        }
        updateRemainingParseExample();
    }

    public void setSmsRemainingFracRegexp(String regexp) {
        try {
            Pattern r = Pattern.compile(regexp);
            Matcher m = r.matcher(m_smsText.getText().toString());
            m_exampleRemainingFrac = m.find() ? m.group(1) : "??";
            int num = Integer.parseInt(m_exampleRemainingFrac.trim());
        } catch (Exception e) {
            m_exampleRemainingFrac = "??";
        }
        updateRemainingParseExample();
    }

    public void updateRemainingParseExample() {
        String text = String.format("Remaining money: %s.%s", m_exampleRemainingInteger, m_exampleRemainingFrac);
        m_remainingParseExample.setText(text);
    }
}
