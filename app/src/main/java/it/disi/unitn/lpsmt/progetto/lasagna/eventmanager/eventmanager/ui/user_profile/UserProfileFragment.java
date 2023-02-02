package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings.MenuSettingsViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;

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

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        ms = new ViewModelProvider(requireActivity()).get(MenuSettingsViewModel.class);
        prefs = requireActivity().getSharedPreferences("AccTok", Context.MODE_PRIVATE);

        TextView username = v.findViewById(R.id.username), email = v.findViewById(R.id.email),
                phone = v.findViewById(R.id.phone_value), numEvOrg = v.findViewById(R.id.numEvOrg);
        username.setText(getString(R.string.username, ""));
        email.setText(getString(R.string.user_email, ""));
        phone.setText(getString(R.string.phone, ""));
        numEvOrg.setText(getString(R.string.numEvOrg, 0));

        String token = prefs.getString("accessToken", "");
        if(!token.equals("")) {
            Bundle b = new Bundle();
            b.putString("userJwt", token);
            v.findViewById(R.id.eventManaging).setOnClickListener(c -> Navigation.findNavController(v)
                    .navigate(R.id.action_nav_user_profile_to_eventManagement, b));
            mViewModel.getUserInfo(this, prefs.getString("accessToken", ""), v.findViewById(R.id.frameLayout2));
        } else {
            ActivityResultLauncher<Intent> loginLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), result -> {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            mViewModel.getUserInfo(this, prefs.getString("accessToken", ""),
                                    v.findViewById(R.id.frameLayout2));
                        } else {
                            Navigation.findNavController(v).navigate(R.id.action_nav_user_profile_to_nav_event_list);
                        }
                    });
            AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
            dialog.setTitle(R.string.user_not_logged_in);
            dialog.setMessage(getString(R.string.user_not_logged_in_message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
                //Autentica l'utente e ritorna il risultato, poi gestisci il risultato nel Fragment
                Intent loginIntent = new Intent(requireActivity(), LoginActivity.class);
                loginLauncher.launch(loginIntent);
            });
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        }
    }

    public void onStart() {
        super.onStart();

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