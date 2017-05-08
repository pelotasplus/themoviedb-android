package pl.pelotasplus.themoviedb.demo;

import pl.pelotasplus.themoviedb.demo.api.Movie;

interface MainContract {
    interface View {
        void addMovie(Movie movie);
    }

    interface Presenter {
        void bind(View view);

        void unbind();
    }
}