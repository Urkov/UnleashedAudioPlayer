package org.openauto.unleashedaudioplayer;


import android.app.Notification;
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

    private static final String NOTIFICATION_CHANNEL_ID = "car";
    private static final int TEST_NOTIFICATION_ID = 1;

    public UnleashedAudioPlayerCarActivity activity;
    private Handler mHandler = new Handler();

    public NotificationService(UnleashedAudioPlayerCarActivity activity){
        this.activity = activity;
    }

    public void showCarToast(String message, int length) {
        CarToast.makeText(activity, message, length).show();
    }

    public void showAudioNotification(TrackModel tm) {
        Bitmap bitmap = BitmapFactory.decodeFile(tm.getCoverArt());

        showNotification(tm.getArtist(), tm.getTitle(), bitmap);
    }

    public void showNotification(String firstLine, String secondLine, Bitmap bitmap) {
        mHandler.postDelayed(() -> {
            Notification notification = new NotificationCompat.Builder(activity,
                    NOTIFICATION_CHANNEL_ID)
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
            notificationManager.notify("activity", TEST_NOTIFICATION_ID, notification);

            //Use this for a sound
            //CarNotificationSoundPlayer soundPlayer = new CarNotificationSoundPlayer(
            //        MainCarActivity.this, R.raw.bubble);
            //soundPlayer.play();

        }, 0);
    }

}
