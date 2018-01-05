package org.openauto.unleashedaudioplayer;

import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MediaSessionHandler {

    public void initMediaSession(final UnleashedAudioPlayerCarActivity activity){

        //Create audio session
        // Set a callback object to handle play control requests, which
        // implements MediaSession.Callback
        MediaSession mSession = new MediaSession(activity, "UNLEASHED_AUDIO_PLAYER_SESSION");
        //setSessionToken(mSession.getSessionToken());

        mSession.setCallback(new MediaSession.Callback() {

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
        mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

        PlaybackState state = new PlaybackState.Builder()
                .setActions(
                        PlaybackState.ACTION_PLAY | PlaybackState.ACTION_PLAY_PAUSE | PlaybackState.ACTION_STOP |
                                PlaybackState.ACTION_PLAY_FROM_MEDIA_ID | PlaybackState.ACTION_PAUSE |
                                PlaybackState.ACTION_SKIP_TO_NEXT | PlaybackState.ACTION_SKIP_TO_PREVIOUS)
                .setState(PlaybackState.STATE_PLAYING, 0, 1, SystemClock.elapsedRealtime())
                .build();
        mSession.setQueueTitle("HTML5Audio");
        mSession.setPlaybackState(state);
        mSession.setActive(true);

    }


}
