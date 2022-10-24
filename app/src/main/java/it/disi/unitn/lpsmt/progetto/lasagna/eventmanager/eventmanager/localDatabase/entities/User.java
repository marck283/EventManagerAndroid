package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "Users")
public class User {
    @PrimaryKey
    private String email = ""; //Chiave primaria

    private String nome = "", profilePic = "";
    private String gServerAuthCode = "", gToken = "";
    private String tel = "";
    private List<String> eventiCreati = new ArrayList<>();
    private List<String> eventiIscritto = new ArrayList<>();
    private int numEvOrg = 0;
    private double valutazioneMedia = 0.0;

    //Getter e setter per permettere a Room di accedere ai campi
    public String getEmail() {
        return email;
    }

    public void setEmail(String e) {
        email = e;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String n) {
        nome = n;
    }

    public String getProfilePic() {
        return profilePic;
    }

    //Non dovrebbe essere utilizzato se non in casi molto particolari.
    public void setProfilePic(String val) {
        profilePic = val;
    }

    public String getGoogleServerAuthCode() {
        return gServerAuthCode;
    }

    public void setGoogleServerAuthCode(String code) {
        gServerAuthCode = code;
    }

    public String getGoogleToken() {
        return gToken;
    }

    public void setgToken(String token) {
        gToken = token;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String val) {
        tel = val;
    }

    public List<String> getEventiCreati() {
        return eventiCreati;
    }

    public void setEventiCreati(List<String> val) {
        eventiCreati.clear();
        eventiCreati.addAll(val);
    }

    public List<String> getEventiIscritto() {
        return eventiIscritto;
    }

    public void setEventiIscritto(List<String> val) {
        eventiIscritto.clear();
        eventiIscritto.addAll(val);
    }

    public int getNumEvOrg() {
        return numEvOrg;
    }

    public void setNumEvOrg() {
        numEvOrg += 1;
    }

    public double getValutazioneMedia() {
        return valutazioneMedia;
    }

    /**
     * Ricalcola la valutazione media di un utente, incrementandola tenendo conto del valore passato come parametro.
     * @param val Il valore di cui tenere conto per ricalcolare la valutazione media dell'utente.
     */
    public void setValutazioneMedia(double val) {
        double temp = valutazioneMedia*numEvOrg;
        valutazioneMedia = (temp + val)/numEvOrg;
    }
}
