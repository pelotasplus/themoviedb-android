package pl.pelotasplus.themoviedb.demo.screens.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;

import pl.pelotasplus.themoviedb.demo.MobileApplication;
import pl.pelotasplus.themoviedb.demo.R;
import pl.pelotasplus.themoviedb.demo.api.Movie;
import pl.pelotasplus.themoviedb.demo.databinding.ActivityMainBinding;
import pl.pelotasplus.themoviedb.demo.databinding.ViewMovieRowBinding;
import pl.pelotasplus.themoviedb.demo.di.AppComponent;
import pl.pelotasplus.themoviedb.demo.screens.details.DetailsActivity;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private final static String EXTRA_MOVIES_KEY = "MainActivity/EXTRA_MOVIES_KEY";
    private final static String EXTRA_LAYOUT_MANAGER_KEY = "MainActivity/EXTRA_LAYOUT_MANAGER_KEY";

    private final MoviesAdapter moviesAdapter = new MoviesAdapter();
    private MainPresenter presenter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(moviesAdapter);


        presenter = new MainPresenter(getAppComponent().getTheMovieDatabaseAPI());

        presenter.bind(
                this,
                savedInstanceState != null ? savedInstanceState.getParcelableArrayList(EXTRA_MOVIES_KEY) : null
        );
    }

    @Override
    protected void onDestroy() {
        presenter.unbind();

        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        binding.recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(EXTRA_LAYOUT_MANAGER_KEY));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(EXTRA_MOVIES_KEY, moviesAdapter.getMovies());
        outState.putParcelable(EXTRA_LAYOUT_MANAGER_KEY, binding.recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                DatePickerFragment.show(getSupportFragmentManager());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addMovie(Movie movie) {
        moviesAdapter.addMovie(movie);
        moviesAdapter.notifyDataSetChanged();
    }

    private AppComponent getAppComponent() {
        return ((MobileApplication) getApplication()).getAppComponent();
    }

    private static class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {
        private ArrayList<Movie> movies = new ArrayList<>();

        @Override
        public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MovieViewHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(parent.getContext()),
                            R.layout.view_movie_row,
                            parent,
                            false
                    )
            );
        }

        @Override
        public void onBindViewHolder(MovieViewHolder holder, int position) {
            Movie movie = movies.get(position);

            holder.binding.title.setText(movie.title());
            holder.binding.overview.setText(movie.overview());
            holder.binding.releaseDate.setText(movie.releaseDate());

            holder.binding.getRoot().setOnClickListener(v ->
                    DetailsActivity.showActivity(holder.binding.getRoot().getContext(), movie));
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        void addMovie(Movie movie) {
            movies.add(movie);
        }

        ArrayList<Movie> getMovies() {
            return movies;
        }
    }

    private static class MovieViewHolder extends RecyclerView.ViewHolder {
        private ViewMovieRowBinding binding;

        MovieViewHolder(ViewMovieRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
