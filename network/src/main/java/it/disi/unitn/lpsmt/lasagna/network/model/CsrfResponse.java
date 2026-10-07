package it.disi.unitn.lpsmt.lasagna.network.model;

import com.google.gson.annotations.SerializedName;

public class CsrfResponse {

    @SerializedName("csrfToken")
    private String csrfToken;

    public CsrfResponse() {
        // Required for Gson
    }

    public CsrfResponse(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }
}
