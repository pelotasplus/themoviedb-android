package pl.pelotasplus.themoviedb.demo.screens.main;

import java.util.ArrayList;

import pl.pelotasplus.themoviedb.demo.api.Movie;

interface MainContract {
    interface View {
        void addMovie(Movie movie);
    }

    interface Presenter {
        void bind(View view, ArrayList<Movie> savedInstanceState);

        void unbind();
    }
}