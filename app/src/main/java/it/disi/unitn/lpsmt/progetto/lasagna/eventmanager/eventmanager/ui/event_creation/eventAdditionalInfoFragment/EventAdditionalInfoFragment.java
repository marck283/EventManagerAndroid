package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.eventAdditionalInfoFragment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.lasagna.sharedprefs.sharedpreferences.SharedPrefs;
import it.disi.unitn.lasagna.eventcreation.viewmodel.EventViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.special_buttons.ListenerButton;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.speechListeners.SpeechRecognizerInterface;

public class EventAdditionalInfoFragment extends Fragment {

    private EventAdditionalInfoViewModel mViewModel;
    private EventViewModel evm;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private boolean pickerAvailable = false;

    private View view;
    private ActivityResultLauncher<Intent> loginLauncher;

    @NonNull
    @Contract(" -> new")
    public static EventAdditionalInfoFragment newInstance() {
        return new EventAdditionalInfoFragment();
    }

    private String encodeImage(@NonNull Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void setImage(Uri uri) {
        Activity activity = getActivity();
        if(activity != null && isAdded()) {
            if(uri != null) {
                try {
                    final Bitmap selectedImage = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                    updateEventImage(encodeImage(selectedImage));
                } catch(FileNotFoundException ex) {
                    AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
                    dialog.setTitle(R.string.file_not_found);
                    dialog.setMessage(getString(R.string.file_not_found));
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    dialog.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void selectImage(@Nullable ActivityResultLauncher<Intent> launcher) {
        if(launcher == null) {
            //Posso usare PhotoPicker
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        } else {
            //Non posso usare PhotoPicker
            Intent intent = new Intent();
            intent.setType("image/");
            intent.setAction(Intent.ACTION_PICK);

            launcher.launch(intent);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if(ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(requireContext())) {
                pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), this::setImage);
                pickerAvailable = true;
            }

            loginLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), result -> {
                        Activity activity = getActivity();
                        if(result != null && result.getData() != null && activity != null && isAdded()) {
                            SharedPrefs prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok",
                                    requireActivity());
                            String jwt = prefs.getString("accessToken");
                            mViewModel.createPrivateEvent(this, jwt, evm, loginLauncher);
                        }
                    });
        } catch(IllegalStateException ex) {
            if(ex.getMessage() != null) {
                Log.e("Errore", ex.getMessage());
            } else {
                Log.e("Errore", "Qualcosa e' andato storto.");
            }
        }
    }

    private void setOnClickListener(@NotNull FloatingActionButton b, View.OnClickListener listener) {
        if(!b.hasOnClickListeners()) {
            b.setOnClickListener(listener);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.event_creation_additional_information, container, false);

        FloatingActionButton selectImage = view.findViewById(R.id.imageSelector);
        if(selectImage != null) {
            if(pickerAvailable) {
                setOnClickListener(selectImage, c -> selectImage(null));
            } else {
                ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts
                        .StartActivityForResult(), result -> {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        setImage(result.getData().getData());
                    }
                });
                setOnClickListener(selectImage, c -> selectImage(launcher));
            }
        } else {
            Log.i("NoButton", "Nessun bottone con quell'id");
        }

        return view;
    }

    public void updateEventImage(String uri) {
        evm.setBase64Image(uri);
        Log.i("image", uri);
        Toast t;
        Activity activity = getActivity();
        if(activity != null && isAdded()) {
            if(evm.getBase64Image() != null && !evm.getBase64Image().equals("")) {
                t = Toast.makeText(requireActivity(), R.string.add_image_success, Toast.LENGTH_SHORT);
            } else {
                t = Toast.makeText(requireActivity(), R.string.add_image_no_success, Toast.LENGTH_SHORT);
            }
            t.show();
        }
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        Activity activity = getActivity();
        if(activity != null && isAdded()) {
            AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
            dialog.setTitle(title);
            dialog.setMessage(getString(message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        }
    }

    private void createSpeechListener(@IdRes int resId) {
        SpeechRecognizerInterface speechInterface = new SpeechRecognizerInterface(view, resId);
        speechInterface.performClick();
    }

    private void sendRequest(@NonNull View view) {
        //EditText description = view.findViewById(R.id.event_description);
        if(evm.getPrivEvent()) {
            RelativeLayout dhm = view.findViewById(R.id.relativeLayout2);
            dhm.setVisibility(View.INVISIBLE);

            TextView evDuration = view.findViewById(R.id.textView40);
            evDuration.setVisibility(View.INVISIBLE);
        }

        TextInputLayout edInputLayout = view.findViewById(R.id.edInputLayout);
        edInputLayout.setEndIconOnClickListener(c -> createSpeechListener(R.id.event_description));
        TextInputEditText description = edInputLayout.findViewById(R.id.event_description);

        TextInputLayout dInputLayout = view.findViewById(R.id.dInputLayout),
                dhInputLayout = view.findViewById(R.id.dhInputLayout),
                dmInputLayout = view.findViewById(R.id.dmInputLayout);
        dInputLayout.setEndIconOnClickListener(c -> createSpeechListener(R.id.duration_days));
        dhInputLayout.setEndIconOnClickListener(c -> createSpeechListener(R.id.duration_hours));
        dmInputLayout.setEndIconOnClickListener(c -> createSpeechListener(R.id.duration_mins));
        TextInputEditText editGiorni = dInputLayout.findViewById(R.id.duration_days),
                editOre = dhInputLayout.findViewById(R.id.duration_hours),
                editMins = dmInputLayout.findViewById(R.id.duration_mins);

        ListenerButton forward = view.findViewById(R.id.button14);
        forward.setOnClickListener(c -> {
            String giorni = "", ore = "", minuti = "", descrizione = "";
            if(description.getText() != null) {
                descrizione = description.getText().toString();
            }

            if(editGiorni.getText() != null) {
                giorni = editGiorni.getText().toString();
            }
            if(editOre.getText() != null) {
                ore = editOre.getText().toString();
            }
            if(editMins.getText() != null) {
                minuti = editMins.getText().toString();
            }
            evm.setDescription(descrizione);

            String image = evm.getBase64Image();
            if(image == null || image.equals("")) {
                setAlertDialog(R.string.no_event_picture, R.string.missing_event_image);
            } else {
                String description1 = evm.getDescription();
                if(description1 == null || description1.equals("")) {
                    setAlertDialog(R.string.no_event_description, R.string.missing_event_description);
                } else {
                    if(evm.getPrivEvent()) {
                        Activity activity = getActivity();
                        if(activity != null && isAdded()) {
                            SharedPrefs prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok",
                                    requireActivity());
                            mViewModel.createPrivateEvent(this, prefs.getString("accessToken"),
                                    evm, loginLauncher);
                        }
                    } else {
                        try {
                            evm.setGiorni(Integer.parseInt(giorni));
                            evm.setOre(Integer.parseInt(ore));
                            evm.setMinuti(Integer.parseInt(minuti));

                            if(evm.getGiorni() < 0 || evm.getOre() < 0 || evm.getMinuti() < 0) {
                                setAlertDialog(R.string.wrong_duration, R.string.wrong_duration_value);
                            } else {
                                if(evm.getOre() >= 24) {
                                    setAlertDialog(R.string.illegal_hours_value, R.string.illegal_hours_message);
                                } else {
                                    if(evm.getMinuti() >= 60) {
                                        setAlertDialog(R.string.illegal_minutes_value, R.string.illegal_minutes_message);
                                    } else {
                                        Navigation.findNavController(view).navigate(R.id.action_eventAdditionalInfoFragment_to_eventRestrictionsFragment2);
                                    }
                                }
                            }
                        } catch(NumberFormatException ex) {
                            setAlertDialog(R.string.illegal_duration_format, R.string.illegal_duration_format_message);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventAdditionalInfoViewModel.class);
        evm = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        Log.i("private", String.valueOf(evm.getPrivEvent()));

        sendRequest(view);
    }

}