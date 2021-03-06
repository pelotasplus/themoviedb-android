package pl.pelotasplus.themoviedb.demo.screens.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pl.pelotasplus.themoviedb.demo.MobileApplication;
import pl.pelotasplus.themoviedb.demo.MovieViewModel;
import pl.pelotasplus.themoviedb.demo.R;
import pl.pelotasplus.themoviedb.demo.api.Movie;
import pl.pelotasplus.themoviedb.demo.databinding.ActivityMainBinding;
import pl.pelotasplus.themoviedb.demo.databinding.ViewMovieRowBinding;
import pl.pelotasplus.themoviedb.demo.di.AppComponent;
import pl.pelotasplus.themoviedb.demo.screens.details.DetailsActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements
        MainContract.View, DatePickerFragment.OnDatePicked {
    private final static String EXTRA_MOVIES_KEY = "MainActivity/EXTRA_MOVIES_KEY";
    private final static String EXTRA_YEAR_KEY = "MainActivity/EXTRA_YEAR_KEY";
    private final static String EXTRA_PAGE_KEY = "MainActivity/EXTRA_PAGE_KEY";
    private final static String EXTRA_LAYOUT_MANAGER_KEY = "MainActivity/EXTRA_LAYOUT_MANAGER_KEY";
    private final static int LOAD_MORE_THRESHOLD = 5;

    private final MoviesAdapter moviesAdapter = new MoviesAdapter();
    private MainPresenter presenter;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(moviesAdapter);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                loadMoreIfNeeded(recyclerView);
            }
        });

        binding.refreshLayout.setOnRefreshListener(() -> presenter.refresh());

        presenter = new MainPresenter(
                getAppComponent().getTheMovieDatabaseAPI(),
                AndroidSchedulers.mainThread(),
                Schedulers.io()
        );

        ArrayList<Movie> savedMovies = null;
        int savedYear = Calendar.getInstance().get(Calendar.YEAR);
        int savedPage = 1;

        if (savedInstanceState != null) {
            savedMovies = savedInstanceState.getParcelableArrayList(EXTRA_MOVIES_KEY);
            savedYear = savedInstanceState.getInt(EXTRA_YEAR_KEY);
            savedPage = savedInstanceState.getInt(EXTRA_PAGE_KEY);
        }

        presenter.bind(this, savedMovies, savedYear, savedPage);
    }

    private void loadMoreIfNeeded(RecyclerView recyclerView) {
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
        int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();

        if ((totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + LOAD_MORE_THRESHOLD)
                || totalItemCount == 0) {
            presenter.loadMore();
        }
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
        outState.putInt(EXTRA_YEAR_KEY, presenter.getYear());
        outState.putInt(EXTRA_PAGE_KEY, presenter.getPage());
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
                presenter.filterClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // MainContract.View

    @Override
    public void addMovies(List<Movie> movies) {
        moviesAdapter.addMovies(movies);
        moviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearMovies() {
        moviesAdapter.clearMovies();
        moviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showYear(int year) {
        getSupportActionBar().setTitle(getString(R.string.title_filter, year));
    }

    @Override
    public void showDatePicker(int year) {
        DatePickerFragment.show(getSupportFragmentManager(), year);
    }

    @Override
    public void hideRefreshing() {
        Observable
                .timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> binding.refreshLayout.setRefreshing(false));
    }

    @Override
    public void showRefreshingError(Throwable throwable) {
        Snackbar
                .make(
                        binding.refreshLayout,
                        getString(R.string.error_loading, throwable.getMessage()),
                        Snackbar.LENGTH_LONG
                )
                .show();
    }

    @Override
    public void showRefreshing() {
        binding.refreshLayout.setRefreshing(true);
    }

    // DatePickerFragment.OnDatePicked

    @Override
    public void onYearPicked(int year) {
        presenter.yearPicked(year);
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

            holder.binding.setViewModel(new MovieViewModel(movie));

            holder.binding.getRoot().setOnClickListener(v ->
                    DetailsActivity.showActivity(holder.binding.getRoot().getContext(), movie));
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        ArrayList<Movie> getMovies() {
            return movies;
        }

        void addMovies(List<Movie> movies) {
            this.movies.addAll(movies);
        }

        void clearMovies() {
            this.movies.clear();
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
