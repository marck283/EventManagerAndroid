package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.userinfo;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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
                        Glide.with(f.requireActivity()).load(userInfo.getString("profilePic")).circleCrop().into(iv);

                        TextView username = v.findViewById(R.id.username);
                        username.setText(userInfo.getString("nome"));

                        TextView email = v.findViewById(R.id.email_value);
                        email.setText(userInfo.getString("email"));

                        TextView phone = v.findViewById(R.id.phone_value);
                        phone.setText(userInfo.getString("tel"));

                        TextView numEvOrg = v.findViewById(R.id.numEvOrg);
                        numEvOrg.setText(f.getString(R.string.numEvOrg, userInfo.getNumEvOrg()));

                        if(userInfo.getNumEvOrg() == 0) {
                            v.findViewById(R.id.rating).setEnabled(false);
                            v.findViewById(R.id.rating).setVisibility(View.INVISIBLE);
                        } else {
                            v.findViewById(R.id.rating).setEnabled(true);
                            v.findViewById(R.id.rating).setVisibility(View.VISIBLE);
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
