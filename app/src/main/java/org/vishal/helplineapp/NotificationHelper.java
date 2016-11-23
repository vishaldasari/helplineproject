package org.vishal.helplineapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Vishal on 11/22/2016.
 */

public class NotificationHelper {

    private Context m_context;
    public static final int ONGOING_NOTIFICATION = -99;
    public static final int ONETIME_NOTIFICATION = -100;

    public NotificationHelper(Context context) {
        m_context = context;
    }


    public static void fireNotification (Context m_context, int type, String message, String phoneNumber) {
        boolean ongoing = (type == NotificationHelper.ONGOING_NOTIFICATION) ? true : false;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(m_context)
                        .setSmallIcon(R.drawable.common_google_signin_btn_text_light_focused)
                        .setContentTitle(m_context.getString(R.string.app_name))
                        .setOngoing(ongoing)
                        .setContentText(message);
        Intent resultIntent = new Intent(m_context, IssueViewActivity.class);
        if(phoneNumber != null) {
            resultIntent.putExtra("INCOMINGPHONENUMBER", phoneNumber);
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
            mBuilder.setContentTitle(m_context.getString(R.string.recording_ended));
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(m_context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(IssueViewActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
        }

        NotificationManager mNotificationManager =
                (NotificationManager) m_context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        Notification notification = mBuilder.build();
        if (!ongoing) {
            notification.flags = Notification.FLAG_AUTO_CANCEL;
        }
        mNotificationManager.notify(type, notification);



    }

    public static void fireNotification (Context m_context, int type, String message) {
        fireNotification(m_context,type,message,null);
    }
    public static void destroyNotification(Context m_context, int id) {
        NotificationManager mNotificationManager =
                (NotificationManager) m_context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(-99);
    }








}
