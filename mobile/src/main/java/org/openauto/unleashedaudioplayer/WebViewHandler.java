package org.openauto.unleashedaudioplayer;

import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

public class WebViewHandler {

    private ArrayList<AlbumModel> albums;
    private String jsonToPass;
    private WebView webview;
    private Context context;

    public WebViewHandler(Context context, WebView webview){
        this.webview = webview;
        this.context = context;
        albums = MediaStoreHandler.getListOfAlbums(context);

        //load web view
        WebSettings wbset=webview.getSettings();
        wbset.setJavaScriptEnabled(true);
        wbset.setAllowContentAccess(true);
        wbset.setAllowFileAccess(true);
        wbset.setAllowFileAccessFromFileURLs(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient());
        webview.addJavascriptInterface(new WebAppInterface(context), "Android");
        webview.loadUrl("file:///android_asset/index.html");

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
    public void playPause(){
        webview.post(() -> webview.loadUrl("javascript:playPause()"));
    }


    public void loadAlbumPage(){
        Gson g = new Gson();
        final String jsonAlbums = g.toJson(albums);
        jsonToPass = jsonAlbums;
        webview.post(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl("javascript:createAlbumGrid(\"" + StringEscapeUtils.escapeEcmaScript(jsonToPass) + "\")");
            }
        });
    }

    public void loadTrackPage(int albumIdToLoad){
        AlbumModel albumToLoad = null;
        for(AlbumModel m : albums){
            if(m.getId() == albumIdToLoad){
                albumToLoad = m;
            }
        }
        List<TrackModel> songs = MediaStoreHandler.getTracksForAlbum(context, albumToLoad);
        Gson g = new Gson();
        final String jsonSongs = g.toJson(songs);
        jsonToPass = jsonSongs;

        webview.post(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl("javascript:createTrackGrid(\"" +  StringEscapeUtils.escapeEcmaScript(jsonToPass) + "\")");
            }
        });
    }

}
