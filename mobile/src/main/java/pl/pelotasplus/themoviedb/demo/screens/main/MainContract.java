package pl.pelotasplus.themoviedb.demo.screens.main;

import java.util.ArrayList;
import java.util.List;

import pl.pelotasplus.themoviedb.demo.api.Movie;

interface MainContract {
    interface View {
        void showYear(int year);

        void showDatePicker(int year);

        void hideRefreshing();

        void showRefreshingError(Throwable throwable);

        void showRefreshing();

        void addMovies(List<Movie> movies);

        void clearMovies();
    }

    interface Presenter {
        void bind(View view, ArrayList<Movie> savedInstanceState, int year, int page);

        void unbind();

        void yearPicked(int year);

        int getYear();

        void filterClicked();

        void refresh();

        void loadMore();

        int getPage();
    }
}