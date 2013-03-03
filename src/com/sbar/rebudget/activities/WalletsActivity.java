package com.sbar.rebudget.activities;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import java.util.ArrayList;

import com.sbar.rebudget.Common;
import com.sbar.rebudget.Pair;
import com.sbar.rebudget.R;

public class WalletsActivity extends ListActivity {
    private static final int DIALOG_NEW_WALLET = 1;
    private static final int DIALOG_NEW_WALLET_EXISTS = 2;
    private static final int DIALOG_RENAME_WALLET = 3;
    private static final int DIALOG_REMOVE_WALLET = 4;

    private ArrayList<Pair<String, Float>> m_listViewItems = null;
    private String m_listViewItemSelected = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallets);

        m_listViewItems = new ArrayList<Pair<String, Float>>();

        updateListView();
        addButtonsListeners();
        registerForContextMenu(getListView());
    }

    private void createWallet(String name) {
        name = name.trim();
        if (name.length() == 0 || !MainTabActivity.s_dc.addWallet(name, 0.0f))
            showMyDialog(DIALOG_NEW_WALLET_EXISTS);
        else
            updateListView();
    }

    private void updateListView() {
        m_listViewItems.clear();

        Cursor c = MainTabActivity.s_dc.selectWallets();
        if (c.moveToFirst()) {
            do {
                String item = c.getString(0);
                Float money = new Float(c.getFloat(1));
                m_listViewItems.add(new Pair<String, Float>(item, money));
            } while (c.moveToNext());
        }

        ArrayList<String> outList = new ArrayList<String>();
        for (Pair<String, Float> i : m_listViewItems)
            outList.add(String.format("%s (%2.2f)", i.first, i.second));

        setListAdapter(createAdapter(outList.toArray(new String[0])));
    }

    protected ListAdapter createAdapter(String [] values) {
        ListAdapter adapter = new ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            values
        );
        return adapter;
    }

    private void addButtonsListeners() {
        Button button = (Button) findViewById(R.id.add_new);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Common.LOGI("add new wallet");
                showMyDialog(DIALOG_NEW_WALLET);
            }
        });
    }

    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        Common.LOGI("pos:" + position + " id:"+id);
    }

    void showMyDialog(int id) {
        String walletName = m_listViewItemSelected;
        LayoutInflater inflater = getLayoutInflater();
        WalletDialogFragment
            .newInstance(inflater, this, id, walletName)
            .show(getFragmentManager(), "");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wallet_edit_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        
        m_listViewItemSelected = m_listViewItems.get((int)info.id).first;

        switch(item.getItemId()) {
        case R.id.add_outcome_filter:
            Intent intent = new Intent(this, AddOutcomeFilterActivity.class);
            startActivity(intent);
            return true;
        case R.id.add_income_filter:
            //TODO
            return true;
        case R.id.rename_wallet:
            showMyDialog(DIALOG_RENAME_WALLET);
            return true;
        case R.id.remove_wallet:
            showMyDialog(DIALOG_REMOVE_WALLET);
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    private static class WalletDialogFragment extends DialogFragment {
        private LayoutInflater m_inflater = null;
        private WalletsActivity m_activity = null;
        private int m_id = -1;
        private String m_walletName = null;

        public WalletDialogFragment(
            LayoutInflater inflater, WalletsActivity activity,
            int id, String walletName) {
            super();
            m_inflater = inflater;
            m_activity = activity;
            m_id = id;
            m_walletName = walletName;
        }

        public Dialog onCreateDialog(Bundle b) {
            LayoutInflater inflater = m_inflater;
            Builder builder = new AlertDialog.Builder(m_activity);

            switch (m_id) {
            case DIALOG_NEW_WALLET:
            {
            final View v = inflater.inflate(R.layout.dialog_new_wallet, null);

            builder.setView(v);
            builder.setMessage("Create new wallet");
            builder.setCancelable(true);
            builder.setPositiveButton(
                "ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText ed = (EditText) v.findViewById(R.id.wallet_name);
                        String walletName = ed.getText().toString();
                        Common.LOGI("create new wallet '" +
                                    walletName + "'");
                        WalletDialogFragment.this.m_activity.createWallet(walletName);
                        ed.setText("");
                    }
                }
            );
            builder.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }
            );
            return builder.create();
            }

            case DIALOG_NEW_WALLET_EXISTS:
            {
            final View v = inflater.inflate(
                R.layout.dialog_new_wallet_exists,
                null
            );

            builder.setView(v);
            builder.setMessage("This wallet already exists or has an empty text. Use another name.");
            builder.setCancelable(false);
            builder.setPositiveButton(
                "ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        WalletDialogFragment.this.m_activity.showMyDialog(DIALOG_NEW_WALLET);
                    }
                }
            );

            return builder.create();
            }

            case DIALOG_RENAME_WALLET:
            {
            final View v = inflater.inflate(R.layout.dialog_new_wallet, null);
            EditText ed = (EditText) v.findViewById(R.id.wallet_name);
            
            ed.setText(m_walletName);

            builder.setView(v);
            builder.setMessage("Renaming wallet " + m_walletName);
            builder.setCancelable(true);
            builder.setPositiveButton(
                "ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText ed = (EditText) v.findViewById(R.id.wallet_name);
                        String walletName = WalletDialogFragment.this.m_walletName;
                        String newWalletName = ed.getText().toString();
                        Common.LOGI("renaming wallet to '" +
                                    newWalletName + "'");
                        if (MainTabActivity.s_dc.renameWallet(walletName, newWalletName))
                            WalletDialogFragment.this.m_activity.updateListView();
                        ed.setText("");
                    }
                }
            );
            builder.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }
            );
            return builder.create();
            }

            case DIALOG_REMOVE_WALLET:
            {
            final View v = inflater.inflate(R.layout.dialog_remove_wallet, null);

            builder.setView(v);
            builder.setMessage("WARNING: the next operation cannot be undone! Are you sure you want to remove the wallet \"" + m_walletName + "\" with all its filters?");
            builder.setCancelable(true);
            builder.setPositiveButton(
                "yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String walletName = WalletDialogFragment.this.m_walletName;
                        Common.LOGI("remove wallet '" + walletName + "'");
                        if (MainTabActivity.s_dc.deleteWallet(walletName))
                            WalletDialogFragment.this.m_activity.updateListView();
                    }
                }
            );
            builder.setNegativeButton(
                "no",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }
            );

            return builder.create();
            }

            }

            return null;
        }

        public static DialogFragment newInstance(
            LayoutInflater inflater, WalletsActivity activity,
            int id, String walletName) {
            return new WalletDialogFragment(inflater, activity, id, walletName);
        }
    }
}
