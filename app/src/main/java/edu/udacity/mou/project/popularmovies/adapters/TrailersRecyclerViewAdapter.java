package edu.udacity.mou.project.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.udacity.mou.project.popularmovies.R;
import edu.udacity.mou.project.popularmovies.model.Trailer;
import edu.udacity.mou.project.popularmovies.utils.IntentUtils;
import edu.udacity.mou.project.popularmovies.utils.MovieUtils;

/**
 * Created by mou on 22/06/16.
 */
public class TrailersRecyclerViewAdapter extends RecyclerView.Adapter<TrailersRecyclerViewAdapter.TrailerHolder> {

    private List<Trailer> mTrailers;
    private Context mContext;

    public TrailersRecyclerViewAdapter(Context context) {
        mContext = context;
        mTrailers = new ArrayList<>();
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false);

        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, final int position) {
        final Trailer trailer = mTrailers.get(position);

        holder.mTrailerTitleTextView.setText(trailer.getName());
        setThumbImage(holder, trailer.getKey());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.watchTrailer(mContext, trailer.getKey());
            }
        });
    }

    private void setThumbImage (TrailerHolder holder, String key) {
        Picasso.with(mContext)
                .load(MovieUtils.getTrailerThumbUri(mContext, key))
                .placeholder(R.drawable.ic_trailer)
                .error(R.drawable.ic_trailer)
                .fit()
                .centerInside()
                .into(holder.mTrailerThumbImageView);

    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public void swap(List<Trailer> newTrailers){
        mTrailers.clear();

        if(newTrailers != null) {
            mTrailers.addAll(newTrailers);
        }

        notifyDataSetChanged();
    }

    public static class TrailerHolder extends RecyclerView.ViewHolder {
        public final TextView mTrailerTitleTextView;
        public final ImageView mTrailerThumbImageView;
        public final View mView;

        public TrailerHolder(View view) {
            super(view);
            mView = view;
            mTrailerThumbImageView = (ImageView)  view.findViewById(R.id.trailer_thumb);
            mTrailerTitleTextView = (TextView) view.findViewById(R.id.trailer_name);
        }
    }
}
