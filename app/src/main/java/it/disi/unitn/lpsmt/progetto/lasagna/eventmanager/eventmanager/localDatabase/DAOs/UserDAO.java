package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.entities.User;

@Dao
public interface UserDAO {
    @Query("SELECT U.gServerAuthCode FROM Users U WHERE U.email = :email")
    String getServerAuthCode(String email);

    /**
     * Aggiorna il serverAuthCode e il token Google dell'utente.
     * @param gServerAuthCode
     * @param email
     */
    @Query("UPDATE Users SET gServerAuthCode = :gServerAuthCode WHERE email = :email")
    void updateUserServerAuthCode(String gServerAuthCode, String email);

    @Query("UPDATE Users SET profilePic = :profilePic WHERE email = :email")
    void updateProfilePic(String profilePic, String email);

    @Query("UPDATE Users SET gToken = :gToken WHERE email = :email")
    void updateGToken(String gToken, String email);

    @Query("SELECT U.email FROM Users U WHERE U.email = :email")
    String getUserEmail(String email);

    @Query("SELECT U.profilePic, U.nome, U.email, U.tel, U.numEvOrg, U.valutazioneMedia FROM Users U WHERE U.email = :email")
    User getUser(String email);

    @Query("INSERT INTO Users (email, nome, profilePic, gServerAuthCode, gToken, tel, eventiCreati, eventiIscritto, numEvOrg, valutazioneMedia)" +
            "VALUES (:email, :nome, :profilePic, :gServerAuthCode, :gToken, :tel, :eventiCreati, :eventiIscritto, :numEvOrg, :valutazioneMedia)")
    void insert(String email, String nome, String profilePic, String gServerAuthCode, String gToken, String tel, List<String> eventiCreati, List<String> eventiIscritto,
                int numEvOrg, double valutazioneMedia);
}
