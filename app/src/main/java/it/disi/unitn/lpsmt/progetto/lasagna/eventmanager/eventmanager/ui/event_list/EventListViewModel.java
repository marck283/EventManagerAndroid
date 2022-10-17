package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.PublicEvents;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent.PublicEvent;

public class EventListViewModel extends ViewModel {

    private MutableLiveData<List<PublicEvent>> peList;
    private MutableLiveData<GoogleSignInAccount> account;
    private PublicEvents pubEv;

    public EventListViewModel() {
        peList = new MutableLiveData<>();
        account = new MutableLiveData<>();
        peList.setValue(new ArrayList<>());
    }

    public void getEvents(@NonNull View layout, @NonNull Fragment fragment) {
        pubEv = new PublicEvents(layout);

        account.setValue(GoogleSignIn.getLastSignedInAccount(fragment.requireContext()));
        account.observe(fragment, a -> {
            if(a == null) {
                pubEv.getEvents((ConstraintLayout) layout, null, null, null, null, null, null);
            } else {
                Log.i("token", a.getIdToken());
                pubEv.getEvents((ConstraintLayout) layout, a.getIdToken(), null, null, null, null, null);
            }
        });
    }
}