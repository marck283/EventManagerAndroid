package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.eventAdditionalInfoFragment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Contract;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
        try {
            final InputStream imageStream = requireActivity().getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            updateEventImage(encodeImage(selectedImage));
        } catch(FileNotFoundException ex) {
            AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
            dialog.setTitle(R.string.file_not_found);
            dialog.setMessage(getString(R.string.file_not_found));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        }
    }

    public void selectImage(@Nullable ActivityResultLauncher<Intent> launcher) {
        if(pickerAvailable) {
            //Posso usare PhotoPicker
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType((ActivityResultContracts.PickVisualMedia.VisualMediaType) ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        } else {
            if(launcher != null) {
                //Non posso usare PhotoPicker
                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                launcher.launch(intent);
            } else {
                Log.i("nullLauncher", "Il laucher dell'Activity di selezione immagini è null.");
            }
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
        if(evm.getBase64Image() != null && !evm.getBase64Image().equals("")) {
            Toast t = Toast.makeText(requireActivity(), R.string.add_image_success, Toast.LENGTH_SHORT);
            t.show();
        } else {
            Toast t = Toast.makeText(requireActivity(), R.string.add_image_no_success, Toast.LENGTH_SHORT);
            t.show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventAdditionalInfoViewModel.class);
        evm = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
    }

}