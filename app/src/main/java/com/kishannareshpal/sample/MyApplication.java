package com.kishannareshpal.sample;

import android.app.Application;

import com.kishannareshpal.superdialog.R;
import com.kishannareshpal.superdialog.SuperDialogConfiguration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SuperDialogConfiguration.setTheme(R.style.SuperDialogTheme_Night);
    }
}
