package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.location.Address;

import androidx.annotation.NonNull;

import java.util.Locale;

public class LuogoEv extends Address {
    private String address, city, civNum, province;
    private int cap;

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

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCivNum() {
        return civNum;
    }

    public void setCivNum(String civNum) {
        this.civNum = civNum;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getCAP() {
        return cap;
    }

    public void setCAP(int cap) {
        this.cap = cap;
    }

    @NonNull
    public String toString() {
        return address + ", " + civNum + ", " + cap + " " + city + " " + province;
    }
}
