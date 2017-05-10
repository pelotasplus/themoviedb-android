package pl.pelotasplus.themoviedb.demo.screens.main;

import java.util.ArrayList;

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

    MainPresenter(TheMovieDatabaseAPI theMovieDatabaseAPI) {
        this.theMovieDatabaseAPI = theMovieDatabaseAPI;
    }

    @Override
    public void bind(MainContract.View view, ArrayList<Movie> savedInstanceState) {
        Observable<Movie> movieObservable;

        if (savedInstanceState == null) {
            movieObservable = theMovieDatabaseAPI
                    .discoverMovie(
                            "2017-05-08",
                            1
                    )
                    .flatMap(moviesResponse -> Observable.from(moviesResponse.getResults()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            movieObservable = Observable
                    .from(savedInstanceState);
        }

        Subscription sub = movieObservable
                .subscribe(
                        view::addMovie,
                        Throwable::printStackTrace
                );
        compositeSubscription.add(sub);
    }

    @Override
    public void unbind() {
        compositeSubscription.clear();
    }
}
