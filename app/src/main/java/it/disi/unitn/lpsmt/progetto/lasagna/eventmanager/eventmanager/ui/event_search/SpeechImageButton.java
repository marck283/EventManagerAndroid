package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_search;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;

import java.util.Locale;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.speechListeners.EventSpeechRecognizer;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.speechListeners.SpeechOnTouchListener;

public class SpeechImageButton extends AppCompatImageButton {
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    public SpeechImageButton(Context context) {
        super(context);
    }

    public SpeechImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpeechImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setupImageButton(@NonNull View v, @IdRes int resId) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(v.getContext());
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer.setRecognitionListener(new EventSpeechRecognizer(v, resId));
        setOnTouchListener(new SpeechOnTouchListener(speechRecognizer, speechRecognizerIntent));
    }

    public void destroy() {
        speechRecognizer.destroy();
        speechRecognizerIntent = null;
    }
}
