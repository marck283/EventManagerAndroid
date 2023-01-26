package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.eventAdditionalInfoFragment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Contract;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventViewModel;

public class EventAdditionalInfoFragment extends Fragment {

    private EventAdditionalInfoViewModel mViewModel;
    private EventViewModel evm;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private boolean pickerAvailable = false;

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

    public void selectImage(@Nullable ActivityResultLauncher<Intent> launcher) {
        if(launcher == null) {
            //Posso usare PhotoPicker
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType((ActivityResultContracts.PickVisualMedia.VisualMediaType) ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        } else {
            //Non posso usare PhotoPicker
            Intent intent = new Intent();
            intent.setType("image/jpg");
            intent.setAction(Intent.ACTION_PICK);

            launcher.launch(intent);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable()) {
            pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), this::setImage);
            pickerAvailable = true;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_creation_additional_information, container, false);

        FloatingActionButton selectImage = view.findViewById(R.id.imageSelector);
        if(selectImage != null) {
            if(pickerAvailable) {
                selectImage.setOnClickListener(c -> selectImage(null));
            } else {
                ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts
                        .StartActivityForResult(), result -> {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        setImage(result.getData().getData());
                    }
                });
                selectImage.setOnClickListener(c -> selectImage(launcher));
            }
        } else {
            Log.i("NoButton", "Nessun bottone con quell'id");
        }

        return view;
    }

    public void updateEventImage(String uri) {
        evm.setBase64Image(uri);
        Log.i("image", uri);
        if(evm.getBase64Image() != null && !evm.getBase64Image().equals("")) {
            Toast t = Toast.makeText(requireActivity(), R.string.add_image_success, Toast.LENGTH_SHORT);
            t.show();
        } else {
            Toast t = Toast.makeText(requireActivity(), R.string.add_image_no_success, Toast.LENGTH_SHORT);
            t.show();
        }
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
        dialog.setTitle(title);
        dialog.setMessage(getString(message));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventAdditionalInfoViewModel.class);
        evm = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        Log.i("private", String.valueOf(evm.getPrivEvent()));

        EditText description = view.findViewById(R.id.event_description);
        if(evm.getPrivEvent()) {
            description.setVisibility(View.INVISIBLE);
        }

        Button forward = view.findViewById(R.id.button14);
        forward.setOnClickListener(c -> {
            String giorni, ore, minuti, descrizione = description.getText().toString();
            EditText editGiorni = view.findViewById(R.id.duration_days), editOre = view.findViewById(R.id.duration_hours),
            editMins = view.findViewById(R.id.duration_mins);
            giorni = editGiorni.getText().toString();
            ore = editOre.getText().toString();
            minuti = editMins.getText().toString();
            evm.setDescription(descrizione);

            try {
                evm.setGiorni(Integer.parseInt(giorni));
                evm.setOre(Integer.parseInt(ore));
                evm.setMinuti(Integer.parseInt(minuti));
            } catch(NumberFormatException ex) {
                setAlertDialog(R.string.illegal_duration_format, R.string.illegal_duration_format_message);
            }

            String image = evm.getBase64Image();
            if(image == null || image.equals("")) {
                setAlertDialog(R.string.no_event_picture, R.string.missing_event_image);
            } else {
                String description1 = evm.getDescription();
                if(!evm.getPrivEvent() && (description1 == null || description1.equals(""))) {
                    setAlertDialog(R.string.no_event_description, R.string.missing_event_description);
                } else {
                    if(evm.getGiorni() < 0 || evm.getOre() < 0 || evm.getMinuti() < 0) {
                        setAlertDialog(R.string.wrong_duration, R.string.wrong_duration_value);
                    } else {
                        if(evm.getOre() >= 24) {
                            setAlertDialog(R.string.illegal_hours_value, R.string.illegal_hours_message);
                        } else {
                            if(evm.getMinuti() >= 60) {
                                setAlertDialog(R.string.illegal_minutes_value, R.string.illegal_minutes_message);
                            } else {
                                if(!evm.getPrivEvent()) {
                                    Navigation.findNavController(view).navigate(R.id.action_eventAdditionalInfoFragment_to_eventRestrictionsFragment2);
                                } else {
                                    //Poiché l'evento è privato, fai partire la sua creazione da qui...
                                    SharedPreferences prefs = requireActivity().getSharedPreferences("AccTok", Context.MODE_PRIVATE);
                                    mViewModel.createPrivateEvent(this, prefs.getString("accessToken", ""), evm);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

}