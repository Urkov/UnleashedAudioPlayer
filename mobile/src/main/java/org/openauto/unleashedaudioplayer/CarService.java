package org.openauto.unleashedaudioplayer;

import com.google.android.apps.auto.sdk.CarActivity;
import com.google.android.apps.auto.sdk.CarActivityService;

public class CarService extends CarActivityService {
    public Class<? extends CarActivity> getCarActivity() {
        return UnleashedAudioPlayerCarActivity.class;
    }
}
