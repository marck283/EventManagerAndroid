package it.disi.unitn.lpsmt.lasagna.login;

import android.content.Intent;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.login.model.LoggedInUser;

public interface AuthenticationInterface {
    void shareData(@NotNull LoggedInUser data, @Nullable Intent intent);

    void logout(@Nullable Intent intent);

    void showNotLoggedInMsg();
}
