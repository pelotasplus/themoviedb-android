package pl.pelotasplus.themoviedb.demo.api;

import retrofit2.http.GET;
import rx.Observable;

public interface TheMovieDatabaseAPI {
    @GET("/3/discover/movie")
    Observable<MoviesResponse> discoverMovie();
}
