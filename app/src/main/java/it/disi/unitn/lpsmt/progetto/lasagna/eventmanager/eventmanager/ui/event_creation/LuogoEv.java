package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

public class LuogoEv {
    private String address, city, civNum, province;
    private int cap;

    public LuogoEv(String a, String c, String civNum, String province, int cap) {
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
}
