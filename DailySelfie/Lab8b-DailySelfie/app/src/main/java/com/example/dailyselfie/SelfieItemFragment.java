package com.example.dailyselfie;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyselfie.SelfieItemContent.SelfieItem;

public class SelfieItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private SelfieItemRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public SelfieItemFragment() {
        // Required empty public constructor
    }

    public static SelfieItemFragment newInstance(int columnCount) {
        SelfieItemFragment fragment = new SelfieItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mAdapter = new SelfieItemRecyclerViewAdapter(SelfieItemContent.ITEMS, mListener);
            mRecyclerView.setAdapter(mAdapter);

            DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(),
                    DividerItemDecoration.VERTICAL);
            mRecyclerView.addItemDecoration(itemDecor);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    public void notifyDataChanged() {
        if (mAdapter != null) {
            mAdapter.notifyItemInserted(mAdapter.getItemCount());
            mRecyclerView.scrollToPosition(mAdapter.getItemCount());
        }
    }

    public void notifyClear() {
        int size = mAdapter.getItemCount();
        mAdapter.notifyItemRangeRemoved(0,size);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(SelfieItem item);
    }
}
