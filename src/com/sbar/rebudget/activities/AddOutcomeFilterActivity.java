package com.sbar.rebudget.activities;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ListAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import com.sbar.rebudget.Common;
import com.sbar.rebudget.activities.MainTabActivity;
import com.sbar.rebudget.R;

public class AddOutcomeFilterActivity extends ListActivity {
    static AddOutcomeFilterActivity instance = null;
    HashMap<String, String>[] m_arraySMS = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.add_outcome_filter);

        final EditText smsAddress = (EditText) findViewById(R.id.sms_address);
        EditText smsContainsText = (EditText) findViewById(R.id.sms_contains_text);
        smsContainsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Context context = (Context)AddOutcomeFilterActivity.instance;
                m_arraySMS = Common.getSMSes(
                    context,
                    "",
                    s.toString(),
                    5
                );
                ArrayList<String> lsText = new ArrayList<String>();
                for (int i = 0; i < m_arraySMS.length; ++i) {
                    lsText.add(m_arraySMS[i].get("text"));
                }

                if (lsText.size() > 0)
                    smsAddress.setText(m_arraySMS[0].get("address"));

                ListAdapter adapter = new ArrayAdapter<String>(
                    (Context) AddOutcomeFilterActivity.instance,
                    android.R.layout.simple_list_item_1,
                    lsText.toArray(new String[0])
                );
                AddOutcomeFilterActivity.instance.setListAdapter(adapter);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final EditText smsAddress = (EditText) findViewById(R.id.sms_address);
        try {
            smsAddress.setText(m_arraySMS[(int)id].get("address"));
        } catch (Exception ex) {
            smsAddress.setText("");
        }
    }
}
