package com.example.sda.retrofitapp.ui.clients;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sda.retrofitapp.R;
import com.example.sda.retrofitapp.ui.dashboard.DashboardActivty;

/**
 * Created by sda on 01.07.17.
 */

public class ClientsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients, container, false);
        ((DashboardActivty)getActivity()).getSupportActionBar().setTitle(R.string.client_title);
        return view;
    }
}
