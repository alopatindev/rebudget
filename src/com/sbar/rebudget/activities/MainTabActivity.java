package com.sbar.rebudget.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.sbar.rebudget.activities.PlansActivity;
import com.sbar.rebudget.activities.WalletsActivity;
import com.sbar.rebudget.activities.StatsActivity;
import com.sbar.rebudget.Common;
import com.sbar.rebudget.receivers.SmsListener;
import com.sbar.rebudget.R;

public class MainTabActivity extends TabActivity {
    private TabHost m_tabHost = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        m_tabHost = getTabHost();
        createTab("Plans", R.drawable.icon_plans_tab, PlansActivity.class);
        createTab("Wallets", R.drawable.icon_wallets_tab, WalletsActivity.class);
        createTab("Stats", R.drawable.icon_stats_tab, StatsActivity.class);

        //SmsListener.readSms(this);
    }

    private void createTab(final String title, int iconId, Class<?> activityClass)
    {
        TabSpec spec = m_tabHost.newTabSpec(title);
        spec.setIndicator(title, getResources().getDrawable(iconId));
        Intent intent = new Intent(this, activityClass);
        spec.setContent(intent);
        m_tabHost.addTab(spec);
    }
}
