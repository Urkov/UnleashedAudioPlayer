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

/**
 * This class provides a MediaBrowser through a service. It exposes the media library to a browsing
 * client, through the onGetRoot and onLoadChildren methods. It also creates a MediaSession and
 * exposes it through its MediaSession.Token, which allows the client to create a MediaController
 * that connects to and send control commands to the MediaSession remotely. This is useful for
 * user interfaces that need to interact with your media session, like Android Auto. You can
 * (should) also use the same service from your app's UI, which gives a seamless playback
 * experience to the user.
 * <p>
 * To implement a MediaBrowserService, you need to:
 * <p>
 * <ul>
 * <p>
 * <li> Extend {@link MediaBrowserServiceCompat}, implementing the media browsing
 * related methods {@link MediaBrowserServiceCompat#onGetRoot} and
 * {@link MediaBrowserServiceCompat#onLoadChildren};
 * <li> In onCreate, start a new {@link MediaSessionCompat} and notify its parent
 * with the session's token {@link MediaBrowserServiceCompat#setSessionToken};
 * <p>
 * <li> Set a callback on the {@link MediaSessionCompat#setCallback(MediaSessionCompat.Callback)}.
 * The callback will receive all the user's actions, like play, pause, etc;
 * <p>
 * <li> Handle all the actual music playing using any method your app prefers (for example,
 * {@link android.media.MediaPlayer})
 * <p>
 * <li> Update playbackState, "now playing" metadata and queue, using MediaSession proper methods
 * {@link MediaSessionCompat#setPlaybackState(android.support.v4.media.session.PlaybackStateCompat)}
 * {@link MediaSessionCompat#setMetadata(android.support.v4.media.MediaMetadataCompat)} and
 * {@link MediaSessionCompat#setQueue(List)})
 * <p>
 * <li> Declare and export the service in AndroidManifest with an intent receiver for the action
 * android.media.browse.MediaBrowserService
 * <p>
 * </ul>
 * <p>
 * To make your app compatible with Android Auto, you also need to:
 * <p>
 * <ul>
 * <p>
 * <li> Declare a meta-data tag in AndroidManifest.xml linking to a xml resource
 * with a &lt;automotiveApp&gt; root element. For a media app, this must include
 * an &lt;uses name="media"/&gt; element as a child.
 * For example, in AndroidManifest.xml:
 * &lt;meta-data android:name="com.google.android.gms.car.application"
 * android:resource="@xml/automotive_app_desc"/&gt;
 * And in res/values/automotive_app_desc.xml:
 * &lt;automotiveApp&gt;
 * &lt;uses name="media"/&gt;
 * &lt;/automotiveApp&gt;
 * <p>
 * </ul>
 */
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
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder().setTitle("Title").setSubtitle("Subtitle").setDescription("Description").setMediaId("0123").build();
        mediaItems.add(new MediaItem(mediaDescriptionCompat, MediaItem.FLAG_PLAYABLE));
        result.sendResult(mediaItems);
    }

    private final class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            Log.i("UnleashedMusicService", "onPlay");
        }

        @Override
        public void onSkipToQueueItem(long queueId) {
            Log.i("UnleashedMusicService", "onSkipToQueueItem");
        }

        @Override
        public void onSeekTo(long position) {
            Log.i("UnleashedMusicService", "onSeekTo");
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            Log.i("UnleashedMusicService", "onPlayFromMediaId");
        }

        @Override
        public void onPause() {
            Log.i("UnleashedMusicService", "onPause");
            try{
                ActivityAccessHelper.getInstance().activity.webViewHandler.playPause();
            } catch(Exception e){
                Log.i("UnleashedMusicService", e.getMessage());
            }
        }

        @Override
        public void onStop() {
            Log.i("UnleashedMusicService", "onStop");
        }

        @Override
        public void onSkipToNext() {
            Log.i("UnleashedMusicService", "onSkipToNext");
            try{
                ActivityAccessHelper.getInstance().activity.webViewHandler.nextTrack();
            } catch(Exception e){
                Log.i("UnleashedMusicService", e.getMessage());
            }
        }

        @Override
        public void onSkipToPrevious() {
            Log.i("UnleashedMusicService", "onSkipToNext");
            try{
                ActivityAccessHelper.getInstance().activity.webViewHandler.prevTrack();
            } catch(Exception e){
                Log.i("UnleashedMusicService", e.getMessage());
            }
        }

        @Override
        public void onCustomAction(String action, Bundle extras) {
            Log.i("UnleashedMusicService", "onCustomAction");
        }

        @Override
        public void onPlayFromSearch(final String query, final Bundle extras) {
            Log.i("UnleashedMusicService", "onPlayFromSearch");
        }
    }
}
