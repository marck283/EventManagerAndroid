package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.reviews;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class ReviewsFragment extends Fragment {

    private ReviewsViewModel mViewModel1;
    private String id = "", screenType = "";
    private View root;

    @NonNull
    @Contract(" -> new")
    public static ReviewsFragment newInstance() {
        return new ReviewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        if(b != null) {
            id = b.getString("eventId");
            screenType = b.getString("screenType");
        }

        root = inflater.inflate(R.layout.fragment_reviews, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel1 = new ViewModelProvider(this).get(ReviewsViewModel.class);
    }

    public void onStart() {
        super.onStart();
        mViewModel1.getReviews(this, root.findViewById(R.id.frameLayout5), id, screenType);
    }

}