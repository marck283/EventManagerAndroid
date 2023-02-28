package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.entities.User;

@Dao
public interface UserDAO {

    @Query("SELECT U.id, U.profilePic, U.nome, U.email, U.tel, U.numEvOrg, U.valutazioneMedia FROM Users U WHERE U.id = :id")
    User getUser(@NonNull String id);

    @Insert
    void insert(User u);

    @Query("UPDATE Users SET nome = :nome, email = :email, tel = :tel, profilePic = :profilePic," +
            "eventiCreati = :eventiCreati, eventiIscritto = :eventiIscritto, numEvOrg = :numEvOrg," +
            "valutazioneMedia = :valutazioneMedia WHERE id = :id")
    void updateUserProfile(@NonNull String id, @NonNull String nome, @NonNull String email,
                           @NonNull String tel, @NonNull String profilePic,
                           @NonNull List<String> eventiCreati, @NonNull List<String> eventiIscritto,
                           @NonNull Integer numEvOrg, @NonNull Double valutazioneMedia);
}
