package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.eventAdditionalInfoFragment;

import android.app.Activity;
import android.content.Intent;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;
import it.disi.unitn.lasagna.eventcreation.EventCreationInterface;
import it.disi.unitn.lasagna.eventcreation.viewmodel.EventViewModel;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import it.disi.unitn.lpsmt.lasagna.network.repository.EventRepository;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EventAdditionalInfoViewModel extends ViewModel {

    public void createPrivateEvent(@NonNull Fragment f, @NonNull String jwt, @NonNull EventViewModel evm,
                                   @Nullable ActivityResultLauncher<Intent> launcher) {
        if (!jwt.isEmpty()) {
            JSONObject jsonObject = evm.toJson();
            RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));

            RetrofitClient.getInstance().setAccessToken(jwt);
            EventRepository repo = new EventRepository();

            Intent loginIntent = (launcher != null) ? new Intent(f.requireContext(), LoginActivity.class) : null;

            repo.createPrivateEvent(body, new EventRepository.EventActionCallback() {
                @Override
                public void onSuccess() {
                    Activity activity = f.getActivity();
                    if (activity instanceof EventCreationInterface eci && !activity.isFinishing() && !activity.isDestroyed()) {
                        eci.showOK();
                    }
                }

                @Override
                public void onError(int statusCode, String errorMessage) {
                    Activity activity = f.getActivity();
                    if (activity instanceof EventCreationInterface eci && !activity.isFinishing() && !activity.isDestroyed()) {
                        if (statusCode == 400) {
                            eci.showEventCreationError();
                        } else if (statusCode == 401 && launcher != null && loginIntent != null) {
                            launcher.launch(loginIntent);
                        } else if (statusCode == 500) {
                            eci.showInternalServerError();
                        } else if (statusCode == 503) {
                            eci.showServiceUavailable();
                        } else {
                            eci.showEventCreationError();
                        }
                    }
                }
            });
        }
    }
}