package pl.pelotasplus.themoviedb.demo.screens.main

import pl.pelotasplus.themoviedb.demo.api.Movie
import pl.pelotasplus.themoviedb.demo.api.MoviesResponse
import pl.pelotasplus.themoviedb.demo.api.TheMovieDatabaseAPI
import rx.Observable
import rx.schedulers.TestScheduler
import spock.lang.Specification

class MainPresenterSpec extends Specification {
    MainContract.View view = Mock();
    TheMovieDatabaseAPI theMovieDatabaseAPI = Mock()
    def year = 2017
    def subscribeOn = new TestScheduler()
    def observeOn = new TestScheduler()

    def moviesList = [Mock(Movie), Mock(Movie), Mock(Movie)]

    MainPresenter presenter

    def "setup"() {
        presenter = new MainPresenter(theMovieDatabaseAPI, observeOn, subscribeOn)
    }

    def "should keep view reference on bind"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.empty()

        when:
        presenter.bind(view, null, year)

        then:
        presenter.@view == view
    }

    def "should keep year reference on bind"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.empty()

        when:
        presenter.bind(view, null, year)

        then:
        presenter.@year == year
    }

    def "should fetch new movies on bind"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.just(
                new MoviesResponse(
                        results: moviesList
                )
        )

        when:
        presenter.bind(view, null, year)

        and:
        subscribeOn.triggerActions()
        observeOn.triggerActions()

        then:
        1 * view.setMovies(moviesList)
    }

    def "should restore saved movies on bind"() {
        when:
        presenter.bind(view, moviesList, year)

        then:
        1 * view.setMovies(moviesList)
    }

    def "should clean subscriptions on unbind"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.just(
                new MoviesResponse(
                        results: moviesList
                )
        )

        when:
        presenter.bind(view, null, year)

        and:
        subscribeOn.triggerActions()

        then:
        assert presenter.compositeSubscription.hasSubscriptions()

        when:
        presenter.unbind()

        then:
        assert !presenter.compositeSubscription.hasSubscriptions()
    }

    def "should remove view reference on unbind"() {
        given:
        presenter.bind(view, moviesList, year)

        when:
        presenter.unbind()

        then:
        !presenter.@view
    }

    def "should update year reference on year change"() {
        given:
        presenter.@view = view
        presenter.year = 2000
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.empty()

        when:
        presenter.yearPicked(1000)

        then:
        presenter.year == 1000
    }

    def "should fetch movies on year change"() {
        given:
        presenter.@view = view

        when:
        presenter.yearPicked(1000)

        and:
        subscribeOn.triggerActions()

        then:
        1 * theMovieDatabaseAPI.discoverMovie(_, _, _) >> { args ->
            assert args[1] == "1000-01-01"
            assert args[2] == "1000-12-31"
            Observable.empty()
        }
    }

    def "should return year"() {
        when:
        presenter.@year = 1981

        then:
        presenter.getYear() == 1981
    }

    def "should show date picker"() {
        given:
        presenter.@view = view
        presenter.year = 1981

        when:
        presenter.filterClicked()

        then:
        1 * view.showDatePicker(1981)
    }

    def "should refresh movies"() {
        given:
        presenter.@view = view

        when:
        presenter.refresh()

        then:
        1 * theMovieDatabaseAPI.discoverMovie(1, _, _) >> Observable.empty()
    }
}