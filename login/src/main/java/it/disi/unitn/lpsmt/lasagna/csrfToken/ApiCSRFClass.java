package it.disi.unitn.lpsmt.lasagna.csrfToken;

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

    private ApiCSRFClass(@NotNull String token) {
        csrfToken = token;
    }

    private static String fromJson(@NonNull Gson gs1, @NonNull JsonObject json) {
        return gs1.fromJson(json.get("csrfToken"), String.class);
    }

    @NonNull
    public static ApiCSRFClass parseJSON(@NonNull JsonObject json) throws InvalidObjectException {
        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();
        return new ApiCSRFClass(fromJson(gs1, json));
    }

    public String getToken() {
        return csrfToken;
    }
}
