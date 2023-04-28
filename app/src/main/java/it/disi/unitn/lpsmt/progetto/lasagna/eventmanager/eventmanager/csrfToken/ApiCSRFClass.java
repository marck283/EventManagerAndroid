package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.InvalidObjectException;

/**
 * Classe da cui ogni classe che chiama le API web di EventManager deve ereditare se la chiamata richiede un token CSRF.
 */
public class ApiCSRFClass {
    private static String csrfToken;

    private ApiCSRFClass(@NotNull String token) throws InvalidObjectException {
        if(token == null) {
            throw new InvalidObjectException("Il token CSRF non può essere null.");
        }
        csrfToken = token;
    }

    private static String fromJson(@NonNull Gson gs1, @NonNull JsonObject json) {
        return gs1.fromJson(json.get("csrfToken"), String.class);
    }

    @NonNull
    public static ApiCSRFClass parseJSON(@NonNull JsonObject json) throws InvalidObjectException {
        if(json == null) {
            throw new InvalidObjectException("L'oggetto JSON contenente il token CSRF non può essere null.");
        }

        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();
        return new ApiCSRFClass(fromJson(gs1, json));
    }

    public void setToken(String val) {
        csrfToken = val;
    }

    public String getToken() {
        return csrfToken;
    }
}
