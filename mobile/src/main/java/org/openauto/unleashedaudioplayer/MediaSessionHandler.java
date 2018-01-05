package org.openauto.unleashedaudioplayer;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MediaSessionHandler {

    public MediaSessionCompat mSession;

    public void initMediaSession(final UnleashedAudioPlayerCarActivity activity){

        //Create audio session
        // Set a callback object to handle play control requests, which
        // implements MediaSession.Callback
        mSession = new MediaSessionCompat(activity, "org.openauto.unleashedaudioplayer");
        MediaSessionCompat.Token sessionToken = mSession.getSessionToken();
        Log.i("SessionToken", sessionToken.toString());

        mSession.setCallback(new MediaSessionCompat.Callback() {

            @Override
            public void onCommand(@NonNull String command, @Nullable Bundle args, @Nullable ResultReceiver cb) {
                Log.d("onCommand", command);
                super.onCommand(command, args, cb);
            }

            @Override
            public void onPlay() {
                activity.webViewHandler.playPause();
                super.onPlay();
            }

            @Override
            public void onPause() {
                activity.webViewHandler.playPause();
                super.onPause();
            }

            @Override
            public void onSkipToNext() {
                activity.webViewHandler.nextTrack();
                super.onSkipToNext();
            }

            @Override
            public void onSkipToPrevious() {
                activity.webViewHandler.prevTrack();
                super.onSkipToPrevious();
            }

            @Override
            public void onStop() {
                super.onStop();
            }
        });
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        PlaybackStateCompat state = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_STOP |
                                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID | PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1, SystemClock.elapsedRealtime())
                .build();


        List<MediaSessionCompat.QueueItem> queueItemList = new ArrayList<>();
        MediaDescriptionCompat md = new MediaDescriptionCompat.Builder().setMediaId("HTML5Audio").build();
        queueItemList.add(new MediaSessionCompat.QueueItem(md, 34234));
        mSession.setQueue(queueItemList);
        mSession.setQueueTitle("HTML5Audio");
        mSession.setPlaybackState(state);
        mSession.setActive(true);

    }


}
