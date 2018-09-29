package com.example.user.trendy.Payu_Utility;

import android.app.Application;

public class MyApplication extends Application {
    private AppEnvironment appEnvironment;
    @Override
    public void onCreate() {
        super.onCreate();

        appEnvironment= AppEnvironment.SANDBOX;
    }

    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }

    public void setAppEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }

}
