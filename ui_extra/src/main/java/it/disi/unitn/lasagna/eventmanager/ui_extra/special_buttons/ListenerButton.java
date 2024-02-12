package it.disi.unitn.lasagna.eventmanager.ui_extra.special_buttons;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

public class ListenerButton extends MaterialButton {
    public ListenerButton(Context context) {
        super(context);
    }

    public ListenerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if(!hasOnClickListeners()) {
            super.setOnClickListener(l);
        }
    }
}
