package com.example.sda.retrofitapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sda.retrofitapp.App;
import com.example.sda.retrofitapp.R;
import com.example.sda.retrofitapp.model.CallActivity;
import com.example.sda.retrofitapp.model.Client;
import com.example.sda.retrofitapp.model.Contact;
import com.example.sda.retrofitapp.network.ApiClient;
import com.example.sda.retrofitapp.ui.calls.CallsAdapter;
import com.example.sda.retrofitapp.ui.clients.ClientDetailsActivity;
import com.example.sda.retrofitapp.ui.clients.ClientsAdapter;
import com.example.sda.retrofitapp.ui.contacts.ContactAdapter;
import com.example.sda.retrofitapp.ui.contacts.RealmContactAdapter;
import com.example.sda.retrofitapp.utlis.PrefsManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sda on 25.05.17.
 */

public class MainActivity extends AppCompatActivity implements ClientsAdapter.ClientClickListener, MainMvpView {

    @Inject
    MainPresenter mainPresenter;

    private Realm realm;
    private ClientsAdapter clientsAdapter;
    private CallsAdapter callsAdapter;
    private RealmContactAdapter contactAdapter;
    private RealmResults<Contact> contactRealmResults;

    @BindView(R.id.clients_recycler)
    RecyclerView recycler;
    @BindView(R.id.clients_progress_bar)
    ProgressBar progressBar;

    @OnClick(R.id.getCalls)
    public void downloadCalls() {
        mainPresenter.getCalls();
    }

    @OnClick(R.id.getClients)
    public void downloadClients() {
        mainPresenter.getClients();
    }

    @OnClick(R.id.getContacts)
    public void downloadContacts() {
        mainPresenter.getContacts();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("STATE", "OnCreate");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((App)getApplication()).getAppComponent().inject(this);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clients, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_clients_add) {
            openDetailsView(null);
            return true;
        }
        return false;
    }

    private void init() {
        mainPresenter.attachView(this);
        realm = Realm.getDefaultInstance();
        clientsAdapter = new ClientsAdapter(this);
        callsAdapter = new CallsAdapter();

        contactRealmResults = realm.where(Contact.class)
                .equalTo("name", "Jan")
                .or()
                .endsWith("name", "zl")
                .findAll();
        contactAdapter = new RealmContactAdapter(contactRealmResults, true);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(contactAdapter);

        mainPresenter.setCallsAdapter(callsAdapter);
        mainPresenter.setClientsAdapter(clientsAdapter);
        mainPresenter.setContactAdapter(contactAdapter);
        mainPresenter.setRealm(realm);
    }

    @Override
    protected void onDestroy() {
        mainPresenter.detachView();
        realm.close();
        super.onDestroy();
    }

    @Override
    public void onClientClick(Client client) {
        Toast.makeText(this, client.getName(), Toast.LENGTH_SHORT).show();
        openDetailsView(client);
    }

    @Override
    public void openDetailsView(@Nullable Client client) {
        Intent intent = new Intent(this, ClientDetailsActivity.class);
        intent.putExtra(getString(R.string.extra_client), client);
        startActivity(intent);
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this, getString(R.string.toast_login_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recycler.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        recycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCallsAdapter() {
        recycler.setAdapter(callsAdapter);
    }

    @Override
    public void setClientsAdapter() {
        recycler.setAdapter(clientsAdapter);
    }

    @Override
    public void setContactsAdapter() {
        recycler.setAdapter(contactAdapter);
    }

    @Override
    public void updateRealmAdapter() {
        contactAdapter.updateData(contactRealmResults);
    }
}
