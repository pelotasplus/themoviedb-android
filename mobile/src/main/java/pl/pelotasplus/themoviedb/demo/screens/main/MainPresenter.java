package pl.pelotasplus.themoviedb.demo.screens.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.pelotasplus.themoviedb.demo.api.Movie;
import pl.pelotasplus.themoviedb.demo.api.TheMovieDatabaseAPI;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

class MainPresenter implements MainContract.Presenter {
    private final TheMovieDatabaseAPI theMovieDatabaseAPI;
    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private MainContract.View view;

    private int totalPages = Integer.MAX_VALUE;
    private int page = 1;
    private int year;
    private Scheduler subscribeOn;
    private Scheduler observeOn;
    private boolean isLoading = false;

    MainPresenter(TheMovieDatabaseAPI theMovieDatabaseAPI, Scheduler observeOn, Scheduler subscribeOn) {
        this.theMovieDatabaseAPI = theMovieDatabaseAPI;
        this.observeOn = observeOn;
        this.subscribeOn = subscribeOn;
    }

    @Override
    public void bind(MainContract.View view, ArrayList<Movie> savedInstanceState, int year, int page) {
        this.view = view;
        this.year = year;
        this.page = page;

        fetchMovies(savedInstanceState);
    }

    @Override
    public void unbind() {
        compositeSubscription.clear();

        this.view = null;
    }

    @Override
    public void yearPicked(int year) {
        this.year = year;

        resetMoviesAndState();

        fetchMovies(null);
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void filterClicked() {
        view.showDatePicker(year);
    }

    @Override
    public void refresh() {
        resetMoviesAndState();

        fetchMovies(null);
    }

    @Override
    public void loadMore() {
        fetchMovies(null);
    }

    private void resetMoviesAndState() {
        this.page = 1;
        this.totalPages = Integer.MAX_VALUE;
        view.clearMovies();
    }

    private void fetchMovies(ArrayList<Movie> savedInstanceState) {
        if (isLoading) return;
        if (page > totalPages) return;

        view.showYear(year);

        Observable<List<Movie>> movieObservable;

        if (savedInstanceState == null) {
            String dateFrom = String.format(Locale.getDefault(), "%d-01-01", year);
            String dateTo = String.format(Locale.getDefault(), "%d-12-31", year);

            movieObservable = theMovieDatabaseAPI
                    .discoverMovie(page, dateFrom, dateTo)
                    .flatMap(
                            moviesResponse -> {
                                page = moviesResponse.getPage() + 1;
                                totalPages = moviesResponse.getTotalPages();
                                return Observable.just(moviesResponse.getResults());
                            }
                    )
                    .subscribeOn(subscribeOn)
                    .observeOn(observeOn);
        } else {
            movieObservable = Observable
                    .just(savedInstanceState);
        }

        Subscription sub = movieObservable
                .doOnSubscribe(() -> {
                    isLoading = true;
                    view.showRefreshing();
                })
                .doOnUnsubscribe(() -> {
                    isLoading = false;
                })
                .subscribe(
                        movies -> {
                            view.addMovies(movies);
                            view.hideRefreshing();
                        },
                        view::showRefreshingError
                );
        compositeSubscription.add(sub);
    }
}