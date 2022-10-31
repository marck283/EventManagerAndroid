package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.entities.User;

@Dao
public interface UserDAO {
    @Query("SELECT U.gServerAuthCode FROM Users U WHERE U.email = :email")
    String getServerAuthCode(String email);

    /**
     * Aggiorna il serverAuthCode dell'utente.
     * @param gServerAuthCode
     * @param email
     */
    @Query("UPDATE Users SET gServerAuthCode = :gServerAuthCode WHERE email = :email")
    void updateUserServerAuthCode(String gServerAuthCode, String email);

    @Query("UPDATE Users SET profilePic = :profilePic WHERE email = :email")
    void updateProfilePic(String profilePic, String email);

    @Query("SELECT U.profilePic FROM Users U WHERE U.email = :email")
    String getProfilePic(@NonNull String email);

    @Query("UPDATE Users SET gToken = :gToken WHERE email = :email")
    void updateGToken(String gToken, String email);

    @Query("SELECT U.email FROM Users U WHERE U.email = :email")
    String getUserEmail(String email);

    @Query("SELECT U.profilePic, U.nome, U.email, U.tel, U.numEvOrg, U.valutazioneMedia FROM Users U WHERE U.email = :email")
    User getUser(String email);

    @Insert
    void insert(User u);

    @Query("SELECT U.gToken FROM Users U WHERE U.email = :email")
    String getGToken(String email);
}
