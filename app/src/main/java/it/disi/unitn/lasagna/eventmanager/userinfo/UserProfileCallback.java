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
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import it.disi.unitn.lasagna.eventmanager.ui_extra.special_buttons.ListenerButton;
import it.disi.unitn.lpsmt.lasagna.localdatabase.AppDatabase;
import it.disi.unitn.lpsmt.lasagna.localdatabase.daos.UserDAO;
import it.disi.unitn.lpsmt.lasagna.localdatabase.entities.User;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
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

    public void handleProfileSuccess(@NonNull JsonObject userProfile) {
        final UserInfo userInfo = UserInfo.parseJSON(userProfile);

        Activity activity = f.getActivity();
        if(activity != null && !activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
            AppDatabase db = Room.databaseBuilder(activity.getApplicationContext(),
                    AppDatabase.class, "EventManagerDB").fallbackToDestructiveMigration().build();
            UserDAO userDao = db.getUserDAO();
            User userEntity = userInfo.toUser();
            new Thread(() -> {
                if (userDao.getUser(userEntity.getId()) == null) {
                    userDao.insert(userEntity);
                } else {
                    userDao.updateUserProfile(userEntity.getId(), userEntity.getNome(),
                            userEntity.getEmail(), userEntity.getTel(),
                            userEntity.getProfilePic(), userEntity.getEventiCreati(),
                            userEntity.getEventiIscritto(), userEntity.getNumEvOrg(),
                            userEntity.getValutazioneMedia());
                }
                db.close();
            }).start();

            //Imposta la schermata del profilo dell'utente
            activity.runOnUiThread(() -> {
                if (!activity.isFinishing() && !activity.isDestroyed()) {
                    ImageView iv = v.findViewById(R.id.profilePic);
                    Glide.with(activity).load(userInfo.getString("profilePic"))
                            .diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop().into(iv);

                    TextView username = v.findViewById(R.id.username);
                    username.setText(f.getString(R.string.username, userInfo.getString("nome")));

                    TextView email = v.findViewById(R.id.email);
                    email.setText(f.getString(R.string.user_email, userInfo.getString("email")));

                    TextView phone = v.findViewById(R.id.phone_value);
                    if (userInfo.getString("tel") != null && !userInfo.getString("tel").isEmpty()) {
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
                            if (!activity.isFinishing() && !activity.isDestroyed()) {
                                AlertDialog ad = new AlertDialog.Builder(activity).create();
                                ad.setTitle(R.string.personal_rating);
                                ad.setMessage(f.getString(R.string.personal_rating_message, meanRating));
                                ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (c1, d) -> c1.dismiss());
                                ad.show();
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        //Set up the ImageView
        if(response.isSuccessful()) {
            Gson gson1 = new GsonBuilder().create();
            JsonObject res = gson1.fromJson(response.body().string(), JsonObject.class);
            handleProfileSuccess(res);

            response.body().close();
        } else {
            Log.i("onlineResponse", "Utente non trovato");
        }
    }
}
