package pl.pelotasplus.themoviedb.demo.di;

import javax.inject.Singleton;

import dagger.Component;
import pl.pelotasplus.themoviedb.demo.api.TheMovieDatabaseAPI;

@Singleton
@Component(modules = {AndroidModule.class, AppModule.class, ApiModule.class})
public interface AppComponent {
    TheMovieDatabaseAPI getTheMovieDatabaseAPI();
}