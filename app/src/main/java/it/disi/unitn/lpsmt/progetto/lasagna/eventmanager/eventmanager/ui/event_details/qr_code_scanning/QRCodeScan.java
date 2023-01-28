package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.qr_code_scanning;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.util.concurrent.ListenableFuture;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.qr_code_scanning.image_analyzer.ImageAnalyzer;

public class QRCodeScan extends Fragment {

    @NonNull
    @Contract(" -> new")
    public static QRCodeScan newInstance() {
        return new QRCodeScan();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_q_r_code_scan, container, false);

        ImageAnalysis analysis = new ImageAnalysis.Builder().build();
        ImageAnalyzer analyzer = new ImageAnalyzer(this);
        analysis.setAnalyzer((Executor) requireActivity(), analyzer);

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        ProcessCameraProvider provider = null;
        try {
            provider = cameraProviderFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        provider.unbindAll();
        //Camera camera = provider.bindToLifecycle(requireContext(), CameraSelector.DEFAULT_BACK_CAMERA, preview, analyzer);

        return root;
    }

}