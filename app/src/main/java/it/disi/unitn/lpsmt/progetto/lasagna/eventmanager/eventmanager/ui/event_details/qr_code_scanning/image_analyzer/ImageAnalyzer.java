package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.qr_code_scanning.image_analyzer;

import android.app.AlertDialog;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.annotation.StringRes;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageAnalyzer implements ImageAnalysis.Analyzer {

    private final Fragment f;

    public ImageAnalyzer(@NonNull Fragment f) {
        this.f = f;
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
        dialog.setTitle(title);
        dialog.setMessage(f.getString(message));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    @Override
    @OptIn(markerClass = ExperimentalGetImage.class)
    public void analyze(@NonNull ImageProxy image) {
        Image mediaImage = image.getImage();
        if (mediaImage != null) {
            InputImage image1 = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());

            BarcodeScannerOptions options =  new BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE).build();
            BarcodeScanner scanner = BarcodeScanning.getClient(options);

            Task<List<Barcode>> result = scanner.process(image1)
                    .addOnSuccessListener(barcodes -> {
                        if(barcodes != null) {
                            String rawValue = barcodes.get(0).getRawValue();

                            // Ora invia i dati al server per il riconoscimento del biglietto
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("https://eventmanagerzlf.herokuapp.com/api/v2/QRCodeCheck/" + rawValue)
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    try {
                                        throw e;
                                    } catch(IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) {
                                    switch(response.code()) {
                                        case 200: {
                                            setAlertDialog(R.string.qr_code_ok, R.string.qr_code_ok_message);
                                            break;
                                        }
                                        case 400:
                                        case 404: {
                                            setAlertDialog(R.string.qr_code_invalid, R.string.qr_error_message);
                                            break;
                                        }
                                    }
                                }
                            });
                        }
                        image.close();
                    })
                    .addOnFailureListener(e -> {
                        try {
                            throw e;
                        } catch(Exception ex) {
                            ex.printStackTrace();
                        }
                    });

            try {
                List<Barcode> barList = result.getResult(ApiException.class);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}
