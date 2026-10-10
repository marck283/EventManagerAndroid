package it.disi.unitn.lpsmt.lasagna;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Class to list all the supported authentication providers
 */
public class AuthProviders {
    public static final String GOOGLE = "google";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({GOOGLE})
    public @interface Provider {}
}
