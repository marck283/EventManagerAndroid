package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings.MenuSettingsViewModel;

public class UserProfileFragment extends Fragment {

    private UserProfileViewModel mViewModel;
    private NavigationSharedViewModel ns;
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

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
    }

    public void onStart() {
        super.onStart();
        ns = new ViewModelProvider(requireActivity()).get(NavigationSharedViewModel.class);
        mViewModel.getUserInfo(this, ns.getToken().getValue(), v.findViewById(R.id.frameLayout2));
        ms = new ViewModelProvider(requireActivity()).get(MenuSettingsViewModel.class);

        ms.getChecked().observe(requireActivity(), o -> {
            if(!((boolean) o)) {
                v.findViewById(R.id.phone_text_box).setVisibility(View.INVISIBLE);
                v.findViewById(R.id.phone_value).setVisibility(View.INVISIBLE);
            } else {
                v.findViewById(R.id.phone_text_box).setVisibility(View.VISIBLE);
                v.findViewById(R.id.phone_value).setVisibility(View.VISIBLE);
            }
        });
    }

}