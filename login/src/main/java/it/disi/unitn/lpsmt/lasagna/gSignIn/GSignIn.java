package it.disi.unitn.lpsmt.lasagna.gSignIn;

import android.app.Activity;
import android.content.MutableContextWrapper;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.ClearCredentialException;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.credentials.exceptions.NoCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import java.security.SecureRandom;

public class GSignIn {

    private final CredentialManager credentialManager;
    private final GetCredentialRequest request;
    private final MutableContextWrapper mcwrapper;

    private String idToken;

    private final String serverClientId;

    public GSignIn(@NonNull Activity a, @StringRes int clientID) {
        serverClientId = a.getString(clientID);

        credentialManager = CredentialManager.create(a);
        mcwrapper = new MutableContextWrapper(a);

        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(serverClientId)
                .setAutoSelectEnabled(false)
                .setNonce(generateSecureRandomNonce())
                .build();

        request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();
    }

    private String generateSecureRandomNonce() {
        byte[] nonceBytes = new byte[16];
        new SecureRandom().nextBytes(nonceBytes);
        return Base64.encodeToString(nonceBytes, Base64.NO_WRAP);
    }

    public String getIdToken() {
        return idToken;
    }

    private void handleResult(GetCredentialResponse result, OnSignInListener listener) {
        Credential credential = result.getCredential();
        if (credential instanceof CustomCredential customCredential &&
                customCredential.getType().equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            try {
                GoogleIdTokenCredential googleIdToken = GoogleIdTokenCredential.createFrom(customCredential.getData());
                idToken = googleIdToken.getIdToken();
                // Handle Google ID Token

                String email = googleIdToken.getEmail();
                String displayName = googleIdToken.getDisplayName();
                String givenName = googleIdToken.getGivenName();
                String familyName = googleIdToken.getFamilyName();
                Uri photoUri = googleIdToken.getProfilePictureUri();

                listener.onSuccess(googleIdToken);
            } catch (Exception e) {
                listener.onError(e);
            }
        }
    }

    private void signInWithAllAccounts(@NonNull Activity a, OnSignInListener listener) {
        // 2. Fallback attempt: filterByAuthorizedAccounts = false
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(serverClientId)
                .setAutoSelectEnabled(false)
                .setNonce(generateSecureRandomNonce())
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        credentialManager.getCredentialAsync(
                mcwrapper,
                request,
                null,
                ContextCompat.getMainExecutor(a),
                new CredentialManagerCallback<>() {

                    @Override
                    public void onError(@NonNull GetCredentialException error) {
                        listener.onError(error);
                    }

                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleResult(result, listener);
                    }
                });
    }

    public void signIn(@NonNull Activity a, OnSignInListener listener) {
        mcwrapper.setBaseContext(a);

        credentialManager.getCredentialAsync(
                mcwrapper,
                request,
                null,
                ContextCompat.getMainExecutor(a),
                new CredentialManagerCallback<>() {

                    @Override
                    public void onError(@NonNull GetCredentialException error) {
                        if (error instanceof NoCredentialException) {
                            signInWithAllAccounts(a, listener);
                        } else {
                            listener.onError(error);
                        }
                    }

                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleResult(result, listener);
                    }
                });
    }

    public void signOut(@NonNull Activity activity, @Nullable Runnable onComplete) {
        CredentialManager cm = CredentialManager.create(activity);
        ClearCredentialStateRequest clearRequest = new ClearCredentialStateRequest();
        cm.clearCredentialStateAsync(
                clearRequest,
                new CancellationSignal(),
                ContextCompat.getMainExecutor(activity),
                new CredentialManagerCallback<>() {
                    @Override
                    public void onResult(Void result) {
                        Log.i("GSignIn", "Credential state cleared successfully");
                        idToken = null;
                        if (onComplete != null) {
                            activity.runOnUiThread(onComplete);
                        }
                    }

                    @Override
                    public void onError(@NonNull ClearCredentialException e) {
                        Log.e("GSignIn", "Error clearing credential state: " + e.getMessage());
                        idToken = null;
                        if (onComplete != null) {
                            activity.runOnUiThread(onComplete);
                        }
                    }
                }
        );
    }
}
