package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.List;

public class DropdownSpinnerAdapter extends ArrayAdapter<CharSequence> {

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    private DropdownSpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     */
    public DropdownSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    /**
     * Constructor. This constructor will result in the underlying data collection being
     * immutable, so methods such as {@link #clear()} will throw an exception.
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    private DropdownSpinnerAdapter(@NonNull Context context, int resource, @NonNull CharSequence[] objects) {
        super(context, resource, objects);
    }

    /**
     * Constructor. This constructor will result in the underlying data collection being
     * immutable, so methods such as {@link #clear()} will throw an exception.
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects            The objects to represent in the ListView.
     */
    private DropdownSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull CharSequence[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    private DropdownSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<CharSequence> objects) {
        super(context, resource, objects);
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects            The objects to represent in the ListView.
     */
    private DropdownSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<CharSequence> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public void create(@LayoutRes int textLayoutViewId) {
        setDropDownViewResource(textLayoutViewId);
    }
}
