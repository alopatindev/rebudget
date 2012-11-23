package com.sbar.rebudget.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.sbar.rebudget.activities.MainTabActivity;

import android.net.Uri;
import android.database.Cursor;

public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msgFrom = ""; // TODO: use HashMap with msgFrom and msgBody
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    String msgBody = "";
                    for(int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msgFrom = msgs[i].getOriginatingAddress();
                        msgBody += msgs[i].getMessageBody();
                    }
                    MainTabActivity.LOGI("sms from '" + msgFrom + "'");
                    MainTabActivity.LOGI("sms text '" + msgBody + "'");
                } catch(Exception e) {
                    MainTabActivity.LOGE("Exception caught: " + e.getMessage());
                }
            }
        }
    }

    /*public static void readSms(Context context) {
        Cursor cursor = context.getContentResolver().query(
            Uri.parse("content://sms/inbox"), null, null, null, null
        );
        cursor.moveToFirst();

        do {
            for(int i = 0; i < cursor.getColumnCount(); i++)
            {
                String key = cursor.getColumnName(i) + "";
                String val = cursor.getString(i) + "";
                String msgData = key + ": " + val;

                if (key.equals("_id") ||
                    key.equals("date") ||
                    key.equals("date_sent") ||
                    key.equals("address") ||
                    key.equals("body"))
                    MainTabActivity.LOGI(msgData);
            }
            MainTabActivity.LOGI("====");
        } while (cursor.moveToNext());
    }*/
}
