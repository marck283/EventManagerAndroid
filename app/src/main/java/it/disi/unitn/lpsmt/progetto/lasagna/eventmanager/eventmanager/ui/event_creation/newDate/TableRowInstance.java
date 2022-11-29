package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class TableRowInstance implements Parcelable {
    private String data, ora, luogo, posti;

    public TableRowInstance(@NonNull String data, @NonNull String ora, @NonNull String luogo, @NonNull String posti) {
        this.data = data;
        this.ora = ora;
        this.luogo = luogo;
        this.posti = posti;
    }

    public String getData() {
        return data;
    }

    public String getOra() {
        return ora;
    }

    public String getLuogo() {
        return luogo;
    }

    public String getPosti() {
        return posti;
    }

    protected TableRowInstance(@NonNull Parcel in) {
        data = in.readString();
        ora = in.readString();
        luogo = in.readString();
        posti = in.readString();
    }

    public static final Creator<TableRowInstance> CREATOR = new Creator<>() {
        @NonNull
        @Contract("_ -> new")
        @Override
        public TableRowInstance createFromParcel(Parcel in) {
            return new TableRowInstance(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public TableRowInstance[] newArray(int size) {
            return new TableRowInstance[size];
        }
    };

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(data);
        dest.writeString(ora);
        dest.writeString(luogo);
        dest.writeString(posti);
    }
}
