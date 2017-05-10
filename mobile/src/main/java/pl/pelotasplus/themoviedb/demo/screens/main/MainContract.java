package pl.pelotasplus.themoviedb.demo.screens.main;

import java.util.ArrayList;
import java.util.List;

import pl.pelotasplus.themoviedb.demo.api.Movie;

interface MainContract {
    interface View {
        void showYear(int year);

        void setMovies(List<Movie> movies);

        void showDatePicker(int year);
    }

    interface Presenter {
        void bind(View view, ArrayList<Movie> savedInstanceState, int year);

        void unbind();

        void yearPicked(int year);

        int getYear();

        void filterClicked();
    }
}