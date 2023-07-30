package it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class OrganizerCallback implements Callback {
    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        try {
            throw e;
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public abstract void onResponse(@NonNull Call call, @NonNull Response response) throws IOException;
}
