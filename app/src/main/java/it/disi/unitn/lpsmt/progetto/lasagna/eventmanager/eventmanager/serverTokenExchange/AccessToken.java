package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.serverTokenExchange;

import androidx.lifecycle.MutableLiveData;

public class AccessToken extends MutableLiveData<String> {
    private MutableLiveData<String> token;

    public String getToken() {
        return token.getValue();
    }


}
