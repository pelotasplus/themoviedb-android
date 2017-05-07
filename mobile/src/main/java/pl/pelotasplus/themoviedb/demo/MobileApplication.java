package pl.pelotasplus.themoviedb.demo;

import android.app.Application;

import pl.pelotasplus.themoviedb.demo.di.AndroidModule;
import pl.pelotasplus.themoviedb.demo.di.ApiModule;
import pl.pelotasplus.themoviedb.demo.di.AppComponent;
import pl.pelotasplus.themoviedb.demo.di.AppModule;
import pl.pelotasplus.themoviedb.demo.di.DaggerAppComponent;

public class MobileApplication extends Application {
    AppComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = buildApplicationComponent();
    }

    AppComponent buildApplicationComponent() {
        return DaggerAppComponent
                .builder()
                .androidModule(new AndroidModule(this))
                .appModule(new AppModule())
                .apiModule(new ApiModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return applicationComponent;
    }
}
