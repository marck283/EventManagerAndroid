package it.disi.unitn.lpsmt.lasagna.gSignIn;

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

public interface OnSignInListener {
    void onSuccess(GoogleIdTokenCredential credential);
    void onError(Exception error);
}
