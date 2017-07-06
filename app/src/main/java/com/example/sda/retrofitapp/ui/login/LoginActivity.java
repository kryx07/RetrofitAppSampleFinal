package com.example.sda.retrofitapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sda.retrofitapp.App;
import com.example.sda.retrofitapp.R;
import com.example.sda.retrofitapp.model.LoginResponse;
import com.example.sda.retrofitapp.network.ApiClient;
import com.example.sda.retrofitapp.ui.main.MainActivity;
import com.example.sda.retrofitapp.utlis.PrefsManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements LoginMvpView {

    // Bind views - provide id, type and your var name
    @BindView(R.id.email)
    EditText editMail;
    @BindView(R.id.password)
    EditText editPassword;

    @Inject
    LoginPresenter loginPresenter;

    // Set OnClickListener - provide id and your public method
    @OnClick(R.id.submit_button)
    public void onSubmitClick() {
        loginPresenter.login(editMail.getText().toString(), editPassword.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ((App) getApplication()).getAppComponent().inject(this);
        loginPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        loginPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoginError() {
        Toast.makeText(this, getString(R.string.toast_login_error), Toast.LENGTH_SHORT).show();
    }
}
