package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.content.Intent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class SpeechOnTouchListener implements View.OnTouchListener {
    private final SpeechRecognizer speechRecognizer;
    private final Intent speechRecognizerIntent;

    public SpeechOnTouchListener(SpeechRecognizer t, Intent i) {
        speechRecognizer = t;
        speechRecognizerIntent = i;
    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, @NonNull MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                return performClick();
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                speechRecognizer.stopListening();
            }
        }
        return false;
    }

    public boolean performClick() {
        speechRecognizer.startListening(speechRecognizerIntent);
        return false;
    }
}
