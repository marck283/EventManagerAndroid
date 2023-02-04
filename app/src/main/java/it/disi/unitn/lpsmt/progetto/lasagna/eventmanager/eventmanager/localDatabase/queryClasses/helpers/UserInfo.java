package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.helpers;

import androidx.annotation.NonNull;

import java.util.List;

public class UserInfo {

    private String id;
    private String profilePic, nome, email, tel;
    private Integer numEvOrg;
    private Double valutazioneMedia;

    private final List<String> EventiCreati, EventiIscritto;

    public UserInfo(@NonNull String id, @NonNull String profilePic, @NonNull String nome,
                    @NonNull String email, @NonNull String tel, @NonNull Integer numEvOrg,
                    @NonNull Double valutazioneMedia, @NonNull List<String> EventiCreati,
                    @NonNull List<String> EventiIscritto) {
        this.id = id;
        this.profilePic = profilePic;
        this.nome = nome;
        this.email = email;
        this.tel = tel;
        this.numEvOrg = numEvOrg;
        this.valutazioneMedia = valutazioneMedia;
        this.EventiCreati = EventiCreati;
        this.EventiIscritto = EventiIscritto;
    }

    public List<String> getEventiCreati() {
        return EventiCreati;
    }

    public List<String> getEventiIscritto() {
        return EventiIscritto;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setProfilePic(@NonNull String val) {
        profilePic = val;
    }

    public void setNome(@NonNull String val) {
        nome = val;
    }

    public void setEmail(@NonNull String val) {
        email = val;
    }

    public void setTel(@NonNull String val) {
        tel = val;
    }

    public String getString(@NonNull String string) {
        switch(string) {
            case "profilePic": {
                return profilePic;
            }
            case "nome": {
                return nome;
            }
            case "email": {
                return email;
            }
            case "tel": {
                return tel;
            }
        }
        return "";
    }

    public Integer getNumEvOrg() {
        return numEvOrg;
    }

    public void setNumEvOrg(int num) {
        numEvOrg = num;
    }

    public Double getValutazioneMedia() {
        return valutazioneMedia;
    }

    public void setValutazioneMedia(double val) {
        valutazioneMedia = val;
    }
}
