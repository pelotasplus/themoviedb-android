package pl.pelotasplus.themoviedb.demo

import pl.pelotasplus.themoviedb.demo.api.Movie
import spock.lang.Specification

class MovieViewModelSpec extends Specification {
    def movie = Movie.create(
            "file.png",
            false,
            "overview",
            "release date",
            [],
            123,
            "original title",
            "original language",
            "title",
            14.5,
            876,
            true,
            4.5
    )

    def viewModel

    def "setup"() {
        viewModel = new MovieViewModel(movie)
    }

    def "should have reference to movie"() {
        expect:
        viewModel.@movie == movie
    }

    def "should get title"() {
        expect:
        viewModel.getTitle() == "title"
    }

    def "should get overview"() {
        expect:
        viewModel.getOverview() == "overview"
    }

    def "should get release date"() {
        expect:
        viewModel.getReleaseDate() == "release date"
    }

    def "should get link to poster"() {
        expect:
        viewModel.getCover() == "https://image.tmdb.org/t/p/w500/file.png"
    }
}
