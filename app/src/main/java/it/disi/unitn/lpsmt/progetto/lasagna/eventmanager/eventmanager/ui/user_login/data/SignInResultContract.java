package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class SignInResultContract extends ActivityResultContract<String, Uri> {
    private GoogleSignInClient i;
    private Activity a;

    public SignInResultContract(GoogleSignInClient _i, Activity _a) {
        i = _i;
        a = _a;
    }

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String s) {
        return i.getSignInIntent();
    }

    @Override
    public Uri parseResult(int i, @NonNull Intent intent) {
        return intent.getData();
    }
}
