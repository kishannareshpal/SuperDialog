package com.kishannareshpal.superdialog;

import android.support.annotation.StyleRes;

public class SuperDialogConfiguration {
    static @StyleRes int SUPERDIALOG_THEME = R.style.SuperDialogTheme_Light;

    public static void setTheme(@StyleRes int theme){
        SUPERDIALOG_THEME = theme;
    }
    public static @StyleRes int getTheme(){
        return SUPERDIALOG_THEME;
    }
}
