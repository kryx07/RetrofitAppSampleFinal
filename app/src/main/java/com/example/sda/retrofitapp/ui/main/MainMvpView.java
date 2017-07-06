package com.example.sda.retrofitapp.ui.main;

import android.support.annotation.Nullable;

import com.example.sda.retrofitapp.model.Client;

/**
 * Created by sda on 26.06.17.
 */

public interface MainMvpView {

    void showProgress();

    void hideProgress();

    void showErrorMessage();

    void openDetailsView(@Nullable Client client);

    void setCallsAdapter();

    void setClientsAdapter();

    void setContactsAdapter();

    void updateRealmAdapter();
}
