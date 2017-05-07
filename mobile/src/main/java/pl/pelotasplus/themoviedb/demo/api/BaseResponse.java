package pl.pelotasplus.themoviedb.demo.api;

import com.google.gson.annotations.SerializedName;

class BaseResponse {
    @SerializedName("status_message")
    private String statusMessage = "";

    private boolean success = false;

    @SerializedName("status_code")
    private int statusCode = -1;
}