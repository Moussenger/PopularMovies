package edu.udacity.mou.project.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.udacity.mou.project.popularmovies.R;
import edu.udacity.mou.project.popularmovies.model.Review;

/**
 * Created by mou on 22/06/16.
 */
public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ReviewHolder> {

    private List<Review> mReviews;
    private Context mContext;

    public ReviewsRecyclerViewAdapter(Context context) {
        mContext = context;
        mReviews = new ArrayList<>();
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);

        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, final int position) {
        final Review review = mReviews.get(position);

        holder.mReviewAuthorTextview.setText(review.getAuthor());
        holder.mReviewContentTextview.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void swap(List<Review> newReviews){
        mReviews.clear();

        if(newReviews != null) {
            mReviews.addAll(newReviews);
        }

        notifyDataSetChanged();
    }

    public static class ReviewHolder extends RecyclerView.ViewHolder {
        public final TextView mReviewAuthorTextview;
        public final TextView mReviewContentTextview;

        public ReviewHolder(View view) {
            super(view);
            mReviewAuthorTextview = (TextView)  view.findViewById(R.id.review_author);
            mReviewContentTextview = (TextView) view.findViewById(R.id.review_content);
        }
    }
}
