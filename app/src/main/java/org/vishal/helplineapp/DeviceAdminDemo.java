package org.vishal.helplineapp;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Vishal on 10/24/2016.
 */
public class DeviceAdminDemo extends DeviceAdminReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        context.stopService(new Intent(context, TService.class));
        Intent myIntent = new Intent(context, TService.class);
        context.startService(myIntent);
    }

    public void onEnabled(Context context, Intent intent) {
    };

    public void onDisabled(Context context, Intent intent) {
    };
}