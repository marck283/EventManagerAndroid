package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.daos.UserDAO;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.entities.User;

public class DBUser extends DBThread {
    private final String action, id;
    private String profilePic;
    private final UserDAO user;
    private final View v;
    private Fragment f;
    private final Activity a;

    private final it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.userinfo.UserInfo userInfo;

    public DBUser(@NonNull String id, @NonNull String action, @NonNull View v, @NonNull Fragment f) {
        super(f.requireActivity());
        this.action = action;
        this.v = v;
        this.f = f;
        user = db.getUserDAO();
        profilePic = "";
        a = f.requireActivity();
        userInfo = null;
        this.id = id;
    }
    public DBUser(@NonNull Activity a, @NonNull String action, @NonNull View v,
                  @NonNull it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.userinfo.UserInfo userInfo) {
        super(a);
        this.action = action;
        user = db.getUserDAO();
        this.profilePic = "";
        this.v = v;
        this.a = a;
        this.userInfo = userInfo;
        id = userInfo.getId();
    }

    public boolean checkUser(@NonNull String id) {
        Log.i("null", id);
        return user.getUser(id) == null;
    }

    public void insert() {
        User u = new User();
        u.setId(id);
        u.setEmail(userInfo.getString("email"));
        u.setNome(userInfo.getString("nome"));
        u.setValutazioneMedia(userInfo.getValutazioneMedia());
        u.setProfilePic(userInfo.getString("profilePic"));
        u.setTel(userInfo.getString("tel"));
        u.setEventiIscritto(userInfo.getEventiIscritto());
        u.setEventiCreati(userInfo.getEventiCreati());
        user.insert(u);
    }

    public void run() {
        synchronized(this) {
            switch(action) {
                case "getAll": {
                    User u = user.getUser(id);

                    profilePic = u.getProfilePic();

                    a.runOnUiThread(() -> {
                        //Imposta la schermata del profilo dell'utente
                        ImageView iv = v.findViewById(R.id.profilePic);
                        if(profilePic != null) {
                            Glide.with(f.requireActivity()).load(profilePic).circleCrop().into(iv);
                        }

                        TextView username = v.findViewById(R.id.username);
                        username.setText(f.getString(R.string.username, u.getNome()));

                        TextView email = v.findViewById(R.id.email);
                        email.setText(f.getString(R.string.user_email, u.getEmail()));

                        TextView phone = v.findViewById(R.id.phone_value);
                        phone.setText(f.getString(R.string.phone, u.getTel()));

                        TextView numEvOrg = v.findViewById(R.id.numEvOrg);
                        numEvOrg.setText(f.getString(R.string.numEvOrg, u.getNumEvOrg()));

                        Button rating = v.findViewById(R.id.rating);
                        if(u.getNumEvOrg() == 0) {
                            rating.setEnabled(false);
                            rating.setVisibility(View.INVISIBLE);
                        } else {
                            rating.setEnabled(true);
                            rating.setVisibility(View.VISIBLE);
                            final double meanRating = u.getValutazioneMedia();
                            rating.setOnClickListener(c -> {
                                AlertDialog ad = new AlertDialog.Builder(f.requireContext()).create();
                                ad.setTitle(R.string.personal_rating);
                                ad.setMessage(f.getString(R.string.personal_rating_message, meanRating));
                                ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (c1, d) -> c1.dismiss());
                                ad.show();
                            });
                        }
                    });
                    break;
                }
                case "setProfile": {
                    if(userInfo != null) {
                        user.updateUserProfile(userInfo.getId(), userInfo.getString("nome"),
                                userInfo.getString("email"), userInfo.getString("tel"),
                                userInfo.getString("profilePic"), userInfo.getEventiCreati(),
                                userInfo.getEventiIscritto(), userInfo.getNumEvOrg(),
                                userInfo.getValutazioneMedia());
                    }
                    break;
                }
            }
            notify();
            close();
        }
    }

    public String getProfilePic() {
        return profilePic;
    }
}
