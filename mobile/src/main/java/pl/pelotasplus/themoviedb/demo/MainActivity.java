package pl.pelotasplus.themoviedb.demo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import pl.pelotasplus.themoviedb.demo.api.Movie;
import pl.pelotasplus.themoviedb.demo.databinding.ActivityMainBinding;
import pl.pelotasplus.themoviedb.demo.databinding.ViewMovieRowBinding;
import pl.pelotasplus.themoviedb.demo.di.AppComponent;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MoviesAdapter moviesAdapter = new MoviesAdapter();
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(moviesAdapter);

        presenter = new MainPresenter(
                getAppComponent().getTheMovieDatabaseAPI()
        );

        presenter.bind(this);
    }

    @Override
    protected void onDestroy() {
        presenter.unbind();

        super.onDestroy();
    }

    @Override
    public void addMovie(Movie movie) {
        moviesAdapter.addMovie(movie);
    }

    private AppComponent getAppComponent() {
        return ((MobileApplication) getApplication()).getAppComponent();
    }

    private static class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {
        private final SortedList<Movie> sortedList =
                new SortedList<>(Movie.class, new SortedListAdapterCallback<Movie>(this) {
                    @Override
                    public int compare(Movie o1, Movie o2) {
                        return o2.releaseDate().compareTo(o1.releaseDate());
                    }

                    @Override
                    public boolean areContentsTheSame(Movie oldItem, Movie newItem) {
                        return oldItem.equals(newItem);
                    }

                    @Override
                    public boolean areItemsTheSame(Movie item1, Movie item2) {
                        return item1 == item2;
                    }
                });

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
            Movie movie = sortedList.get(position);

            holder.binding.title.setText(movie.title());
            holder.binding.overview.setText(movie.overview());
            holder.binding.releaseDate.setText(movie.releaseDate());
        }

        @Override
        public int getItemCount() {
            return sortedList.size();
        }

        void addMovie(Movie movie) {
            sortedList.add(movie);
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
