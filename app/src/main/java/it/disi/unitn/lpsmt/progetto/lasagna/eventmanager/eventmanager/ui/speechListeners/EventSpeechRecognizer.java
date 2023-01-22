package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.speechListeners;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class EventSpeechRecognizer implements RecognitionListener {
    private final View a;
    private final int resId;

    public EventSpeechRecognizer(@NonNull View a, @IdRes int resId) {
        this.a = a;
        this.resId = resId;
    }

    /**
     * Called when the endpointer is ready for the user to start speaking.
     *
     * @param params parameters set by the recognition service. Reserved for future use.
     */
    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    /**
     * The user has started to speak.
     */
    @Override
    public void onBeginningOfSpeech() {

    }

    /**
     * The sound level in the audio stream has changed. There is no guarantee that this method will
     * be called.
     *
     * @param rmsdB the new RMS dB value
     */
    @Override
    public void onRmsChanged(float rmsdB) {

    }

    /**
     * More sound has been received. The purpose of this function is to allow giving feedback to the
     * user regarding the captured audio. There is no guarantee that this method will be called.
     *
     * @param buffer a buffer containing a sequence of big-endian 16-bit integers representing a
     *               single channel audio stream. The sample rate is implementation dependent.
     */
    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    /**
     * Called after the user stops speaking.
     */
    @Override
    public void onEndOfSpeech() {

    }

    /**
     * A network or recognition error occurred.
     *
     * @param error code is defined in {@link SpeechRecognizer}. Implementations need to handle any
     *              integer error constant to be passed here beyond constants prefixed with ERROR_.
     */
    @Override
    public void onError(int error) {

    }

    /**
     * Called when recognition results are ready.
     *
     * <p>
     * Called with the results for the full speech since {@link #onReadyForSpeech(Bundle)}.
     * To get recognition results in segments rather than for the full session see
     * {@link RecognizerIntent#EXTRA_SEGMENTED_SESSION}.
     * </p>
     *
     * @param results the recognition results. To retrieve the results in {@code
     *                ArrayList<String>} format use {@link Bundle#getStringArrayList(String)} with
     *                {@link SpeechRecognizer#RESULTS_RECOGNITION} as a parameter. A float array of
     *                confidence values might also be given in {@link SpeechRecognizer#CONFIDENCE_SCORES}.
     */
    @Override
    public void onResults(@NonNull Bundle results) {
        TextInputEditText v = a.findViewById(resId);
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        v.setText(data.get(0));
    }

    /**
     * Called when partial recognition results are available. The callback might be called at any
     * time between {@link #onBeginningOfSpeech()} and {@link #onResults(Bundle)} when partial
     * results are ready. This method may be called zero, one or multiple times for each call to
     * {@link SpeechRecognizer#startListening(Intent)}, depending on the speech recognition
     * service implementation.  To request partial results, use
     * {@link RecognizerIntent#EXTRA_PARTIAL_RESULTS}
     *
     * @param partialResults the returned results. To retrieve the results in
     *                       ArrayList&lt;String&gt; format use {@link Bundle#getStringArrayList(String)} with
     *                       {@link SpeechRecognizer#RESULTS_RECOGNITION} as a parameter
     */
    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    /**
     * Reserved for adding future events.
     *
     * @param eventType the type of the occurred event
     * @param params    a Bundle containing the passed parameters
     */
    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}
