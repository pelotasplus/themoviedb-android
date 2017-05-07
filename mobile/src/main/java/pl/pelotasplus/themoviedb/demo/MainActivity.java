package pl.pelotasplus.themoviedb.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import pl.pelotasplus.themoviedb.demo.di.AppComponent;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    private AppComponent getAppComponent() {
        return ((MobileApplication) getApplication()).getAppComponent();
    }
}
