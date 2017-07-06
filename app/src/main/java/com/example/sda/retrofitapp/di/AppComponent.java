package com.example.sda.retrofitapp.di;

import com.example.sda.retrofitapp.ui.contacts.ContactsFragment;
import com.example.sda.retrofitapp.ui.login.LoginActivity;
import com.example.sda.retrofitapp.ui.main.MainActivity;

import javax.inject.Singleton;

/**
 * Created by sda on 26.06.17.
 */

@Singleton
@dagger.Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(LoginActivity loginActivity);

    void inject(MainActivity mainActivity);

    void inject(ContactsFragment contactsFragment);
}
