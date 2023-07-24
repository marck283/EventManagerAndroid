package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.converters.ListConverter;

@Entity(tableName = "Users")
public class User implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id", defaultValue = "0")
    private String id = "0";

    @NonNull
    @ColumnInfo(name = "email")
    private String email = "";

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "profilePic")
    @Nullable
    private String profilePic;

    @ColumnInfo(name = "tel")
    @Nullable
    private String tel;

    @ColumnInfo(name = "eventiCreati")
    @TypeConverters(ListConverter.class)
    private final List<String> eventiCreati = new ArrayList<>();

    @ColumnInfo(name = "eventiIscritto")
    @TypeConverters(ListConverter.class)
    private final List<String> eventiIscritto = new ArrayList<>();

    @ColumnInfo(name = "numEvOrg")
    private Integer numEvOrg = eventiCreati.size();

    @ColumnInfo(name = "valutazioneMedia")
    private Double valutazioneMedia = 0.0;

    //Getter e setter per permettere a Room di accedere ai campi
    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String e) {
        email = e;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String n) {
        nome = n;
    }

    @Nullable
    public String getProfilePic() {
        return profilePic;
    }

    //Non dovrebbe essere utilizzato se non in casi molto particolari.
    public void setProfilePic(@Nullable String val) {
        profilePic = val;
    }

    @Nullable
    public String getTel() {
        return tel;
    }

    public void setTel(@Nullable String val) {
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

    public Integer getNumEvOrg() {
        return numEvOrg;
    }

    public void setNumEvOrg(Integer val) {
        numEvOrg = val;
    }

    /**
     * Ottiene la valutazione media di questo utente.
     * @return La valutazione media di questo utente.
     */
    public Double getValutazioneMedia() {
        return valutazioneMedia;
    }

    /**
     * Ricalcola la valutazione media di un utente, incrementandola tenendo conto del valore passato come parametro.
     * @param val Il valore di cui tenere conto per ricalcolare la valutazione media dell'utente.
     */
    public void setValutazioneMedia(@NotNull Double val) {
        double temp = valutazioneMedia*numEvOrg;
        if(val == null) {
            valutazioneMedia = temp/numEvOrg;
        } else {
            valutazioneMedia = (temp + val)/numEvOrg;
        }
    }
}
