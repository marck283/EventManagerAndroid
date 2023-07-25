package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.eventReviews;

import java.io.Serializable;
import java.util.Objects;

/**
 * @param rating Rating in decimi
 */
public record Review(String userName, String userPic, String description, float rating) implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Float.compare(review.rating, rating) == 0 && userName.equals(review.userName) && userPic.equals(review.userPic) && description.equals(review.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, userPic, description, rating);
    }
}
