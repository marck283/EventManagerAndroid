package it.disi.unitn.lasagna.eventmanager.ui_extra.spinnerImplementation;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.ArrayRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpinnerArrayAdapter extends ArrayAdapter<CharSequence> {

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     */
    public SpinnerArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public SpinnerArrayAdapter(@NonNull Context context, int resource, @NonNull List<CharSequence> objects) {
        super(context, resource, objects);
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     */
    public SpinnerArrayAdapter(@NonNull Context context, int resource, int textViewResourceId) {
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
    public SpinnerArrayAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
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
    public SpinnerArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[] objects) {
        super(context, resource, textViewResourceId, objects);
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
    public SpinnerArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<CharSequence> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public void add(String string) {
        super.add(string);
    }

    @NonNull
    public static SpinnerArrayAdapter createFromResources(@NonNull Activity c, @ArrayRes int arrayList,
                                                          @LayoutRes int textViewResId, @LayoutRes int dropDownViewResource) {
        CharSequence[] arr = c.getResources().getTextArray(arrayList);
        List<CharSequence> arrList = new ArrayList<>(Arrays.asList(arr));
        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(c.getApplicationContext(), textViewResId, arrList);
        adapter.setDropDownViewResource(dropDownViewResource);
        return adapter;
    }
}
