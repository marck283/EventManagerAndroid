package it.disi.unitn.lpsmt.lasagna.eventinfo.fields;

import androidx.annotation.StringRes;

public class DialogState {
    private final @StringRes int titleRes;
    private final @StringRes int messageRes;

    public DialogState(@StringRes int titleRes, @StringRes int messageRes) {
        this.titleRes = titleRes;
        this.messageRes = messageRes;
    }

    public @StringRes int getTitleRes() {
        return titleRes;
    }

    public @StringRes int getMessageRes() {
        return messageRes;
    }
}
