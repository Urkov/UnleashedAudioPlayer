package org.openauto.unleashedaudioplayer;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebAppInterface {

    private Context context;

    WebAppInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void selectTrack(String str) {
        int albumid = Integer.parseInt(str);
        if (context instanceof UnleashedAudioPlayerPhoneActivity){
            ((UnleashedAudioPlayerPhoneActivity)context).webViewHandler.loadTrackPage(albumid);
        }
        if (context instanceof UnleashedAudioPlayerCarActivity){
            ((UnleashedAudioPlayerCarActivity)context).webViewHandler.loadTrackPage(albumid);
        }

    }

    @JavascriptInterface
    public void goToAlbums(String str) {
        if (context instanceof UnleashedAudioPlayerPhoneActivity){
            ((UnleashedAudioPlayerPhoneActivity)context).webViewHandler.loadAlbumPage();
        }
        if (context instanceof UnleashedAudioPlayerCarActivity){
            ((UnleashedAudioPlayerCarActivity)context).webViewHandler.loadAlbumPage();
        }
    }


}