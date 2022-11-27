package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.userinfo;

import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OnlineUserInfo {
    private final UserInfoInterface userInfo;

    public OnlineUserInfo() {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userInfo = retro.create(UserInfoInterface.class);
    }

    public void getUserInfo(@NonNull String accessToken, @NonNull View v, @NonNull Fragment f) {
        Call<JsonObject> call = userInfo.getUserInfo(accessToken);
        call.enqueue(new Callback<>() {

            /**
             * Invoked for a received HTTP response.
             *
             * <p>Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call {@link Response#isSuccessful()} to determine if the response indicates success.
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                //Set up the ImageView
                if(response.body() != null) {
                    if(response.isSuccessful()) {
                        UserInfo userInfo = new UserInfo();
                        userInfo = userInfo.parseJSON(response.body());

                        //Imposta la schermata del profilo dell'utente
                        ImageView iv = v.findViewById(R.id.profilePic);
                        Glide.with(f.requireActivity()).load(userInfo.getString("profilePic"))
                                .diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop().into(iv);

                        TextView username = v.findViewById(R.id.username);
                        username.setText(f.getString(R.string.username, userInfo.getString("nome")));

                        TextView email = v.findViewById(R.id.email);
                        email.setText(f.getString(R.string.user_email, userInfo.getString("email")));

                        TextView phone = v.findViewById(R.id.phone_value);
                        if(userInfo.getString("tel") != null && !userInfo.getString("tel").equals("")) {
                            phone.setText(f.getString(R.string.phone, userInfo.getString("tel")));
                        } else {
                            phone.setText(f.getString(R.string.phone, "non presente"));
                        }

                        TextView numEvOrg = v.findViewById(R.id.numEvOrg);
                        numEvOrg.setText(f.getString(R.string.numEvOrg, userInfo.getNumEvOrg()));

                        Button rating = v.findViewById(R.id.rating);
                        if(userInfo.getNumEvOrg() == 0) {
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
                    } else {
                        Log.i("onlineResponse", "Utente non trovato");
                    }
                } else {
                    Log.i("onlineResponse", "nessuna risposta dal server");
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                try {
                    throw t;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
