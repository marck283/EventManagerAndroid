package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.onClickListeners;

import android.view.View;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;

import com.google.android.material.textfield.TextInputLayout;
import com.journeyapps.barcodescanner.ScanOptions;

import org.jetbrains.annotations.NotNull;

public class QrCodeOnClickListener implements View.OnClickListener {

    private final TextInputLayout spinner, spinner2;

    private final ActivityResultLauncher<ScanOptions> launcher;

    public QrCodeOnClickListener(@NotNull TextInputLayout s, @NotNull TextInputLayout s2,
                                 @NotNull ActivityResultLauncher<ScanOptions> l) {
        spinner = s;
        spinner2 = s2;
        launcher = l;
    }

    @Override
    public void onClick(View view) {
        EditText editText = spinner.getEditText(), editText1 = spinner2.getEditText();
        if (editText != null &&
                !editText.getText().toString().equals("") &&
                !editText.getText().toString().equals("---") &&
                editText1 != null && !editText.getText().toString().equals("") &&
                !editText.getText().toString().equals("---")) {
            launcher.launch(new ScanOptions());
        }
    }
}
