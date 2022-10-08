package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Classe da cui ogni classe che chiama le API web di EventManager deve ereditare se la chiamata richiede un token CSRF.
 */
public class ApiCSRFClass {
    private static String token;

    public ApiCSRFClass parseJSON(@NonNull JsonObject json) {
        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();
        gs1.fromJson(json.toString(), ApiCSRFClass.class);
        return this;
    }

    public void setToken(String val) {
        token = val;
    }

    public String getToken() {
        return token;
    }
}
