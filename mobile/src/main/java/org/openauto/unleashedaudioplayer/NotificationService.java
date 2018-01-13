package org.openauto.unleashedaudioplayer;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.google.android.apps.auto.sdk.CarToast;
import com.google.android.apps.auto.sdk.notification.CarNotificationExtender;

import org.openauto.unleashedaudioplayer.entities.TrackModel;

public class NotificationService {

    public UnleashedAudioPlayerCarActivity activity;
    private Handler mHandler = new Handler();

    public NotificationService(UnleashedAudioPlayerCarActivity activity){
        this.activity = activity;

        //create channel for oreo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("UNLEASHED_AUDIO_NOTIFICATION_CHANNEL",
                    "UNLEASHED_AUDIO_NOTIFICATION_CHANNEL",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription("UnleashedAudioPlayer notification for Android Auto");
            NotificationManager mNotificationManager =
                    (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    public void showCarToast(String message, int length) {
        CarToast.makeText(activity, message, length).show();
    }

    public void showAudioNotification(TrackModel tm) {
        //the activity should only be sent when it is currently paused (e.g. in Maps view)
        if(activity.activityIsPaused){
            Bitmap bitmap = BitmapFactory.decodeFile(tm.getCoverArt());
            showNotification(tm.getArtist() + " - " + tm.getAlbum(), tm.getTitle(), bitmap);
        }
    }

    public void showNotification(String firstLine, String secondLine, Bitmap bitmap) {

        mHandler.postDelayed(() -> {
            Notification notification = new NotificationCompat.Builder(activity,
                    "UNLEASHED_AUDIO_NOTIFICATION_CHANNEL")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(firstLine)
                    .setContentText(secondLine)
                    .setAutoCancel(true)
                    .extend(new CarNotificationExtender.Builder()
                            .setTitle(firstLine)
                            .setSubtitle(secondLine)
                            .setActionIconResId(R.mipmap.ic_launcher)
                            .setThumbnail(bitmap)
                            .setShouldShowAsHeadsUp(true)
                            .build())
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.notify("activity", 1, notification);

            //Use this for a sound
            //CarNotificationSoundPlayer soundPlayer = new CarNotificationSoundPlayer(
            //        MainCarActivity.this, R.raw.bubble);
            //soundPlayer.play();

        }, 0);
    }

}
