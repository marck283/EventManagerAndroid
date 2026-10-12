package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.concurrent.FutureTask;

import it.disi.lasagna.navigationsvm.NavigationSharedViewModel;
import it.disi.unitn.lasagna.eventcreation.helpers.LuogoEv;
import it.disi.unitn.lasagna.eventmanager.geocoder.GeocoderExt;
import it.disi.unitn.lasagna.eventmanager.ui_extra.special_buttons.ListenerButton;
import it.disi.unitn.lasagna.eventmanager.ui_extra.spinnerImplementation.SpinnerArrayAdapter;
import it.disi.unitn.lasagna.futuretask.futuretaskext.FutureTaskExt;
import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;
import it.disi.unitn.lpsmt.lasagna.eventinfo.interfaces.EventDetailsInterface;
import it.disi.unitn.lpsmt.lasagna.eventinfo.onClickListeners.AnnullaEventoOnClickListener;
import it.disi.unitn.lpsmt.lasagna.eventinfo.onClickListeners.QrCodeOnClickListener;
import it.disi.unitn.lpsmt.lasagna.eventinfo.onClickListeners.RatingsOnClickListener;
import it.disi.unitn.lpsmt.lasagna.eventinfo.onClickListeners.SignUpOnClickListener;
import it.disi.unitn.lpsmt.lasagna.eventinfo.onClickListeners.TerminaEventoOnClickListener;
import it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent.RegisteredEvent;
import it.disi.unitn.lpsmt.lasagna.localdatabase.queryClasses.DBOrgEvents;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallback;
import it.disi.unitn.lpsmt.lasagna.sharedprefs.sharedpreferences.SharedPrefs;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.qr_code_scan.QRCodeRenderingFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;

public class EventDetailsFragment extends Fragment implements EventDetailsInterface {

    private EventDetailsViewModel mViewModel;
    private NavigationSharedViewModel nvm;
    private String day, time;

    //Indica il tipo della schermata (ad esempio "iscr" per utente iscritto, od "org" per "organizzatore")
    private String screenType;
    private String eventId;

    private ActivityResultLauncher<ScanOptions> launcher;

    private ActivityResultLauncher<Intent> loginLauncher, loginLauncher1;

    private SharedPrefs prefs;

    private String token = "";

    private View view;

    private NetworkCallback callback;

    private TextInputLayout spinner, spinner2;

    private final Bundle taskBundle = new Bundle();
    private final FutureTaskExt<Void> task = new FutureTaskExt<>(() -> {
        QRCodeRenderingFragment destination = QRCodeRenderingFragment.newInstance(taskBundle);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        destination.show(transaction, "QRCodeRenderingFragment");
        return null;
    }, taskBundle);

    public void setEventId(@NonNull String val) {
        eventId = val;
    }

    public void setDay(@NonNull String day) {
        this.day = day;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    @NonNull
    public static EventDetailsFragment newInstance() {
        return new EventDetailsFragment();
    }

    private void executeCallback(FutureTask<Void> ft) {
        if (callback.isOnline(requireActivity())) {
            ft.run();
        } else {
            setDialog(R.string.no_connection, R.string.no_connection_message);
            callback.registerNetworkCallback();
            callback.addDefaultNetworkActiveListener(ft::run);
            callback.unregisterNetworkCallback();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = new SharedPrefs(requireActivity().getApplicationContext());
        token = prefs.getString("accessToken");

        int noconn = R.string.no_connection, noconnmsg = R.string.no_connection_message;
        loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK -> {
                            token = prefs.getString("accessToken");
                            if (!token.isEmpty()) {
                                if (day != null) {
                                    day = String.join("-", day.split("/"));
                                } else {
                                    day = "";
                                }
                                mViewModel.registerUser(token, eventId, this, day, time != null ? time : "", null,
                                        noconn, noconnmsg, LoginActivity.class);
                            } else {
                                AlertDialog d = new AlertDialog.Builder(requireContext()).create();
                                d.setTitle(R.string.log_in);
                                d.setMessage(getString(R.string.log_in));
                                d.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                            }
                        }
                        case Activity.RESULT_CANCELED -> {
                            prefs.setString("accessToken", "");
                            prefs.apply();

                            Activity activity = getActivity();
                            if (activity != null && isAdded()) {
                                ((NavigationDrawerActivity) requireActivity())
                                        .updateUI("logout", "", "", null, false);
                                Navigation.findNavController(view).navigate(R.id.action_eventDetailsFragment_to_nav_event_list);
                            }
                        }
                    }
                });

        Bundle b = getArguments();
        if (b != null) {
            screenType = b.getString("eventType");
            eventId = b.getString("eventId");
            day = b.getString("day");
        } else {
            screenType = "pub";
            eventId = "";
            day = "";
        }

        launcher = registerForActivityResult(new ScanContract(),
                result -> {
                    if (result.getContents() != null && spinner != null && spinner.getEditText() != null &&
                            spinner.getEditText().getText() != null &&
                            !spinner.getEditText().getText().toString().equals("---") &&
                            spinner2 != null && spinner2.getEditText() != null &&
                            spinner2.getEditText().getText() != null &&
                            !spinner2.getEditText().getText().toString().equals("---")) {
                        String token = prefs.getString("accessToken");
                        FutureTask<Void> qr = new FutureTask<>(() -> Void.TYPE.cast(mViewModel.checkQR(
                                result.getContents(),
                                eventId, spinner2.getEditText().getText().toString(),
                                spinner.getEditText().getText().toString(), this,
                                R.string.valid_qr_code, R.string.valid_qr_code_message,
                                noconn, noconnmsg, R.string.qr_code_invalid,
                                R.string.invalid_qr_code_message, R.string.malformed_request,
                                R.string.malformed_request_message, R.string.user_not_logged_in,
                                R.string.user_not_logged_in_message)));
                        if (!token.isEmpty()) {
                            executeCallback(qr);
                        }
                    }
                });

        if(screenType != null && screenType.equals("iscr")) {
            loginLauncher1 = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        switch (result.getResultCode()) {
                            case Activity.RESULT_OK -> mViewModel.getEventInfoIscr(this, prefs.getString("accessToken"),
                                noconn, noconnmsg, day, eventId, null,
                                LoginActivity.class);
                            case Activity.RESULT_CANCELED -> {
                                //Ritorna alla schermata principale, reimpostando il token alla stringa vuota
                                //e chiedendo all'Activity NavigationDrawerActivity di reimpostare il suo menù
                                //a quello riservato agli utenti non autenticati.
                                prefs.setString("accessToken", "");
                                Navigation.findNavController(view).navigate(R.id.action_eventDetailsFragment_to_nav_event_list);
                                ((NavigationDrawerActivity) requireActivity())
                                        .updateUI("logout", "", "", null,
                                                false);
                            }
                        }
                    });
        } else {
            if(screenType != null && screenType.equals("org")) {
                FutureTask<Void> ft = new FutureTask<>(() -> {
                    mViewModel.getEventInfo("org", eventId, this, prefs.getString("accessToken"),
                            null, LoginActivity.class, noconn, noconnmsg);
                    return null;
                });
                loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            switch (result.getResultCode()) {
                                case Activity.RESULT_OK -> {
                                    Activity activity = getActivity();
                                    if (activity != null && isAdded()) {
                                        executeCallback(ft);
                                    }
                                }
                                case Activity.RESULT_CANCELED -> {
                                    Activity activity = getActivity();
                                    if (activity != null && isAdded()) {
                                        prefs.setString("accessToken", "");
                                        Navigation.findNavController(view).navigate(R.id.action_eventDetailsFragment_to_nav_event_list);
                                        ((NavigationDrawerActivity) requireActivity())
                                                .updateUI("logout", "", "", null,
                                                        false);
                                    }
                                }
                            }
                        });
            }
        }

        //Aggiungo le transizioni del Fragment
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        switch (screenType) {
            case "pub" -> view = inflater.inflate(R.layout.public_event_info, container, false);
            case "iscr" -> view = inflater.inflate(R.layout.dettagli_evento_iscritto, container, false);
            case "org" -> view = inflater.inflate(R.layout.dettagli_evento_organizzatore, container, false);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventDetailsViewModel.class);
        nvm = new ViewModelProvider(requireActivity()).get(NavigationSharedViewModel.class);

        mViewModel.getEventInfo().observe(getViewLifecycleOwner(), eventInfo -> {
            if (eventInfo == null) return;

            ImageView image = view.findViewById(R.id.imageView3);
            Glide.with(view).load(eventInfo.getEventPic()).into(image);

            TextView titleView = view.findViewById(R.id.textView6);
            if (titleView != null) {
                titleView.setText(eventInfo.getNomeAtt());
            }

            TextView orgView = view.findViewById(R.id.textView16);
            if (orgView != null) {
                orgView.setText(eventInfo.getOrgName());
            }

            TextView dayView = view.findViewById(R.id.textView11);
            TextView timeView = view.findViewById(R.id.textView20);
            MaterialAutoCompleteTextView dayText = view.findViewById(R.id.orgDateTextView);
            MaterialAutoCompleteTextView timeText = view.findViewById(R.id.orgHourTextView);

            // 1. Populate the Date Spinner List
            ArrayList<CharSequence> dateArr = new ArrayList<>();
            dateArr.add("---");
            dateArr.addAll(eventInfo.getLuoghi());
            if (dayText != null) {
                dayText.setAdapter(new SpinnerArrayAdapter(requireContext(), R.layout.list_item, dateArr));
                dayText.setEnabled(true);

                dayText.setOnItemClickListener((parent, view1, position, id) -> {
                    String selectedDate = parent.getItemAtPosition(position).toString();
                    if (!selectedDate.equals("---")) {
                        if (dayView != null) {
                            dayView.setText(getString(R.string.day_not_selectable, selectedDate));
                            setDay(selectedDate);
                        }

                        // Convert MM/DD/YYYY to YYYY-MM-DD for eventInfo queries
                        String[] parts = selectedDate.split("/");
                        String isoDate = parts[1] + "-" + parts[0] + "-" + parts[2];

                        // 2. Populate Time Spinner List based on selected Date
                        if (timeText != null) {
                            ArrayList<CharSequence> timeArr = new ArrayList<>();
                            timeArr.add("---");
                            timeArr.addAll(eventInfo.getOrari(isoDate));
                            timeText.setAdapter(new SpinnerArrayAdapter(requireContext(), R.layout.list_item, timeArr));
                            timeText.setEnabled(true);

                            timeText.setOnItemClickListener((parent1, view2, pos2, id2) -> {
                                String selectedTime = parent1.getItemAtPosition(pos2).toString();
                                if (!selectedTime.equals("---")) {
                                    if (timeView != null) {
                                        timeView.setText(getString(R.string.time_not_selectable, selectedTime));
                                        setTime(selectedTime);
                                    }

                                    // 3. Show Address and Duration for selected Date + Time
                                    LuogoEv luogo = eventInfo.getLuogo(isoDate, selectedTime);
                                    if (luogo != null) {
                                        TextView addrView = view.findViewById(R.id.textView15); // or R.id.textView15
                                        if (addrView != null) {
                                            addrView.setPaintFlags(addrView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                            addrView.setText(getString(R.string.event_address, luogo.getAddress()));
                                            addrView.setOnClickListener(l -> {
                                                GeocoderExt geocoder = new GeocoderExt(this, addrView);
                                                geocoder.fromLocationName(addrView.getText().toString(), 1);
                                            });
                                        }

                                        TextView durView = view.findViewById(R.id.textView12); // or R.id.textView39
                                        if (durView != null && eventInfo.getDurata() != null) {
                                            String[] sDurata = eventInfo.getDurata().split(":");
                                            durView.setText(getString(R.string.duration, sDurata[0], sDurata[1], sDurata[2]));
                                        }
                                    }
                                } else {
                                    if (timeView != null) {
                                        timeView.setText(getString(R.string.time_not_selectable, ""));
                                    }
                                }
                            });
                        }
                    } else {
                        if (dayView != null) {
                            dayView.setText(getString(R.string.day_not_selectable, ""));
                        }
                        if (timeText != null) {
                            timeText.setAdapter(new SpinnerArrayAdapter(requireContext(), R.layout.list_item, new ArrayList<>()));
                            timeText.setText("");
                        }
                    }
                });
            }
        });

        mViewModel.getDialogState().observe(getViewLifecycleOwner(), dialog -> {
            if (dialog != null) {
                setDialog(dialog.getTitleRes(), dialog.getMessageRes());
            }
        });

        ListenerButton deleteTicket = view.findViewById(R.id.button11);
        if (deleteTicket != null && !deleteTicket.hasOnClickListeners()) {
            deleteTicket.setOnClickListener(c -> {
                RegisteredEvent eventInfo = (RegisteredEvent) mViewModel.getEventInfo().getValue();
                mViewModel.deleteTicket(prefs.getString(SharedPrefs.PREF_NAME), eventInfo.getTicketId(),
                        eventId, this, day,
                        time, R.string.no_connection, R.string.no_connection_message);
                    });
        }

        ListenerButton showQrCode = view.findViewById(R.id.button9);
        if (showQrCode != null && !showQrCode.hasOnClickListeners()) {
            showQrCode.setOnClickListener(c -> task.run());
        }
    }

    public void onStart() {
        super.onStart();

        int noconn = R.string.no_connection, noconnmsg = R.string.no_connection_message;
        switch (screenType) {
            case "pub" -> {
                mViewModel.getEventInfo("pub", eventId, this, null,
                        null, LoginActivity.class, noconn, noconnmsg);

                ListenerButton b = view.findViewById(R.id.cLayout).findViewById(R.id.sign_up_button);
                b.setEnabled(false);
                b.setOnClickListener(new SignUpOnClickListener(prefs, eventId, day, time, mViewModel,
                        loginLauncher, this, noconn, noconnmsg, R.id.spinner, R.id.dateArray, LoginActivity.class));

                ((TextView) view.findViewById(R.id.event_address)).setText(getString(R.string.event_address, ""));
                ((TextView) view.findViewById(R.id.duration)).setText(getString(R.string.duration, "", "", ""));
                ((TextView) view.findViewById(R.id.organizerName)).setText(getString(R.string.organizer, ""));

                ListenerButton ratings = view.findViewById(R.id.show_ratings);
                ratings.setOnClickListener(new RatingsOnClickListener(screenType, eventId, R.id.action_eventDetailsFragment_to_reviewsFragment));
            }
            case "iscr" -> {
                TextView organizer = view.findViewById(R.id.textView16), dayTextView = view.findViewById(R.id.textView11),
                        time = view.findViewById(R.id.textView20), duration = view.findViewById(R.id.textView39),
                        address = view.findViewById(R.id.textView42);
                organizer.setText(getString(R.string.organizer, ""));
                dayTextView.setText(getString(R.string.day_not_selectable, ""));
                time.setText(getString(R.string.time_not_selectable, ""));
                duration.setText(getString(R.string.duration, "", "", ""));
                address.setText(getString(R.string.event_address, ""));

                mViewModel.getEventInfoIscr(this, prefs.getString("accessToken"),
                        noconn, noconnmsg, day, eventId, loginLauncher1,
                        LoginActivity.class);
            }
            case "org" -> {
                TextView duration = view.findViewById(R.id.textView12);
                duration.setText(getString(R.string.duration, "", "", ""));

                TextView address = view.findViewById(R.id.textView15);
                address.setText(getString(R.string.event_address, ""));

                spinner = view.findViewById(R.id.spinner);
                spinner2 = view.findViewById(R.id.spinner2);

                ListenerButton qrCodeScan = view.findViewById(R.id.button8),
                        terminaEvento = view.findViewById(R.id.button12),
                        annullaEvento = view.findViewById(R.id.button13);

                Activity activity = getActivity();
                if (activity != null && isAdded()) {
                    callback = new NetworkCallback(requireActivity());
                    if (callback.isOnline(requireActivity())) {
                        mViewModel.getEventInfo("org", eventId, this, prefs.getString("accessToken"),
                                loginLauncher, LoginActivity.class, noconn, noconnmsg);
                    } else {
                        //Nessuna connessione ad Internet. Acquisire i dati dal database e visualizzarli a schermo
                        setDialog(noconn, R.string.buttons_disabled);

                        qrCodeScan.setEnabled(false);
                        terminaEvento.setEnabled(false);
                        annullaEvento.setEnabled(false);

                        DBOrgEvents events = new DBOrgEvents(this, eventId, view);
                        events.start();

                        callback.registerNetworkCallback();
                        callback.addDefaultNetworkActiveListener(() -> {
                            qrCodeScan.setEnabled(true);
                            terminaEvento.setEnabled(true);
                            annullaEvento.setEnabled(true);
                        });
                        callback.unregisterNetworkCallback();
                    }
                }

                qrCodeScan.setOnClickListener(new QrCodeOnClickListener(spinner, spinner2, launcher));

                Intent loginIntent = new Intent(requireContext(), LoginActivity.class);
                terminaEvento.setOnClickListener(new TerminaEventoOnClickListener(spinner, spinner2,
                        this, mViewModel, token, eventId, callback, loginLauncher, loginIntent,
                        R.id.orgHourTextView));

                annullaEvento.setOnClickListener(new AnnullaEventoOnClickListener(this, nvm, callback,
                        mViewModel, eventId, noconn, noconnmsg));
            }
        }
    }

    private void setDialog(@StringRes int title, @StringRes int message) {
        Activity activity = getActivity();
        if(activity != null && isAdded()) {
            AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
            dialog.setTitle(title);
            dialog.setMessage(getString(message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        }
    }

    public EventDetailsViewModel getViewModel() {
        return mViewModel;
    }

}