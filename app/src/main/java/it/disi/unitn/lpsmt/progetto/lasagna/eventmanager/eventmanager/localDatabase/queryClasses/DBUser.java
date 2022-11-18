package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.UserDAO;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.helpers.UserInfo;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.model.LoggedInUser;

public class DBUser extends DBThread {
    private String action, email, gToken;
    private UserDAO user;
    private MutableLiveData<String> profilePic;
    private View v;
    private Fragment f;

    public DBUser(@NonNull Activity a, @NonNull String email, @NonNull String action) {
        super(a);
        this.action = action;
        user = db.getUserDAO();
        this.email = email;
        profilePic = new MutableLiveData<>();
    }

    public DBUser(@NonNull Activity a, @NonNull String email, @NonNull String action, @NonNull View v, @NonNull Fragment f) {
        super(a);
        this.email = email;
        this.action = action;
        this.v = v;
        this.f = f;
    }

    public DBUser(@NonNull Activity a, @NonNull String email, @NonNull String action, @NonNull String gToken) {
        super(a);
        this.action = action;
        user = db.getUserDAO();
        this.email = email;
        profilePic = new MutableLiveData<>();
        this.gToken = gToken;
    }

    public DBUser(@NonNull Activity a, @NonNull String email, @NonNull String action, @NonNull LoggedInUser profilePic) {
        super(a);
        this.action = action;
        user = db.getUserDAO();
        this.email = email;
        this.profilePic = new MutableLiveData<>(profilePic.getProfilePic());
    }

    /**
     * Decodifica il valore della stringa che rappresenta l'immagine dell'evento in Bitmap.
     * @return Il valore decodificato in tipo Bitmap
     */
    public Bitmap decodeBase64(@NonNull String profilePic) {
        if(!profilePic.equals("")) {
            byte[] decodedImg = Base64.decode(profilePic
                    .replace("data:image/png;base64,", "")
                    .replace("data:image/jpeg;base64,",""), Base64.DEFAULT); //Ritorna una stringa in formato Base64
            return BitmapFactory.decodeByteArray(decodedImg, 0, decodedImg.length); //Decodifico la stringa ottenuta
        }
        return null;
    }

    public void run() {
        synchronized(this) {
            switch(action) {
                case "getProfilePic": {
                    profilePic = new MutableLiveData<>(user.getProfilePic(email));
                    break;
                }
                case "updateGToken": {
                    user.updateGToken(gToken, email);
                    break;
                }
                case "updateProfilePic": {
                    user.updateProfilePic(profilePic.getValue(), email);
                    break;
                }
                case "getAll": {
                    UserInfo u = user.getUser(email);

                    String userPic = u.getString("profilePic");
                    profilePic.postValue(userPic);

                    //Imposta la schermata del profilo dell'utente
                    ImageView iv = v.findViewById(R.id.profilePic);
                    Glide.with(f.requireActivity()).load(userPic).circleCrop().into(iv);

                    TextView username = v.findViewById(R.id.username);
                    username.setText(u.getString("nome"));

                    TextView email = v.findViewById(R.id.email_value);
                    email.setText(u.getString("email"));

                    TextView phone = v.findViewById(R.id.phone_value);
                    phone.setText(u.getString("tel"));

                    TextView numEvOrg = v.findViewById(R.id.numEvOrg);
                    numEvOrg.setText(f.getString(R.string.numEvOrg, u.getNumEvOrg()));

                    if(u.getNumEvOrg() == 0) {
                        v.findViewById(R.id.rating).setEnabled(false);
                        v.findViewById(R.id.rating).setVisibility(View.INVISIBLE);
                    } else {
                        v.findViewById(R.id.rating).setEnabled(true);
                        v.findViewById(R.id.rating).setVisibility(View.VISIBLE);
                    }

                    break;
                }
            }
            close();
        }
    }

    public LiveData<String> getProfilePic() {
        return profilePic;
    }
}
