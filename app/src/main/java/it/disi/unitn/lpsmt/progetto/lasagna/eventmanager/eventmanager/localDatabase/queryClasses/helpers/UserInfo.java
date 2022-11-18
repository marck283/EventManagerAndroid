package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.helpers;

import androidx.annotation.NonNull;

public class UserInfo {
    private String profilePic, nome, email, tel;
    private Integer numEvOrg;
    private Double valutazioneMedia;

    public UserInfo(@NonNull String profilePic, @NonNull String nome, @NonNull String email, @NonNull String tel,
                    @NonNull Integer numEvOrg, @NonNull Double valutazioneMedia) {
        this.profilePic = profilePic;
        this.nome = nome;
        this.email = email;
        this.tel = tel;
        this.numEvOrg = numEvOrg;
        this.valutazioneMedia = valutazioneMedia;
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

    public Double getValutazioneMedia() {
        return valutazioneMedia;
    }
}
