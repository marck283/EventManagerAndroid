package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TableTextView extends androidx.appcompat.widget.AppCompatTextView {

    public TableTextView(@NonNull Context context) {
        super(context);
        init();
    }

    public TableTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TableTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setPadding(15, 0, 15, 0);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }
}
