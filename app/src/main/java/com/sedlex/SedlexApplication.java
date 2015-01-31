package com.sedlex;

import android.app.Application;
import android.content.Context;

public class SedlexApplication extends Application {

    private static SedlexApplication mInstance;
    private static Context mAppContext;

    public SedlexApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        this.setAppContext(getApplicationContext());
    }

    public static SedlexApplication getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }
}
