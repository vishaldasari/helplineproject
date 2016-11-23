package org.vishal.helplineapp;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.util.Log;


/**
 * Created by Vishal on 10/24/2016.
 */


public class TService extends Service {


    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    private CallBr br_call;




    @Override
    public Binder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("service", "destroy");
        NotificationHelper.destroyNotification(getApplicationContext(),NotificationHelper.ONGOING_NOTIFICATION);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_OUT);
        filter.addAction(ACTION_IN);
        this.br_call = new CallBr();
        this.registerReceiver(this.br_call, filter);

        NotificationHelper.fireNotification(getApplicationContext(),NotificationHelper.ONGOING_NOTIFICATION,getString(R.string.recording_service_active));

        return START_STICKY;
    }

}