package pl.pelotasplus.themoviedb.demo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import pl.pelotasplus.themoviedb.demo.databinding.ActivityMainBinding;
import pl.pelotasplus.themoviedb.demo.databinding.ViewMovieRowBinding;
import pl.pelotasplus.themoviedb.demo.di.AppComponent;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private MoviesAdapter moviesAdapter = new MoviesAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(moviesAdapter);

    }

    private AppComponent getAppComponent() {
        return ((MobileApplication) getApplication()).getAppComponent();
    }

    private static class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {
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

        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    private static class MovieViewHolder extends RecyclerView.ViewHolder {
        private ViewMovieRowBinding binding;

        public MovieViewHolder(ViewMovieRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
