package com.example.sda.retrofitapp.ui.main;

import android.widget.Toast;

import com.example.sda.retrofitapp.R;
import com.example.sda.retrofitapp.model.CallActivity;
import com.example.sda.retrofitapp.model.Client;
import com.example.sda.retrofitapp.model.Contact;
import com.example.sda.retrofitapp.network.ApiClient;
import com.example.sda.retrofitapp.ui.calls.CallsAdapter;
import com.example.sda.retrofitapp.ui.clients.ClientsAdapter;
import com.example.sda.retrofitapp.ui.contacts.RealmContactAdapter;
import com.example.sda.retrofitapp.ui.login.LoginMvpView;
import com.example.sda.retrofitapp.utlis.PrefsManager;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sda on 26.06.17.
 */

public class MainPresenter {

    private MainMvpView view;
    private ApiClient apiClient;
    private PrefsManager prefsManager;
    private CallsAdapter callsAdapter;
    private RealmContactAdapter contactAdapter;
    private ClientsAdapter clientsAdapter;
    private Realm realm;

    @Inject
    public MainPresenter(ApiClient apiClient, PrefsManager prefsManager) {
        this.apiClient = apiClient;
        this.prefsManager = prefsManager;
    }

    public void attachView(MainMvpView view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
        if (!realm.isClosed()) realm.close();
    }

    public void setCallsAdapter(CallsAdapter callsAdapter) {
        this.callsAdapter = callsAdapter;
    }

    public void setContactAdapter(RealmContactAdapter contactAdapter) {
        this.contactAdapter = contactAdapter;
    }

    public void setClientsAdapter(ClientsAdapter clientsAdapter) {
        this.clientsAdapter = clientsAdapter;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public void getCalls() {
        view.showProgress();
        view.setCallsAdapter();
        apiClient.getService().getActivities()
                .enqueue(new Callback<List<CallActivity>>() {
                    @Override
                    public void onResponse(Call<List<CallActivity>> call, Response<List<CallActivity>> response) {
                        if (response.isSuccessful()) {
                            callsAdapter.setData(response.body());
                        } else {
                            view.showErrorMessage();
                        }
                        view.hideProgress();
                    }

                    @Override
                    public void onFailure(Call<List<CallActivity>> call, Throwable t) {
                        // Need to handle the error
                        view.hideProgress();
                        view.showErrorMessage();
                    }
                });
    }

    public void getClients() {
        view.showProgress();
        view.setClientsAdapter();
        apiClient.getService().getClients().enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful()) {
                    clientsAdapter.setData(response.body());
                }
                view.hideProgress();
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                view.hideProgress();
                view.showErrorMessage();
            }
        });
    }

    public void getContacts() {
        view.showProgress();
        view.setContactsAdapter();
        apiClient.getService().getContacts().enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, final Response<List<Contact>> response) {
                if (response.isSuccessful()) {
                    // save them to the database
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            for (Contact contact : response.body()) {
                                realm.copyToRealmOrUpdate(contact);
                            }
                        }
                    });
                    view.updateRealmAdapter();
                }
                view.hideProgress();
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                view.hideProgress();
                view.showErrorMessage();
            }
        });
    }
}
