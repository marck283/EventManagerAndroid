package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.speechListeners;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import java.util.Locale;

public class SpeechRecognizerInterface {

    private final SpeechOnTouchListener speech;

    private final SpeechRecognizer rec;

    public SpeechRecognizerInterface(@NonNull View view, @IdRes int resId) {
        rec = SpeechRecognizer.createSpeechRecognizer(view.getContext());
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        rec.setRecognitionListener(new EventSpeechRecognizer(view, resId));

        speech = new SpeechOnTouchListener(rec, speechRecognizerIntent);
    }

    public void performClick() {
        speech.performClick();
    }

    public void destroy() {
        rec.destroy();
    }
}
