package org.openauto.unleashedaudioplayer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.webkit.WebView;

import com.google.android.apps.auto.sdk.CarActivity;

public class UnleashedAudioPlayerCarActivity extends CarActivity {

    public WebViewHandler webViewHandler;
    private static final String CURRENT_FRAGMENT_KEY = "app_current_fragment";
    private String mCurrentFragmentTag;
    public NotificationService notificationService;

    @Override
    public void onDestroy() {
        //Stop playing when disconnecting phone
        if(webViewHandler != null){
            webViewHandler.stopPlaying();
        }
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle bundle) {

        //set the activity for shared access from music service
        ActivityAccessHelper.getInstance().activity = this;

        this.notificationService = new NotificationService(this);

        setTheme(R.style.AppTheme_Car);
        super.onCreate(bundle);
        setContentView(R.layout.activity_car_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        PlayerFragment playerFragment = new PlayerFragment();

        //Add fragments
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, playerFragment, PlayerFragment.TAG)
                .detach(playerFragment)
                .commitNow();

        String initialFragmentTag = PlayerFragment.TAG;

        if (bundle != null && bundle.containsKey(CURRENT_FRAGMENT_KEY)) {
            initialFragmentTag = bundle.getString(CURRENT_FRAGMENT_KEY);
        }
        switchToFragment(initialFragmentTag);

        //Build main menu
        //MainMenuHandler.buildMainMenu(this);

        //Status bar controller
        getCarUiController().getMenuController().hideMenuButton();
        getCarUiController().getStatusBarController().hideMicButton();
        getCarUiController().getStatusBarController().hideTitle();
        getCarUiController().getStatusBarController().hideAppHeader();
        getCarUiController().getStatusBarController().setAppBarAlpha(0f);

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks,
                false);


    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(CURRENT_FRAGMENT_KEY, mCurrentFragmentTag);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        switchToFragment(mCurrentFragmentTag);
    }

    public void switchToFragment(String tag) {
        if (tag.equals(mCurrentFragmentTag)) {
            return;
        }
        FragmentManager manager = getSupportFragmentManager();
        Fragment currentFragment = mCurrentFragmentTag == null ? null : manager.findFragmentByTag(mCurrentFragmentTag);
        Fragment newFragment = manager.findFragmentByTag(tag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            transaction.detach(currentFragment);
        }
        transaction.attach(newFragment);
        transaction.commit();
        mCurrentFragmentTag = tag;
    }

    private final FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycleCallbacks
            = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentStarted(FragmentManager fm, Fragment f) {
            updateStatusBarTitle();
            updateFragmentContent(f);
        }
    };

    public void updateStatusBarTitle() {
        CarFragment fragment = (CarFragment) getSupportFragmentManager().findFragmentByTag(mCurrentFragmentTag);
        getCarUiController().getStatusBarController().setTitle(fragment.getTitle());
    }

    public void updateFragmentContent(Fragment fragment) {
        if(fragment instanceof PlayerFragment){
            updateBrowserFragment(fragment);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void updateBrowserFragment(Fragment fragment) {

        final WebView wbb = (WebView)findViewById(R.id.webview_component);
        if(webViewHandler == null){
            webViewHandler = new WebViewHandler(this, wbb);
        }

        webViewHandler.showToast("updateBrowserFragment");

    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        webViewHandler.showToast(keyEvent.toString());
        return super.onKeyDown(i, keyEvent);
    }

    @Override
    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        webViewHandler.showToast(keyEvent.toString());
        return super.onKeyLongPress(i, keyEvent);
    }

    @Override
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        webViewHandler.showToast(keyEvent.toString());
        return super.onKeyUp(i, keyEvent);
    }

}
