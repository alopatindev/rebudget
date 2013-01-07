package com.sbar.rebudget.activities;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
//import java.util.List;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.content.Intent;

import com.sbar.rebudget.Common;
import com.sbar.rebudget.R;

public class WalletsActivity extends ListActivity {
    private static final int DIALOG_NEW_WALLET = 1;
    private static final int DIALOG_NEW_WALLET_EXISTS = 2;
    private static final int DIALOG_REMOVE_WALLET = 3;

    private ArrayList<String> m_listViewItems = null;
    private String m_listViewItemSelected = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallets);

        m_listViewItems = new ArrayList<String>();

        updateListView();
        addButtonsListeners();
        registerForContextMenu(getListView());
    }

    private void updateListView() {
        setListAdapter(createAdapter(m_listViewItems.toArray(new String[0])));
    }

    private void addToListView(String item) {
        item = item.trim();
        if (item.length() == 0 || m_listViewItems.indexOf(item) != -1) {
            showDialog(DIALOG_NEW_WALLET_EXISTS);
        } else {
            m_listViewItems.add(item);
            updateListView();
        }
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
                showDialog(DIALOG_NEW_WALLET);
            }
        });
    }

    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        Common.LOGI("pos:" + position + " id:"+id);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        LayoutInflater inflater = getLayoutInflater();
        Builder builder = new AlertDialog.Builder(this);

        switch (id) {
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
                        addToListView(walletName + " (??.??)"); // TODO: show current balance
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
                            showDialog(DIALOG_NEW_WALLET);
                        }
                    }
                );

                return builder.create();
            }

        case DIALOG_REMOVE_WALLET:
            {
                final View v = inflater.inflate(R.layout.dialog_remove_wallet, null);
                final String walletName = m_listViewItemSelected;

                builder.setView(v);
                builder.setMessage("WARNING: the next operation cannot be undone! Are you sure you want to remove the wallet \"" + walletName + "\" with all its filters?");
                builder.setCancelable(true);
                builder.setPositiveButton(
                    "yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Common.LOGI("remove wallet '" + walletName + "'");
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

        return super.onCreateDialog(id);
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
        
        m_listViewItemSelected = m_listViewItems.get((int)info.id);

        switch(item.getItemId()) {
        case R.id.add_outcome_filter:
            Intent intent = new Intent(this, AddOutcomeFilterActivity.class);
            startActivity(intent);
            return true;
        case R.id.add_income_filter:
            //TODO
            return true;
        case R.id.remove_wallet:
            showDialog(DIALOG_REMOVE_WALLET);
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }
}
