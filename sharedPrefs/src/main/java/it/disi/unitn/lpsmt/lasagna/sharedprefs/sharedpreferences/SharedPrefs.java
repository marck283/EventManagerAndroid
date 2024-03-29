package it.disi.unitn.lpsmt.lasagna.sharedprefs.sharedpreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class SharedPrefs {
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SharedPrefs(@NonNull String prefString, @NonNull Activity a) {
        prefs = a.getSharedPreferences(prefString, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setString(@NonNull String key, @NonNull String value) {
        editor.putString(key, value);
    }

    public String getString(@NonNull String key) {
        return prefs.getString(key, "");
    }

    public void setBoolean(@NotNull String key, boolean value) {
        editor.putBoolean(key, value);
    }

    public boolean getBoolean(@NotNull String key) {
        boolean res = false;
        return prefs.getBoolean(key, res);
    }

    public void apply() {
        editor.apply();
    }
}
