package pl.pelotasplus.themoviedb.demo.api;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class MoviesResponse extends BaseResponse {
    int page;

    @SerializedName("total_results")
    int totalResults;

    @SerializedName("total_pages")
    int totalPages;

    private List<Movie> results = Collections.emptyList();

    public List<Movie> getResults() {
        return results;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
