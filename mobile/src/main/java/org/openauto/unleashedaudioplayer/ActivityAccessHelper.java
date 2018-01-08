package org.openauto.unleashedaudioplayer;

public final class ActivityAccessHelper {

    public UnleashedAudioPlayerCarActivity activity;
    private static volatile ActivityAccessHelper instance = null;

    private ActivityAccessHelper() {}

    public static ActivityAccessHelper getInstance() {
        if (instance == null) {
            synchronized(ActivityAccessHelper.class) {
                if (instance == null) {
                    instance = new ActivityAccessHelper();
                }
            }
        }
        return instance;
    }
}