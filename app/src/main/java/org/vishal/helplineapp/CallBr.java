package org.vishal.helplineapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

/**
 * Created by Vishal on 11/21/2016.
 */
public class CallBr extends BroadcastReceiver {
    MediaRecorder recorder;
    File audiofile;
    private boolean recordstarted = false;
    Bundle bundle;
    String state;
    String inCall, outCall;
    public boolean wasRinging = false;
    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_IN)) {
            if ((bundle = intent.getExtras()) != null) {
                state = bundle.getString(TelephonyManager.EXTRA_STATE);
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    inCall = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    wasRinging = true;
                    Toast.makeText(context, "IN : " + inCall, Toast.LENGTH_LONG).show();
                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    if (wasRinging == true) {

                        NotificationHelper.fireNotification(context,NotificationHelper.ONGOING_NOTIFICATION, context.getString(R.string.recording_call_from)+ " " + inCall);

                        String out = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date());
                        File sampleDir = new File(Environment.getExternalStorageDirectory(), "/issuetrackerRecordings");
                        if (!sampleDir.exists()) {
                            sampleDir.mkdirs();
                        }
                        String file_name = "Call-" + out + "-" + inCall;
                        try {
                            audiofile = File.createTempFile(file_name, ".mp4", sampleDir);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        recorder = new MediaRecorder();
                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                        recorder.setOutputFile(audiofile.getAbsolutePath());
                        try {
                            recorder.prepare();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        recorder.start();
                        recordstarted = true;
                    }
                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    wasRinging = false;
                    if (recordstarted) {
                        recorder.stop();
                        NotificationHelper.fireNotification(context,NotificationHelper.ONGOING_NOTIFICATION, context.getString(R.string.recording_service_active));
                        NotificationHelper.fireNotification(context,NotificationHelper.ONETIME_NOTIFICATION, context.getString(R.string.recieved_call_from_new_issue) + " " + inCall,inCall);

                        recordstarted = false;
                    }
                }
            }
        } else if (intent.getAction().equals(ACTION_OUT)) {
            if ((bundle = intent.getExtras()) != null) {
                outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Toast.makeText(context, "OUT : " + outCall, Toast.LENGTH_LONG).show();
            }
        }
    }
}
