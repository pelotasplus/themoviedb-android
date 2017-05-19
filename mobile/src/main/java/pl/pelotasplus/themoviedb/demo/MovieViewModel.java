package pl.pelotasplus.themoviedb.demo;

import android.databinding.BaseObservable;

import pl.pelotasplus.themoviedb.demo.api.Movie;

public class MovieViewModel extends BaseObservable {
    private Movie movie;

    public MovieViewModel(Movie movie) {
        this.movie = movie;
    }

    public String getTitle() {
        return movie.title();
    }

    public String getOverview() {
        return movie.overview();
    }

    public String getReleaseDate() {
        return movie.releaseDate();
    }

    public String getCover() {
        return "https://image.tmdb.org/t/p/w500/" + movie.posterPath();
    }
}