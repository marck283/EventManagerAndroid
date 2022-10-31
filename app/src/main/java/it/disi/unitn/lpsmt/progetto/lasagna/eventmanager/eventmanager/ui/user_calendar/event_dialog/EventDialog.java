package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar.event_dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class EventDialog extends DialogFragment {
    private int d, m, y;
    private String idToken;
    private EventDialogViewModel vm;
    private View root;

    public static EventDialog newInstance(String idToken, int d, int m, int y) {
        EventDialog ev = new EventDialog();

        Bundle b = new Bundle();
        b.putString("idToken", idToken);
        b.putInt("day", d);
        b.putInt("month", m);
        b.putInt("year", y);
        ev.setArguments(b);

        return ev;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args != null) {
            idToken = args.getString("idToken");
            d = args.getInt("day");
            m = args.getInt("month");
            y = args.getInt("year");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(EventDialogViewModel.class);

        root = inflater.inflate(R.layout.fragment_calendar_dialog, container, false);
        return root;
    }

    public void onStart() {
        super.onStart();
        vm.getEvents(idToken, d, m, y, root.findViewById(R.id.dialog_constraintLayout));
    }

    public void showDialog(FragmentTransaction t) {
        show(t, "UserCalendarDialog");
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
        setArguments(null);
    }
}
