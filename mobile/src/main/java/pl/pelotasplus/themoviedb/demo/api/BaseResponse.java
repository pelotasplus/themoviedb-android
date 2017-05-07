package pl.pelotasplus.themoviedb.demo.api;

import com.google.gson.annotations.SerializedName;

class BaseResponse {
    @SerializedName("status_message")
    private String statusMessage = "";

    private boolean success = false;

    @SerializedName("status_code")
    private int status_code = -1;

    public String getStatusMessage() {
        return statusMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatus_code() {
        return status_code;
    }
}
