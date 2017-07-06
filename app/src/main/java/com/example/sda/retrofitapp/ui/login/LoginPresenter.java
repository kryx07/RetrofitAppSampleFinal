package com.example.sda.retrofitapp.ui.login;

import android.util.Log;
import com.example.sda.retrofitapp.model.LoginResponse;
import com.example.sda.retrofitapp.network.ApiClient;
import com.example.sda.retrofitapp.utlis.PrefsManager;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sda on 26.06.17.
 */

public class LoginPresenter {

    private LoginMvpView view;
    private ApiClient apiClient;
    private PrefsManager prefsManager;

    @Inject
    public LoginPresenter(ApiClient apiClient, PrefsManager prefsManager) {
        this.apiClient = apiClient;
        this.prefsManager = prefsManager;
    }

    public void attachView(LoginMvpView view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }

    public void login(String email, String password) {
        apiClient.getService().login(email, password)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            prefsManager.saveToken(response.body().getAccessToken());
                            Log.e("Access token", prefsManager.getToken());
                            view.openMainActivity();
                        } else {
                            Log.e("Access token", "Login error");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        // Need to handle the error
                        view.showLoginError();
                    }
                });
    }

}
