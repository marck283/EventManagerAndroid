package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.location.Address;

import androidx.annotation.NonNull;

import java.util.Locale;

public class LuogoEv extends Address {
    private final String address, city, civNum, province;
    private final int cap;

    public LuogoEv(String a, String c, String civNum, String province, int cap) {
        super(Locale.ITALY);
        address = a;
        city = c;
        this.civNum = civNum;
        this.province = province;
        this.cap = cap;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCivNum() {
        return civNum;
    }

    public String getProvince() {
        return province;
    }

    public int getCAP() {
        return cap;
    }

    @NonNull
    public String toString() {
        return address + ", " + civNum + ", " + cap + " " + city + " " + province;
    }
}
