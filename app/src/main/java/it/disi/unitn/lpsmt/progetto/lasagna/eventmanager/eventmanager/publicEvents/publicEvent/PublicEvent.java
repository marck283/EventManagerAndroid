package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

public class PublicEvent implements Parcelable {
    private String id, eventid, self, name, category, eventPic;

    public PublicEvent(String id, String evId, String s, String n, String c, String ep) {
        this.id = id;
        eventid = evId;
        self = s;
        name = n;
        category = c;
        eventPic = ep;
    }

    protected PublicEvent(Parcel in) {
        id = in.readString();
        eventid = in.readString();
        self = in.readString();
        name = in.readString();
        category = in.readString();
        eventPic = in.readString();
    }

    public static final Creator<PublicEvent> CREATOR = new Creator<PublicEvent>() {
        @Override
        public PublicEvent createFromParcel(Parcel in) {
            return new PublicEvent(in);
        }

        @Override
        public PublicEvent[] newArray(int size) {
            return new PublicEvent[size];
        }
    };

    public String getString(@NonNull String field) throws Exception {
        switch(field) {
            case "id": {
                return id;
            }
            case "eventid": {
                return eventid;
            }
            case "self": {
                return self;
            }
            case "name": {
                return name;
            }
            case "category": {
                return category;
            }
            case "eventPic": {
                return eventPic;
            }
            default: {
                Log.i("field", field);
                throw new Exception("No field using this name.");
            }
        }
    }

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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(eventid);
        dest.writeString(self);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(eventPic);
    }
}
