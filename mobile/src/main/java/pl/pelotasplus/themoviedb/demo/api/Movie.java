package pl.pelotasplus.themoviedb.demo.api;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class Movie implements Parcelable {
    public static TypeAdapter<Movie> typeAdapter(Gson gson) {
        return new AutoValue_Movie.GsonTypeAdapter(gson);
    }

    @Nullable
    @SerializedName("poster_path")
    public abstract String posterPath();

    abstract boolean adult();

    public abstract String overview();

    @SerializedName("release_date")
    public abstract String releaseDate();

    @SerializedName("genre_ids")
    abstract List<Integer> genreIds();

    public abstract int id();

    @SerializedName("original_title")
    abstract String originalTitle();

    @SerializedName("original_language")
    abstract String originalLanguage();

    public abstract String title();

    abstract float popularity();

    @SerializedName("vote_count")
    abstract int voteCount();

    abstract boolean video();

    @SerializedName("vote_average")
    abstract float voteAverage();
}
