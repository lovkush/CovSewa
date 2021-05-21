package com.vector.CovSewa.Activities.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vector.CovSewa.Activities.RequestDetails;
import com.vector.CovSewa.R;
import com.vector.CovSewa.RequestData;

import java.io.Serializable;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements RequestListAdapter.OnCardClickListener{
    RecyclerView showReviewList;

    //  private OnFragmentInteractionListener mListener;

    private static final String ARG_PARAM_USER_LIST = "requestList";
    private List<RequestData> requestList;

    public PlaceholderFragment() {
        // Required empty public constructor
    }

    RequestListAdapter listAdapter;
    public static PlaceholderFragment newInstance(List<RequestData> requestList1) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_USER_LIST, (Serializable) requestList1);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestList = (List<RequestData>) getArguments().getSerializable(ARG_PARAM_USER_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_recieved, container, false);
        defineView(view);
        return view;
    }

    private void defineView(View view) {
        showReviewList = view.findViewById(R.id.RequestRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        showReviewList.setLayoutManager(layoutManager);
        listAdapter = new RequestListAdapter(requestList,this);
        showReviewList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(getActivity(), RequestDetails.class);
        intent.putExtra("id",requestList.get(position));
        startActivity(intent);
    }
}
