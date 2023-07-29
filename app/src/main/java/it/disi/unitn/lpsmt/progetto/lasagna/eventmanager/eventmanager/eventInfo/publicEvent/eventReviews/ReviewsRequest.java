package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.eventReviews;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.reviews.ReviewsFragment;
import okhttp3.Request;

public class ReviewsRequest extends ServerOperation {
    private final RecyclerView rv;

    private final ReviewAdapter adapter;

    private final ReviewsFragment f;

    private final String id;

    public ReviewsRequest(@NonNull ReviewsFragment f, @NonNull View layout, @NonNull String id) {
        this.f = f;
        rv = layout.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(layout.getContext());
        rv.setLayoutManager(mLayoutManager);

        adapter = new ReviewAdapter(new ReviewCallback());
        rv.setAdapter(adapter);
        this.id = id;
    }

    public void run() {
        NetworkRequest request = getNetworkRequest();
        Request req = request.getRequest(null, getBaseUrl() + "/api/v2/EventiPubblici/" + id + "/recensioni");
        request.enqueue(req, new ReviewsCallback(adapter, f, rv));
    }
}
