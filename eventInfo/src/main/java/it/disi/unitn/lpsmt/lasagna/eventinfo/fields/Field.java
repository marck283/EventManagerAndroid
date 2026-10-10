package it.disi.unitn.lpsmt.lasagna.eventinfo.fields;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

public class Field {
    private @NonNull String fieldId;
    private @NonNull String fieldValue;

    public Field() {
        this.fieldId = "";
        this.fieldValue = "";
    }

    public @NonNull String getFieldId() {
        return fieldId;
    }

    public void setFieldId(@NonNull String fieldId) {
        this.fieldId = fieldId;
    }

    public @NonNull String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(@NonNull String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
