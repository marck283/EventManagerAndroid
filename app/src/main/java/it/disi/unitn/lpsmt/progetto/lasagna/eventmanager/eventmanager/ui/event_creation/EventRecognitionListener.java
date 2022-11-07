package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

import java.util.ArrayList;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class EventRecognitionListener implements RecognitionListener {
    private final EventCreationActivity a;

    public EventRecognitionListener(EventCreationActivity a) {
        this.a = a;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        //Non implementato
    }

    @Override
    public void onBeginningOfSpeech() {
        //Non implementato
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        //Non implementato
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        //Non implementato
    }

    @Override
    public void onEndOfSpeech() {
        //Non implementato
    }

    @Override
    public void onError(int error) {
        //Non implementato
    }

    @Override
    public void onResults(Bundle results) {
        TextView v = a.findViewById(R.id.text);
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        v.setText(data.get(0));
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        //Non implementato
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        //Non implementato
    }
}
