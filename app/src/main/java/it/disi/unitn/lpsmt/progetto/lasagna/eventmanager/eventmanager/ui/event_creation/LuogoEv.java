package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.location.Address;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LuogoEv extends Address {
    private String address, city, civNum, province, data, ora;
    private int cap, maxPers;

    public LuogoEv(String a, String c, String civNum, String province, int cap, String data, String ora) {
        super(Locale.ITALY);
        address = a;
        city = c;
        this.civNum = civNum;
        this.province = province;
        this.cap = cap;
        this.data = data;
        this.ora = ora;
    }

    public LuogoEv(String a, String c, String civNum, String province, int cap, String data, String ora, int maxPers) {
        super(Locale.ITALY);
        address = a;
        city = c;
        this.civNum = civNum;
        this.province = province;
        this.cap = cap;
        this.data = data;
        this.ora = ora;
        this.maxPers = maxPers;
    }

    public JSONObject toJSON(boolean priv) throws JSONException {
        JSONObject json = new JSONObject();

        json.put("indirizzo", address);
        json.put("civNum", civNum);
        json.put("cap", cap);
        json.put("citta", city);
        json.put("provincia", province);
        if(!priv) {
            json.put("maxPers", maxPers);
        }
        json.put("data", data);
        json.put("ora", ora);

        return json;
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

    public String getData() {
        return data;
    }

    public void setData(@NonNull String data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(@NonNull String ora) {
        this.ora = ora;
    }

    public int getPosti() {
        return maxPers;
    }

    public void setPosti(int posti) {
        maxPers = posti;
    }

    @NonNull
    public String toString() {
        return address + ", " + civNum + ", " + cap + " " + city + " " + province;
    }
}
