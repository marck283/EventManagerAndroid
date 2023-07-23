package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.matchers;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class Matcher {

    private final Pattern pattern;

    private final String input;

    public Matcher(@NotNull String t, @NotNull String t1) {
        if(t.equals("") || t1.equals("")) {
            throw new IllegalArgumentException("Gli argomenti forniti a questo costruttore non possono " +
                    "essere null o una stringa vuota.");
        }
        input = t1;
        pattern = Pattern.compile(t);
    }

    public boolean isValid() {
        return pattern.matcher(input).find();
    }

    public boolean matches() {
        return pattern.matcher(input).matches();
    }
}
