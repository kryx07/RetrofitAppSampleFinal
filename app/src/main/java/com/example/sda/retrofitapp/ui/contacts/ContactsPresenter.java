package com.example.sda.retrofitapp.ui.contacts;

import android.app.usage.UsageEvents;

import com.example.sda.retrofitapp.model.Contact;
import com.example.sda.retrofitapp.model.events.HideProgress;
import com.example.sda.retrofitapp.model.events.ShowProgress;
import com.example.sda.retrofitapp.network.ApiClient;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sda on 01.07.17.
 */

public class ContactsPresenter {

    private ContactsMvpView view;
    private ApiClient apiClient;
    private Realm realm;

    @Inject
    public ContactsPresenter(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public void attach(ContactsMvpView view) {
        this.view = view;
    }

    public void detach() {
        this.view = null;
        this.realm = null;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public void start() {
        EventBus.getDefault().post(new ShowProgress());
        apiClient.getService().getContacts()
                .enqueue(new Callback<List<Contact>>() {
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
                            view.updateData();
                        }
                        EventBus.getDefault().post(new HideProgress());
                    }

                    @Override
                    public void onFailure(Call<List<Contact>> call, Throwable t) {
                        EventBus.getDefault().post(new HideProgress());
                    }
                });
    }

    public RealmResults<Contact> getAllContacts() {
        return realm.where(Contact.class).findAll();
    }
}
