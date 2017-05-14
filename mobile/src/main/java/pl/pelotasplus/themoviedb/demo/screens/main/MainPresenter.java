package pl.pelotasplus.themoviedb.demo.screens.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.pelotasplus.themoviedb.demo.api.Movie;
import pl.pelotasplus.themoviedb.demo.api.TheMovieDatabaseAPI;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

class MainPresenter implements MainContract.Presenter {
    private final TheMovieDatabaseAPI theMovieDatabaseAPI;
    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private MainContract.View view;

    private int page = 1;
    private int year;

    MainPresenter(TheMovieDatabaseAPI theMovieDatabaseAPI) {
        this.theMovieDatabaseAPI = theMovieDatabaseAPI;
    }

    @Override
    public void bind(MainContract.View view, ArrayList<Movie> savedInstanceState, int year) {
        this.view = view;
        this.year = year;

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

        fetchMovies(null);
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void filterClicked() {
        view.showDatePicker(year);
    }

    @Override
    public void refresh() {
        fetchMovies(null);
    }

    private void fetchMovies(ArrayList<Movie> savedInstanceState) {
        view.showYear(year);

        Observable<List<Movie>> movieObservable;

        if (savedInstanceState == null) {
            String dateFrom = String.format(Locale.getDefault(), "%d-01-01", year);
            String dateTo = String.format(Locale.getDefault(), "%d-12-31", year);

            movieObservable = theMovieDatabaseAPI
                    .discoverMovie(page, dateFrom, dateTo)
                    .flatMap(
                            moviesResponse ->
                                    Observable.just(moviesResponse.getResults())
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } else {
            movieObservable = Observable
                    .just(savedInstanceState);
        }

        Subscription sub = movieObservable
                .doOnSubscribe(() -> view.showRefreshing())
                .doOnUnsubscribe(() -> view.hideRefreshing())
                .subscribe(
                        view::setMovies,
                        view::showRefreshingError
                );
        compositeSubscription.add(sub);
    }
}