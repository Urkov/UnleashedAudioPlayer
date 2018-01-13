package org.openauto.unleashedaudioplayer;

import android.content.Context;
import android.webkit.JavascriptInterface;

import org.openauto.unleashedaudioplayer.entities.TrackModel;

public class WebAppInterface {

    private Context context;

    WebAppInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void showPlayNotification(String str) {

        if (context instanceof UnleashedAudioPlayerPhoneActivity){
            //NYI
        }
        if (context instanceof UnleashedAudioPlayerCarActivity){
            TrackModel playedTrack = null;
            for(TrackModel m : ((UnleashedAudioPlayerCarActivity)context).webViewHandler.tracks){
                if(m.getId() == Integer.parseInt(str)){
                    playedTrack = m;
                }
            }
            if(playedTrack != null){
                ((UnleashedAudioPlayerCarActivity)context).notificationService.showAudioNotification(playedTrack);
            }
        }

    }

    @JavascriptInterface
    public void loadTracks(String str) {
        int albumid = Integer.parseInt(str);
        if (context instanceof UnleashedAudioPlayerPhoneActivity){
            ((UnleashedAudioPlayerPhoneActivity)context).webViewHandler.loadTrackPage(albumid);
        }
        if (context instanceof UnleashedAudioPlayerCarActivity){
            ((UnleashedAudioPlayerCarActivity)context).webViewHandler.loadTrackPage(albumid);
        }

    }

    @JavascriptInterface
    public void loadAlbums(String str) {
        if (context instanceof UnleashedAudioPlayerPhoneActivity){
            ((UnleashedAudioPlayerPhoneActivity)context).webViewHandler.loadAlbumPage();
        }
        if (context instanceof UnleashedAudioPlayerCarActivity){
            ((UnleashedAudioPlayerCarActivity)context).webViewHandler.loadAlbumPage();
        }
    }

    @JavascriptInterface
    public void loadWebradio(String str) {
        if (context instanceof UnleashedAudioPlayerPhoneActivity){
            ((UnleashedAudioPlayerPhoneActivity)context).webViewHandler.loadWebradio();
        }
        if (context instanceof UnleashedAudioPlayerCarActivity){
            ((UnleashedAudioPlayerCarActivity)context).webViewHandler.loadWebradio();
        }
    }


}