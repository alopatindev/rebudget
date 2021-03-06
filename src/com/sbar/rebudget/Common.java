package com.sbar.rebudget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

public class Common {
    public static String LOG_TAG = "ReBudget";
    public static void LOGI(final String text) { Log.i(LOG_TAG, text); }
    public static void LOGE(final String text) { Log.e(LOG_TAG, text); }
    public static void LOGW(final String text) { Log.w(LOG_TAG, text); }

    /*public static HashMap[] getSMSes(
        Context context, String address, String searchPattern, int limit
    )
    {
        final String sortOrder = "date_sent DESC LIMIT " + limit;
        final String[] columns = {
            "_id", "date", "date_sent", "address", "body"
        };

        ArrayList<HashMap> list = new ArrayList<HashMap>();

        if (address.length() == 0)
            address = "%";
        if (searchPattern.length() == 0)
            searchPattern = "%";

        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(
            uri,
            columns,
            "body LIKE ? AND address LIKE ?",
            new String[]{searchPattern, address},
            sortOrder
        );
        cursor.moveToFirst();

        do {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("id", "");
            hm.put("address", "");
            hm.put("text", "");
            for(int i = 0; i < cursor.getColumnCount(); i++)
            {
                String key = cursor.getColumnName(i) + "";
                String val = cursor.getString(i) + "";

                if (key.equals("_id") ||
                    key.equals("date") ||
                    key.equals("date_sent"))
                    hm.put("id", hm.get("id") + val);
                else if (key.equals("address"))
                    hm.put("address", val);
                else if (key.equals("body"))
                    hm.put("text", val);
            }
            list.add(hm);
        } while (cursor.moveToNext());

        return list.toArray(new HashMap[0]);
    }*/


    public static HashMap[] getSMSes(
        Context context, String address, String searchPattern, int limit
    )
    {
        ArrayList<HashMap> list = new ArrayList<HashMap>();
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("id", "12345");
        hm.put("address", "111222");
        hm.put("text", "u spent 100.12 rub. in Kokoko store. now u have 300.34 rub.");
        list.add(hm);

        // FIXME: should be optimized by special query
        /*Cursor cursor = context.getContentResolver().query(
            Uri.parse("content://sms/inbox"), null, null, null, null
        );
        cursor.moveToFirst();

        ArrayList<HashMap> list = new ArrayList<HashMap>();

        //if (searchPattern.length() > 0) {
            int num = 0;
            do {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("id", "");
                hm.put("address", "");
                hm.put("text", "");
                for(int i = 0; i < cursor.getColumnCount(); i++)
                {
                    String key = cursor.getColumnName(i) + "";
                    String val = cursor.getString(i) + "";
                    String msgData = key + ": " + val;

                    if (key.equals("_id") ||
                        key.equals("date") ||
                        key.equals("date_sent"))
                        hm.put("id", hm.get("id") + val);
                    else if (key.equals("address"))
                        hm.put("address", val);
                    else if (key.equals("body"))
                        hm.put("text", val);
                }

                if (address.length() > 0 && !hm.get("address").equals(address))
                    continue;
                if (searchPattern.length() > 0 &&
                    hm.get("text").indexOf(searchPattern) == -1)
                    continue;

                list.add(hm);
                ++num;
                if (num >= limit)
                    break;
            } while (cursor.moveToNext());
        //}
        */

        return list.toArray(new HashMap[0]);
    }
}
