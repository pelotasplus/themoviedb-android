package pl.pelotasplus.themoviedb.demo.di;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.pelotasplus.themoviedb.demo.BuildConfig;
import pl.pelotasplus.themoviedb.demo.api.AuthInterceptor;
import pl.pelotasplus.themoviedb.demo.api.TheMovieDatabaseAPI;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class ApiModule {
    private static final String INTERCEPTOR_LOGGING = "interceptor_logging";
    private static final String INTERCEPTOR_AUTH = "interceptor_auth";

    @Provides
    @Named(INTERCEPTOR_LOGGING)
    Interceptor provideLogsInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    @Provides
    @Named(INTERCEPTOR_AUTH)
    Interceptor provideAuthInterceptor() {
        return new AuthInterceptor();
    }

    @Provides
    OkHttpClient provideOkHttpClient(
            @Named(INTERCEPTOR_LOGGING) Interceptor logsInterceptor,
            @Named(INTERCEPTOR_AUTH) Interceptor authInterceptor
    ) {
        return new OkHttpClient
                .Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(logsInterceptor)
                .build();
    }

    @Provides
    TheMovieDatabaseAPI provideTheMovieDatabaseAPI(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.TMDB_API_ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(TheMovieDatabaseAPI.class);
    }
}