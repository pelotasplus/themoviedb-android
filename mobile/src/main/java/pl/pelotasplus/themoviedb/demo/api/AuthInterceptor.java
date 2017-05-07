package pl.pelotasplus.themoviedb.demo.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import pl.pelotasplus.themoviedb.demo.BuildConfig;

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build();
        Request newRequest = chain.request().newBuilder().url(url).build();
        return chain.proceed(newRequest);
    }
}