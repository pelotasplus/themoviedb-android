package pl.pelotasplus.themoviedb.demo.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface TheMovieDatabaseAPI {
    @GET("/3/discover/movie?sort_by=primary_release_date.desc")
    Observable<MoviesResponse> discoverMovie(
            @Query("primary_release_date.lte") String release_date_lte,
            @Query("page") int page
    );
}
