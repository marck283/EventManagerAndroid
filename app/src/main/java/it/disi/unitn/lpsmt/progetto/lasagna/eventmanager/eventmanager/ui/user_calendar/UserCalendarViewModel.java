package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.Contract;

import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents.PrivateEvents;

public class UserCalendarViewModel extends ViewModel {

    private MutableLiveData<List<PrivateEvents>> evList;

    public UserCalendarViewModel() {
        evList = new MutableLiveData<>();
    }

    @NonNull
    @Contract(pure = true)
    private String padStart(@NonNull String s) {
        if(s.length() < 2) {
            return "0" + s;
        }
        return s;
    }

    public void getEvents(String authToken, int d, int m, int y, ConstraintLayout l) {
        PrivateEvents privEv = new PrivateEvents();
        privEv.getPersonalEvents(authToken, padStart(String.valueOf(m)) + "-" + padStart(String.valueOf(d)) + "-" + y, l);
    }

}