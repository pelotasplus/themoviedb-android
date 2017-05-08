package pl.pelotasplus.themoviedb.demo.screens.details;

import pl.pelotasplus.themoviedb.demo.api.Movie;

interface DetailsContract {
    interface View {
        void showMovieDetails(Movie movie);
    }

    interface Presenter {
        void unbind();

        void bind(View view, Movie movie);
    }
}