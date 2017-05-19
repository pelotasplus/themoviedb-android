package pl.pelotasplus.themoviedb.demo.screens.details;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pl.pelotasplus.themoviedb.demo.MovieViewModel;
import pl.pelotasplus.themoviedb.demo.R;
import pl.pelotasplus.themoviedb.demo.api.Movie;
import pl.pelotasplus.themoviedb.demo.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity implements DetailsContract.View {
    private final static String EXTRA_MOVIE_KEY = "DetailsActivity/EXTRA_MOVIE_KEY";
    private DetailsPresenter presenter;
    private ActivityDetailsBinding binding;

    public static void showActivity(Context context, Movie movie) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE_KEY, movie);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        presenter = new DetailsPresenter();
        presenter.bind(this, getIntent().getParcelableExtra(EXTRA_MOVIE_KEY));
    }

    @Override
    protected void onDestroy() {
        presenter.unbind();

        super.onDestroy();
    }

    @Override
    public void showMovieDetails(Movie movie) {
        binding.setViewModel(new MovieViewModel(movie));
    }
}