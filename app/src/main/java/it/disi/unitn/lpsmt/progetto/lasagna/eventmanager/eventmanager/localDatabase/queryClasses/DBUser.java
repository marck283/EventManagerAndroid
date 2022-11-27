package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.UserDAO;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.helpers.UserInfo;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.model.LoggedInUser;

public class DBUser extends DBThread {
    private final String action;
    private final String email;
    private String gToken;
    private final UserDAO user;
    private String profilePic;
    private View v;
    private Fragment f;
    private Activity a;

    public DBUser(@NonNull Activity a, @NonNull String email, @NonNull String action) {
        super(a);
        this.action = action;
        user = db.getUserDAO();
        this.email = email;
        profilePic = "";
        this.a = a;
    }

    public DBUser(@NonNull Activity a, @NonNull String email, @NonNull String action, @NonNull View v, @NonNull Fragment f) {
        super(a);
        this.email = email;
        this.action = action;
        this.v = v;
        this.f = f;
        user = db.getUserDAO();
        profilePic = "";
        this.a = a;
    }

    public DBUser(@NonNull Activity a, @NonNull String email, @NonNull String action, @NonNull String gToken) {
        super(a);
        this.action = action;
        user = db.getUserDAO();
        this.email = email;
        profilePic = "";
        this.gToken = gToken;
        this.a = a;
    }

    public DBUser(@NonNull Activity a, @NonNull String email, @NonNull String action, @NonNull LoggedInUser profilePic) {
        super(a);
        this.action = action;
        user = db.getUserDAO();
        this.email = email;
        this.profilePic = profilePic.getProfilePic();
        this.a = a;
    }
    public DBUser(@NonNull Activity a, @NonNull String email, @NonNull String action, @NonNull View v) {
        super(a);
        this.action = action;
        user = db.getUserDAO();
        this.email = email;
        this.profilePic = "";
        this.v = v;
        this.a = a;
    }


    /**
     * Decodifica il valore della stringa base64 che rappresenta l'immagine dell'evento in Bitmap.
     * @return Il valore decodificato in tipo Bitmap
     */
    public Bitmap decodeBase64(@NonNull String profilePic) {
        if(!profilePic.equals("")) {
            try {
                byte[] decodedImg = Base64.decode(profilePic
                        .replace("data:image/png;base64,", "")
                        .replace("data:image/jpeg;base64,",""), Base64.DEFAULT); //Ritorna una stringa in formato Base64
                return BitmapFactory.decodeByteArray(decodedImg, 0, decodedImg.length); //Decodifico la stringa ottenuta
            } catch(IllegalArgumentException ex) {
                //Bad Base64
                byte[] profileBitmap = profilePic.getBytes(StandardCharsets.UTF_16);
                return BitmapFactory.decodeByteArray(profileBitmap, 0, profileBitmap.length);
            }
        }
        return null;
    }

    public void run() {
        synchronized(this) {
            switch(action) {
                case "getProfilePic": {
                    profilePic = user.getProfilePic(email);
                    a.runOnUiThread(() -> {
                        ImageView v1 = v.findViewById(R.id.imageView);
                        Glide.with(v).load(profilePic).circleCrop().into(v1);
                    });
                    break;
                }
                case "updateGToken": {
                    user.updateGToken(gToken, email);
                    break;
                }
                case "updateProfilePic": {
                    user.updateProfilePic(profilePic, email);
                    break;
                }
                case "getAll": {
                    UserInfo u = user.getUser(email);

                    profilePic = u.getString("profilePic");

                    a.runOnUiThread(() -> {
                        //Imposta la schermata del profilo dell'utente
                        ImageView iv = v.findViewById(R.id.profilePic);
                        try {
                            Drawable bitmap = Glide.with(f.requireActivity()).load(profilePic).circleCrop()
                                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                            iv.setImageDrawable(bitmap);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }

                        TextView username = v.findViewById(R.id.username);
                        username.setText(u.getString("nome"));

                        TextView email = v.findViewById(R.id.email);
                        email.setText(f.getString(R.string.user_email, u.getString("email")));

                        TextView phone = v.findViewById(R.id.phone_value);
                        phone.setText(u.getString("tel"));

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
                case "setAll": {
                    UserInfo ui = user.getUser(email);

                    //Aggiorna la riga dell'utente nel database
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
