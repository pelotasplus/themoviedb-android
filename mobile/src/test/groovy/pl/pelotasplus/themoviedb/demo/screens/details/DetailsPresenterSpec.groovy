package pl.pelotasplus.themoviedb.demo.screens.details

import pl.pelotasplus.themoviedb.demo.api.Movie
import spock.lang.Specification

class DetailsPresenterSpec extends Specification {
    DetailsContract.View view = Mock();
    Movie movie = Mock()

    DetailsPresenter presenter

    def "setup"() {
        presenter = new DetailsPresenter()
    }

    def "should show movie if not empty"() {
        when:
        presenter.bind(view, movie)

        then:
        1 * view.showMovieDetails(movie)
    }

    def "should not show movie if empty"() {
        when:
        presenter.bind(view, null)

        then:
        0 * view.showMovieDetails(_)
    }

    def "should do nothing on unbind"() {
        when:
        presenter.unbind()

        then:
        0 * _
    }
}