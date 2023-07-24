package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.userinfo;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {
    private String id, profilePic, nome, email, tel;
    private final Integer numEvOrg;
    private final Double valutazioneMedia;

    private final List<String> EventiIscritto, EventiCreati;

    public UserInfo() {
        EventiIscritto = null;
        EventiCreati = null;
        numEvOrg = 0;
        valutazioneMedia = 0.0;
    }

    private UserInfo(@NonNull String id, @NonNull String profilePic, @NonNull String nome,
                    @NonNull String email, @NonNull String tel, @NonNull Integer numEvOrg,
                    @NonNull Double valutazioneMedia, @NonNull List<String> EventiIscritto,
                    @NonNull List<String> EventiCreati) {
        this.id = id;
        this.profilePic = profilePic;
        this.nome = nome;
        this.email = email;
        this.tel = tel;
        this.numEvOrg = numEvOrg;
        this.valutazioneMedia = valutazioneMedia;
        this.EventiIscritto = EventiIscritto;
        this.EventiCreati = EventiCreati;
    }

    public String getId() {
        return id;
    }

    public String getString(@NonNull String string) {
        switch (string) {
            case "profilePic" -> {
                return profilePic;
            }
            case "nome" -> {
                return nome;
            }
            case "email" -> {
                return email;
            }
            case "tel" -> {
                return tel;
            }
        }
        return "";
    }

    public Integer getNumEvOrg() {
        return numEvOrg;
    }

    public @NotNull Double getValutazioneMedia() {
        return valutazioneMedia;
    }

    public List<String> getEventiIscritto() {
        return EventiIscritto;
    }

    public List<String> getEventiCreati() {
        return EventiCreati;
    }

    private static String stringFromJson(@NonNull Gson gs1, String name, @NonNull JsonObject json) {
        if(json.get(name) != null) {
            return gs1.fromJson(json.get(name), String.class);
        }
        return "";
    }

    private static Integer integerFromJson(@NonNull Gson gs1, @NonNull JsonObject json) {
        if(json.get("numEvOrg") != null) {
            return gs1.fromJson(json.get("numEvOrg"), Integer.class);
        }
        return 0;
    }

    private static Double doubleFromJson(@NonNull Gson gs1, @NonNull JsonObject json) {
        if(json.get("valutazioneMedia") != null) {
            return gs1.fromJson(json.get("valutazioneMedia"), Double.class);
        }
        return 0.0;
    }

    @NonNull
    private static List<String> fromJsonArr(@NonNull Gson gs1, String name, @NonNull JsonObject json) {
        JsonArray arr = gs1.fromJson(json.get(name), JsonArray.class);

        ArrayList<String> res = new ArrayList<>();
        for(JsonElement e: arr) {
            res.add(e.getAsString());
        }

        return res;
    }

    @NonNull
    public static UserInfo parseJSON(@NonNull JsonObject json) {
        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();

        return new UserInfo(stringFromJson(gs1, "id", json), stringFromJson(gs1, "picture", json),
                stringFromJson(gs1, "nome", json), stringFromJson(gs1, "email", json),
                stringFromJson(gs1, "tel", json), integerFromJson(gs1, json),
                doubleFromJson(gs1, json), fromJsonArr(gs1, "EventiIscritto", json),
                fromJsonArr(gs1, "EventiCreati", json));
    }
}
