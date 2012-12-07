package com.sbar.rebudget.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import com.sbar.rebudget.Common;
import com.sbar.rebudget.activities.MainTabActivity;
import com.sbar.rebudget.activities.AddFilterCostActivity;
import com.sbar.rebudget.R;


public class AddOutcomeFilterActivity extends ListActivity {
    static AddOutcomeFilterActivity instance = null;
    HashMap<String, String>[] m_arraySMS = null;
    TextView m_smsAddress = null;
    HashMap<String, String> m_smsSelected = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(R.layout.add_filter);
        addButtonsListeners();

        m_smsAddress = (TextView) findViewById(R.id.sms_address);
        EditText smsContainsText = (EditText) findViewById(R.id.sms_contains_text);
        smsContainsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                AddOutcomeFilterActivity.instance.searchSMS(s.toString());
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

    private void addButtonsListeners() {
        Button button = (Button) findViewById(R.id.next);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Common.LOGI("next");
                Intent intent = new Intent(AddOutcomeFilterActivity.instance,
                                           AddFilterCostActivity.class);
                startActivity(intent);
            }
        });
    }

    public void searchSMS(String pattern)
    {
        Context context = (Context) AddOutcomeFilterActivity.instance;
        m_arraySMS = Common.getSMSes(context, "", pattern, 5);

        ArrayList<String> lsText = new ArrayList<String>();
        for (int i = 0; i < m_arraySMS.length; ++i) {
            lsText.add(m_arraySMS[i].get("text"));
        }

        if (lsText.size() > 0)
            m_smsAddress.setText(m_arraySMS[0].get("address"));

        ListAdapter adapter = new ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            lsText.toArray(new String[0])
        );
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        try {
            m_smsSelected = m_arraySMS[(int)id];
            m_smsAddress.setText(m_smsSelected.get("address"));
        } catch (Exception ex) {
            m_smsAddress.setText("");
        }
    }
}
