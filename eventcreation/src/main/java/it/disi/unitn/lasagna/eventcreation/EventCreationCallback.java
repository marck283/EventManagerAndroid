package it.disi.unitn.lasagna.eventcreation;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EventCreationCallback implements Callback {

    private final Fragment f;

    private final ActivityResultLauncher<Intent> i;

    private final Intent loginIntent;

    public EventCreationCallback(@NotNull Fragment f, @NotNull ActivityResultLauncher<Intent> i, @NotNull Intent lintent) {
        this.f = f;
        this.i = i;
        loginIntent = lintent;
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        try {
            throw e;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        switch (response.code()) {
            case 201 -> ((EventCreationInterface)f.requireActivity()).showOK();
            case 400 -> ((EventCreationInterface)f.requireActivity()).showEventCreationError();
            case 401 -> i.launch(loginIntent);
            case 500 -> ((EventCreationInterface)f.requireActivity()).showInternalServerError();
            case 503 -> ((EventCreationInterface)f.requireActivity()).showServiceUavailable();
        }
    }
}
