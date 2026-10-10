package it.disi.unitn.lpsmt.lasagna.eventinfo.onClickListeners;

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
                !editText.getText().toString().isEmpty() &&
                !editText.getText().toString().equals("---") &&
                editText1 != null && !editText.getText().toString().isEmpty() &&
                !editText.getText().toString().equals("---")) {
            ScanOptions options = new ScanOptions();
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
            options.setBeepEnabled(false);
            options.setOrientationLocked(true);
            options.setCameraId(0);
            launcher.launch(options);
        }
    }
}
