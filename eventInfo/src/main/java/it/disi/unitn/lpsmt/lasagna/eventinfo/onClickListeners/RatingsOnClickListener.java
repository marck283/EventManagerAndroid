package it.disi.unitn.lpsmt.lasagna.eventinfo.onClickListeners;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.navigation.Navigation;

import org.jetbrains.annotations.NotNull;

public class RatingsOnClickListener implements View.OnClickListener {

    private final String screenType, eventId;

    private final int evDetFragToRevFrag;

    public RatingsOnClickListener(@NotNull String screenType, @NotNull String eventId, @IdRes int toRevFrag) {
        if(screenType.equals("") || eventId.equals("")) {
            throw new IllegalArgumentException("Nessuno degli argomenti forniti al costruttore puo' essere null.");
        }

        this.screenType = screenType;
        this.eventId = eventId;
        evDetFragToRevFrag = toRevFrag;
    }

    @Override
    public void onClick(View view) {
        Bundle b1 = new Bundle();
        b1.putString("screenType", screenType);
        b1.putString("eventId", eventId);
        Navigation.findNavController(view).navigate(evDetFragToRevFrag, b1);
    }
}
