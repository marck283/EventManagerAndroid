package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class MenuSettingsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MenuSettingsViewModel() {
        mText = new MutableLiveData<>();
    }

    public void showSettings(View root) {
        TextView t1 = new TextView(root.getContext());
        t1.setText("Mostra numero di telefono");
        ConstraintLayout l = root.findViewById(R.id.constraintLayout);
        l.addView(t1);
    }

    public LiveData<String> getText() {
        return mText;
    }
}