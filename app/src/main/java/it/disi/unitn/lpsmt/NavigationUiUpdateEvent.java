package it.disi.unitn.lpsmt;

import androidx.annotation.NonNull;

public record NavigationUiUpdateEvent(String fieldName, String fieldValue) {
    public NavigationUiUpdateEvent(@NonNull String fieldName, @NonNull String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    @Override
    @NonNull
    public String fieldName() {
        return fieldName;
    }

    @Override
    @NonNull
    public String fieldValue() {
        return fieldValue;
    }
}
