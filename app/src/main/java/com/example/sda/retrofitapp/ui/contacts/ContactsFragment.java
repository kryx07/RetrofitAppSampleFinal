package com.example.sda.retrofitapp.ui.contacts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sda.retrofitapp.App;
import com.example.sda.retrofitapp.R;
import com.example.sda.retrofitapp.model.Contact;
import com.example.sda.retrofitapp.ui.dashboard.DashboardActivty;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by sda on 01.07.17.
 */

public class ContactsFragment extends android.support.v4.app.Fragment implements ContactsMvpView {

    public static final String TAG = ContactsFragment.class.getClass().getName();

    public static ContactsFragment newInstance(int id) {
        // We cannot use custom constructor - this is a way to pass some data to a fragment
        Bundle args = new Bundle();
        args.putInt(TAG, id);
        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    ContactsPresenter presenter;

    private Realm realm;
    private RealmContactAdapter adapter;

    @BindView(R.id.contacts_recycler)
    RecyclerView recycler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, view);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        realm = Realm.getDefaultInstance();
        presenter.attach(this);
        presenter.setRealm(realm);

        adapter = new RealmContactAdapter(null, true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);

        // Set fragment name in activity toolbar
        ((DashboardActivty)getActivity()).getSupportActionBar().setTitle(R.string.contacts_title);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    public void onDestroyView() {
        realm.close();
        presenter.detach();
        super.onDestroyView();
    }

    @Override
    public void updateData() {
        adapter.updateData(presenter.getAllContacts());
    }
}
