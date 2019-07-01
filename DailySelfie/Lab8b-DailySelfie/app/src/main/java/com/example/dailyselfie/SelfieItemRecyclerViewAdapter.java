package com.example.dailyselfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyselfie.SelfieItemContent.SelfieItem;
import com.example.dailyselfie.SelfieItemFragment.OnListFragmentInteractionListener;

import java.util.List;

public class SelfieItemRecyclerViewAdapter extends RecyclerView.Adapter<SelfieItemRecyclerViewAdapter.ViewHolder> {

    private final List<SelfieItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public SelfieItemRecyclerViewAdapter(List<SelfieItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Bitmap image = BitmapFactory.decodeFile(mValues.get(position).imagePath);

        holder.mItem = mValues.get(position);
        holder.mImageView.setImageBitmap(image);
        holder.mDescriptionView.setText(mValues.get(position).name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) {
                    mListener.onImageClick(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mDescriptionView;
        public SelfieItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.selfie_image);
            mDescriptionView = view.findViewById(R.id.selfie_description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDescriptionView.getText() + "'";
        }
    }
}
