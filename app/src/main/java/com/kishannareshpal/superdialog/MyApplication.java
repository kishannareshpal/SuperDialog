package com.kishannareshpal.superdialog;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SuperDialogConfiguration.setTheme(R.style.SuperDialogTheme_Night);
    }
}
