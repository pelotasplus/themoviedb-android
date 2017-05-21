package pl.pelotasplus.themoviedb.demo.screens.main

import pl.pelotasplus.themoviedb.demo.api.Movie
import pl.pelotasplus.themoviedb.demo.api.MoviesResponse
import pl.pelotasplus.themoviedb.demo.api.TheMovieDatabaseAPI
import rx.Observable
import rx.schedulers.TestScheduler
import spock.lang.Specification
import spock.lang.Unroll

class MainPresenterSpec extends Specification {
    MainContract.View view = Mock();
    TheMovieDatabaseAPI theMovieDatabaseAPI = Mock()
    def year = 2017
    def page = 4
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
        presenter.bind(view, null, year, page)

        then:
        presenter.@view == view
    }

    def "should keep year reference on bind"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.empty()

        when:
        presenter.bind(view, null, year, page)

        then:
        presenter.@year == year
    }

    def "should keep page reference on bind"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.empty()

        when:
        presenter.bind(view, null, year, page)

        then:
        presenter.@page == page
    }

    def "should fetch new movies on bind"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.just(
                new MoviesResponse(
                        results: moviesList
                )
        )

        when:
        presenter.bind(view, null, year, page)

        and:
        subscribeOn.triggerActions()
        observeOn.triggerActions()

        then:
        1 * view.addMovies(moviesList)
    }

    def "should restore saved movies on bind"() {
        when:
        presenter.bind(view, moviesList, year, page)

        then:
        1 * view.addMovies(moviesList)
    }

    def "should clean subscriptions on unbind"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.just(
                new MoviesResponse(
                        results: moviesList
                )
        )

        when:
        presenter.bind(view, null, year, page)

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
        presenter.bind(view, moviesList, year, page)

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

    def "should reset state on year change"() {
        given:
        presenter.@view = view
        presenter.page = 400
        presenter.totalPages = 400
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.empty()

        when:
        presenter.yearPicked(1000)

        then:
        presenter.page == 1
        presenter.totalPages == Integer.MAX_VALUE
        1 * view.clearMovies()
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

    def "should return page"() {
        when:
        presenter.@page = 4000

        then:
        presenter.getPage() == 4000
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

    def "should reset state when refreshing movies"() {
        given:
        presenter.@view = view
        presenter.page = 1000
        presenter.totalPages == 2000
        theMovieDatabaseAPI.discoverMovie(1, _, _) >> Observable.empty()

        when:
        presenter.refresh()

        then:
        presenter.page == 1
        presenter.totalPages == Integer.MAX_VALUE
        1 * view.clearMovies()
    }

    def "should reset state"() {
        given:
        presenter.@view = view
        presenter.page = 1000
        presenter.totalPages == 2000

        when:
        presenter.resetMoviesAndState()

        then:
        presenter.page == 1
        presenter.totalPages == Integer.MAX_VALUE
        1 * view.clearMovies()
    }

    def "should set total pages count after fetching movies"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.just(
                new MoviesResponse(
                        page: 1,
                        totalPages: 10,
                        results: moviesList
                )
        )
        presenter.@view = view

        when:
        presenter.fetchMovies(null)

        and:
        subscribeOn.triggerActions()
        observeOn.triggerActions()

        then:
        presenter.totalPages == 10
    }

    def "should set 'in progress' when fetching movies"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.just(
                new MoviesResponse(
                        page: 1,
                        totalPages: 10,
                        results: moviesList
                )
        )
        presenter.@view = view

        when:
        presenter.fetchMovies(null)

        then:
        presenter.isLoading
    }

    def "should clear 'in progress' after fetching movies"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.just(
                new MoviesResponse(
                        page: 1,
                        totalPages: 10,
                        results: moviesList
                )
        )
        presenter.@view = view

        when:
        presenter.fetchMovies(null)

        then:
        presenter.isLoading

        when:
        subscribeOn.triggerActions()
        observeOn.triggerActions()

        then:
        ! presenter.isLoading
    }

    def "should hide refreshing after loading movies"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.just(
                new MoviesResponse(
                        page: 1,
                        totalPages: 10,
                        results: moviesList
                )
        )
        presenter.@view = view

        when:
        presenter.fetchMovies(null)

        and:
        subscribeOn.triggerActions()
        observeOn.triggerActions()

        then:
        1 * view.hideRefreshing()
    }

    def "should show refreshing while loading movies"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.just(
                new MoviesResponse(
                        page: 1,
                        totalPages: 10,
                        results: moviesList
                )
        )
        presenter.@view = view

        when:
        presenter.fetchMovies(null)

        and:
        subscribeOn.triggerActions()

        then:
        1 * view.showRefreshing()
    }

    def "should bump page after fetching movies"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.just(
                new MoviesResponse(
                        page: 1,
                        totalPages: 10,
                        results: moviesList
                )
        )
        presenter.@view = view

        when:
        presenter.fetchMovies(null)

        and:
        subscribeOn.triggerActions()
        observeOn.triggerActions()

        then:
        presenter.page == 2
    }

    def "should show refreshing error when error happens while loading movies"() {
        given:
        theMovieDatabaseAPI.discoverMovie(_, _, _) >> Observable.error(new Exception("foobar"))
        presenter.@view = view

        when:
        presenter.fetchMovies(null)

        and:
        subscribeOn.triggerActions()
        observeOn.triggerActions()

        then:
        1 * view.showRefreshingError(_)
    }

    @Unroll
    def "should not fetch movies if past last page"() {
        given:
        presenter.@view = view
        presenter.isLoading = false
        presenter.page = 100
        presenter.totalPages = total

        when:
        presenter.fetchMovies(null)

        and:
        subscribeOn.triggerActions()
        observeOn.triggerActions()

        then:
        0 * theMovieDatabaseAPI.discoverMovie(_, _, _)

        where:
        total << [100, 99]
    }

    def "should not fetch movies if already fetching"() {
        given:
        presenter.@view = view
        presenter.isLoading = true

        when:
        presenter.fetchMovies(null)

        and:
        subscribeOn.triggerActions()
        observeOn.triggerActions()

        then:
        0 * theMovieDatabaseAPI.discoverMovie(_, _, _)
    }
}