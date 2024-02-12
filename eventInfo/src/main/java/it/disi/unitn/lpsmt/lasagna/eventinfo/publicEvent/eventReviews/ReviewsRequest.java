package it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.eventReviews;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import okhttp3.Request;

public class ReviewsRequest extends ServerOperation {
    private final RecyclerView rv;

    private final ReviewAdapter adapter;

    private final Fragment f;

    private final String id;

    private final int norevs, norevsmsg, revSmallLayout, username, userName, userRating, userEval,
            userPicture, showAll, revFragToFullRevFrag;

    public ReviewsRequest(@NonNull Fragment f, @NonNull View layout, @NonNull String id,
                          @IdRes int recyclerView, @StringRes int norevs, @StringRes int norevsmsg,
                          @LayoutRes int revSmallLayout,
                          @IdRes int username,
                          @IdRes int userRating, @IdRes int userPicture, @StringRes int userName,
                          @StringRes int userEval, @IdRes int showAll, @NavigationRes int revFragToFullRevFrag) {
        this.f = f;
        rv = layout.findViewById(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(layout.getContext());
        rv.setLayoutManager(mLayoutManager);

        adapter = new ReviewAdapter(new ReviewCallback());
        rv.setAdapter(adapter);
        this.id = id;
        this.norevs = norevs;
        this.norevsmsg = norevsmsg;
        this.revSmallLayout = revSmallLayout;
        this.username = username;
        this.userRating = userRating;
        this.userPicture = userPicture;
        this.userName = userName;
        this.userEval = userEval;
        this.showAll = showAll;
        this.revFragToFullRevFrag = revFragToFullRevFrag;
    }

    public void run() {
        NetworkRequest request = getNetworkRequest();
        Request req = request.getRequest(null, getBaseUrl() + "/api/v2/EventiPubblici/" + id + "/recensioni");
        request.enqueue(req, new ReviewsCallback(adapter, f, rv, norevs, norevsmsg, revSmallLayout,
                username, userRating, userPicture, userName, userEval, showAll, revFragToFullRevFrag));
    }
}
