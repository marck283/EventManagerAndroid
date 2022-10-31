package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Classe da cui ogni classe che chiama le API web di EventManager deve ereditare se la chiamata richiede un token CSRF.
 */
public class ApiCSRFClass {
    private static String csrfToken;

    private String fromJson(@NonNull Gson gs1, @NonNull JsonObject json) {
        return gs1.fromJson(json.get("csrfToken"), String.class);
    }

    public ApiCSRFClass parseJSON(@NonNull JsonObject json) {
        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();
        csrfToken = fromJson(gs1, json);
        return this;
    }

    public void setToken(String val) {
        csrfToken = val;
    }

    public String getToken() {
        return csrfToken;
    }
}
