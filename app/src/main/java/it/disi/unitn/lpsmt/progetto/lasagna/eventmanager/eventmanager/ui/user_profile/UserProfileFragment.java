package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_profile;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings.MenuSettingsViewModel;

public class UserProfileFragment extends Fragment {

    private UserProfileViewModel mViewModel;
    private MenuSettingsViewModel ms;
    private View v;

    private SharedPreferences prefs;

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

        MaterialButton evManaging = v.findViewById(R.id.eventManaging);
        /*evManaging.setEnabled(true);*/
        evManaging.setOnClickListener(c -> Navigation.findNavController(v)
                .navigate(R.id.action_nav_user_profile_to_eventManagement, b));
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        ms = new ViewModelProvider(requireActivity()).get(MenuSettingsViewModel.class);
        prefs = requireActivity().getSharedPreferences("AccTok", Context.MODE_PRIVATE);

        String token = prefs.getString("accessToken", "");
        if(!token.equals("")) {
            toEventManagement(token);
        }
    }

    public void onStart() {
        super.onStart();

        mViewModel.getUserInfo(this, prefs.getString("accessToken", ""), v.findViewById(R.id.frameLayout2));

        ms.getChecked().observe(requireActivity(), o -> {
            if(!((boolean) o)) {
                v.findViewById(R.id.phone_value).setVisibility(View.INVISIBLE);
            } else {
                v.findViewById(R.id.phone_value).setVisibility(View.VISIBLE);
            }
        });

        SharedPreferences sp = requireActivity().getSharedPreferences("MenuSettingsSharedPreferences", Context.MODE_PRIVATE);
        if(sp.getBoolean("showTel", false)) {
            v.findViewById(R.id.phone_value).setVisibility(View.VISIBLE);
        } else {
            v.findViewById(R.id.phone_value).setVisibility(View.INVISIBLE);
        }
    }

}