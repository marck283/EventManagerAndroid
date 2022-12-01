package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.spinnerImplementation;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import androidx.annotation.ArrayRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class SpinnerItemList extends androidx.appcompat.widget.AppCompatSpinner {
    private final SpinnerOnItemSelectedListener listener;
    private SpinnerArrayAdapter adapter;

    /**
     * Construct a new spinner with the given context's theme.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public SpinnerItemList(@NonNull Context context) {
        super(context);
        listener = new SpinnerOnItemSelectedListener();
    }

    public SpinnerItemList(@NonNull Context context, int mode) {
        super(context, mode);
        listener = new SpinnerOnItemSelectedListener();
    }

    /**
     * Construct a new spinner with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public SpinnerItemList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        listener = new SpinnerOnItemSelectedListener();
    }

    /**
     * Construct a new spinner with the given context's theme, the supplied attribute set,
     * and default style attribute.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     */
    public SpinnerItemList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        listener = new SpinnerOnItemSelectedListener();
    }

    public SpinnerItemList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        listener = new SpinnerOnItemSelectedListener();
    }

    public SpinnerItemList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        listener = new SpinnerOnItemSelectedListener();
    }

    public SpinnerOnItemSelectedListener getListener() {
        return listener;
    }

    public void setAdapter(@NonNull Fragment f, @LayoutRes int layoutRes, @IdRes int idRes, List<CharSequence> items) {
        adapter = new SpinnerArrayAdapter(f.requireContext(), layoutRes, idRes, items);
        super.setAdapter(adapter);
    }

    public void setAdapter(@NonNull Fragment f, @ArrayRes int arrayList, @LayoutRes int textViewResId,
                           @LayoutRes int dropDownViewResource) {
        adapter = SpinnerArrayAdapter.createFromResources(f.requireActivity(), arrayList, textViewResId, dropDownViewResource);
        super.setAdapter(adapter);
    }
}
