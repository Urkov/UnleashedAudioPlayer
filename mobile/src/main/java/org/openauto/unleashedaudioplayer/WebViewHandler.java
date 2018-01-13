package org.openauto.unleashedaudioplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import org.apache.commons.text.StringEscapeUtils;
import org.openauto.unleashedaudioplayer.entities.AlbumModel;
import org.openauto.unleashedaudioplayer.entities.TrackModel;
import org.openauto.unleashedaudioplayer.entities.WebradioModel;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class WebViewHandler {

    public List<AlbumModel> albums;
    public List<TrackModel> tracks;
    public List<WebradioModel> webradio;

    private WebView webview;
    private Context context;

    @SuppressLint("SetJavaScriptEnabled")
    public WebViewHandler(Context context, WebView webview){
        this.webview = webview;
        this.context = context;
        albums = MediaStoreHandler.getListOfAlbums(context);
        webradio = MediaStoreHandler.getWebradioList(context);

        //load web view
        WebSettings wbset=webview.getSettings();
        wbset.setJavaScriptEnabled(true);
        wbset.setAllowContentAccess(true);
        wbset.setAllowFileAccess(true);
        wbset.setAllowFileAccessFromFileURLs(true);
        wbset.setDomStorageEnabled(true);
        wbset.setDatabaseEnabled(true);
        webview.setScrollbarFadingEnabled(false);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient());
        webview.addJavascriptInterface(new WebAppInterface(context), "Android");
        webview.loadUrl("file:///android_asset/index.html");

    }

    public void showToast(String msg){
        webview.post(() -> webview.loadUrl("javascript:showToast(\"" + StringEscapeUtils.escapeEcmaScript(msg) + "\")"));
    }
    public void stopPlaying(){
        webview.post(() -> webview.loadUrl("javascript:stopPlaying()"));
    }
    public void nextTrack(){
        webview.post(() -> webview.loadUrl("javascript:playNextTrack()"));
    }
    public void prevTrack(){
        webview.post(() -> webview.loadUrl("javascript:playPrevTrack()"));
    }
    public void pauseEvent(){
        webview.post(() -> webview.loadUrl("javascript:pauseEvent()"));
    }
    public void playEvent(){
        webview.post(() -> webview.loadUrl("javascript:playEvent()"));
    }
    public void stopEvent(){
        webview.post(() -> webview.loadUrl("javascript:stopEvent()"));
    }
    public void seekEvent(long position){
        webview.post(() -> webview.loadUrl("javascript:seekEvent("+position+")"));
    }
    public void customEvent(String action){
        webview.post(() -> webview.loadUrl("javascript:customEvent("+action+")"));
    }

    public void loadWebradio(){
        Gson g = new Gson();
        webview.post(() -> webview.loadUrl("javascript:createRadioGrid(\"" + StringEscapeUtils.escapeEcmaScript(g.toJson(webradio)) + "\")"));
        showToast("loadWebradio");
    }

    public void loadAlbumPage(){
        Gson g = new Gson();
        webview.post(() -> webview.loadUrl("javascript:createAlbumGrid(\"" + StringEscapeUtils.escapeEcmaScript(g.toJson(albums)) + "\")"));
        showToast("loadAlbumPage");
    }

    public void loadTrackPage(int albumIdToLoad){
        AlbumModel albumToLoad = null;
        for(AlbumModel m : albums){
            if(m.getId() == albumIdToLoad){
                albumToLoad = m;
            }
        }
        List<TrackModel> songs = MediaStoreHandler.getTracksForAlbum(context, albumToLoad);
        this.tracks = songs;
        Gson g = new Gson();
        webview.post(() -> webview.loadUrl("javascript:createTrackGrid(\"" +  StringEscapeUtils.escapeEcmaScript(g.toJson(songs)) + "\")"));
    }

}
