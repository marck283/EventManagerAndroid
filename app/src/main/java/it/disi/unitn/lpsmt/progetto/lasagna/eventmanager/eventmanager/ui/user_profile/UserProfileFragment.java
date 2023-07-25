package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_profile;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.icu.text.MessageFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.sharedpreferences.SharedPrefs;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings.MenuSettingsViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.special_buttons.ListenerButton;

public class UserProfileFragment extends Fragment {

    private UserProfileViewModel mViewModel;
    private MenuSettingsViewModel ms;
    private View v;

    @NonNull
    @Contract(" -> new")
    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        return v;
    }

    private void toEventManagement(@NonNull String token) {
        Bundle b = new Bundle();
        b.putString("userJwt", token);

        ListenerButton evManaging = v.findViewById(R.id.eventManaging);
        evManaging.setOnClickListener(c -> Navigation.findNavController(v)
                .navigate(R.id.action_nav_user_profile_to_eventManagement, b));
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        ms = new ViewModelProvider(requireActivity()).get(MenuSettingsViewModel.class);

        TextView username = v.findViewById(R.id.username), user_email = v.findViewById(R.id.email);
        username.setText(getString(R.string.username, ""));
        user_email.setText(getString(R.string.user_email, ""));

        TextView phone_value = v.findViewById(R.id.phone_value), numEvOrg = v.findViewById(R.id.numEvOrg);
        phone_value.setText(getString(R.string.phone, ""));

        String text = MessageFormat.format(getString(R.string.numEvOrg), 0, null);
        numEvOrg.setText(text);
    }

    public void onStart() {
        super.onStart();

        Activity activity = getActivity();
        if(activity != null && isAdded()) {
            SharedPrefs prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok",
                    requireActivity());

            String token = prefs.getString("accessToken");
            if(!token.equals("")) {
                toEventManagement(token);
            }

            mViewModel.getUserInfo(this, prefs.getString("accessToken"), v.findViewById(R.id.frameLayout2));

            ms.getChecked().observe(requireActivity(), o -> {
                if(!((boolean) o)) {
                    v.findViewById(R.id.phone_value).setVisibility(View.INVISIBLE);
                } else {
                    v.findViewById(R.id.phone_value).setVisibility(View.VISIBLE);
                }
            });

            //Il nome della SharedPreference qui utilizzata dovrebbe essere
            //it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.MenuSettingsSharedPreferences

            //Ne consegue anche qui che il nome qui fornito dovrebbe essere modificato per tutte le
            //istanze di SharedPreferences che richiamano questa specifica Shared Preference.
            SharedPrefs sp = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.MenuSettingsSharedPreferences",
                    requireActivity());
            if(sp.getBoolean("showTel")) {
                v.findViewById(R.id.phone_value).setVisibility(View.VISIBLE);
            } else {
                v.findViewById(R.id.phone_value).setVisibility(View.INVISIBLE);
            }
        }
    }

}