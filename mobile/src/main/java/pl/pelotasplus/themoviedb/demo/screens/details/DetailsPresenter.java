package pl.pelotasplus.themoviedb.demo.screens.details;

import pl.pelotasplus.themoviedb.demo.api.Movie;

class DetailsPresenter implements DetailsContract.Presenter {
    DetailsPresenter() {
    }

    @Override
    public void bind(DetailsContract.View view, Movie movie) {
        if (movie != null) view.showMovieDetails(movie);
    }

    @Override
    public void unbind() {
    }
}
