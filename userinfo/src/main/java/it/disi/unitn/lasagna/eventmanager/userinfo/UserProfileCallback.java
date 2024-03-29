package it.disi.unitn.lasagna.eventmanager.userinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.icu.text.MessageFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import it.disi.unitn.lpsmt.lasagna.localdatabase.queryClasses.DBUser;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.special_buttons.ListenerButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserProfileCallback implements Callback {
    private final Fragment f;

    private final View v;

    public UserProfileCallback(@NotNull Fragment f, @NotNull View v) {
        this.f = f;
        this.v = v;
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        try {
            throw e;
        } catch(Throwable e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        //Set up the ImageView
        if(response.isSuccessful()) {
            Gson gson1 = new GsonBuilder().create();
            JsonObject res = gson1.fromJson(response.body().string(), JsonObject.class);
            final UserInfo userInfo = UserInfo.parseJSON(res);

            Activity activity = f.getActivity();
            if(activity != null && f.isAdded()) {
                DBUser dbUser = new DBUser(f.requireActivity(), "setProfile", v, userInfo);
                if(!dbUser.checkUser(userInfo.getId())) {
                    dbUser.start();
                } else {
                    dbUser.insert();
                }

                //Imposta la schermata del profilo dell'utente
                f.requireActivity().runOnUiThread(() -> {
                    ImageView iv = v.findViewById(R.id.profilePic);
                    Glide.with(f.requireActivity()).load(userInfo.getString("profilePic"))
                            .diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop().into(iv);

                    TextView username = v.findViewById(R.id.username);
                    username.setText(f.getString(R.string.username, userInfo.getString("nome")));

                    TextView email = v.findViewById(R.id.email);
                    email.setText(f.getString(R.string.user_email, userInfo.getString("email")));

                    TextView phone = v.findViewById(R.id.phone_value);
                    if (userInfo.getString("tel") != null && !userInfo.getString("tel").equals("")) {
                        phone.setText(f.getString(R.string.phone, userInfo.getString("tel")));
                    } else {
                        phone.setText(f.getString(R.string.phone, f.getString(R.string.parameter_not_set)));
                    }

                    TextView numEvOrg = v.findViewById(R.id.numEvOrg);
                    String eventi = MessageFormat.format(f.getString(R.string.numEvOrg, userInfo.getNumEvOrg()),
                            new StringBuffer());
                    numEvOrg.setText(/*f.getString(R.string.numEvOrg, userInfo.getNumEvOrg())*/eventi);

                    ListenerButton rating = v.findViewById(R.id.rating);
                    if (userInfo.getNumEvOrg() == 0 || userInfo.getValutazioneMedia() == 0.0) {
                        rating.setEnabled(false);
                        rating.setVisibility(View.INVISIBLE);
                    } else {
                        rating.setEnabled(true);
                        rating.setVisibility(View.VISIBLE);
                        final double meanRating = userInfo.getValutazioneMedia();
                        rating.setOnClickListener(c -> {
                            AlertDialog ad = new AlertDialog.Builder(f.requireContext()).create();
                            ad.setTitle(R.string.personal_rating);
                            ad.setMessage(f.getString(R.string.personal_rating_message, meanRating));
                            ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (c1, d) -> c1.dismiss());
                            ad.show();
                        });
                    }
                });
            }
            response.body().close();
        } else {
            Log.i("onlineResponse", "Utente non trovato");
        }
    }
}
