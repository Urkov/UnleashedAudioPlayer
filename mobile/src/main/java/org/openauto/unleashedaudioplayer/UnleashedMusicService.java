package org.openauto.unleashedaudioplayer;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UnleashedMusicService extends MediaBrowserServiceCompat {

    private MediaSessionCompat mSession;

    @Override
    public void onCreate() {
        super.onCreate();

        mSession = new MediaSessionCompat(this, "UnleashedMusicService");
        setSessionToken(mSession.getSessionToken());
        mSession.setCallback(new MediaSessionCallback());
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        PlaybackStateCompat state = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_STOP |
                                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID | PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1, SystemClock.elapsedRealtime())
                .build();
        mSession.setPlaybackState(state);
    }

    @Override
    public void onDestroy() {
        mSession.release();
    }

    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName,
                                 int clientUid,
                                 Bundle rootHints) {
        return new BrowserRoot("root", null);
    }

    @Override
    public void onLoadChildren(@NonNull final String parentMediaId,
                               @NonNull final Result<List<MediaItem>> result) {

        List<MediaItem> mediaItems = new ArrayList<>();
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder().setTitle("Use OEM Tab").setSubtitle("to control").setDescription("Description").setMediaId("0123").build();
        mediaItems.add(new MediaItem(mediaDescriptionCompat, MediaItem.FLAG_PLAYABLE));
        result.sendResult(mediaItems);
    }

    private final class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            try{
                ActivityAccessHelper.getInstance().activity.webViewHandler.playPause();
            } catch(Exception e){
                Log.i(this.getClass().getSimpleName(), e.getMessage());
            }
        }

        @Override
        public void onSkipToQueueItem(long queueId) {
        }

        @Override
        public void onSeekTo(long position) {
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
        }

        @Override
        public void onPause() {
            try{
                ActivityAccessHelper.getInstance().activity.webViewHandler.playPause();
            } catch(Exception e){
                Log.i(this.getClass().getSimpleName(), e.getMessage());
            }
        }

        @Override
        public void onStop() {
            try{
                ActivityAccessHelper.getInstance().activity.webViewHandler.playPause();
            } catch(Exception e){
                Log.i(this.getClass().getSimpleName(), e.getMessage());
            }
        }

        @Override
        public void onSkipToNext() {
            try{
                ActivityAccessHelper.getInstance().activity.webViewHandler.nextTrack();
            } catch(Exception e){
                Log.i(this.getClass().getSimpleName(), e.getMessage());
            }
        }

        @Override
        public void onSkipToPrevious() {
            try{
                ActivityAccessHelper.getInstance().activity.webViewHandler.prevTrack();
            } catch(Exception e){
                Log.i(this.getClass().getSimpleName(), e.getMessage());
            }
        }

        @Override
        public void onCustomAction(String action, Bundle extras) {
        }

        @Override
        public void onPlayFromSearch(final String query, final Bundle extras) {
        }
    }
}
