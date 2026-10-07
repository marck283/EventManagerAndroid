package it.disi.unitn.lpsmt.lasagna;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Class to list all the supported authentication providers
 */
public class AuthProviders {
    public static final String GOOGLE = "google";
    public static final String FACEBOOK = "facebook";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({GOOGLE, FACEBOOK})
    public @interface Provider {}
}
