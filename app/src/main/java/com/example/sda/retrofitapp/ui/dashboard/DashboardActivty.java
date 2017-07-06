package com.example.sda.retrofitapp.ui.dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.sda.retrofitapp.R;
import com.example.sda.retrofitapp.model.events.HideProgress;
import com.example.sda.retrofitapp.model.events.ShowProgress;
import com.example.sda.retrofitapp.ui.clients.ClientsFragment;
import com.example.sda.retrofitapp.ui.contacts.ContactsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sda on 01.07.17.
 */

public class DashboardActivty extends AppCompatActivity {

    @BindView(R.id.dashboard_drawer)
    DrawerLayout drawer;
    @BindView(R.id.dashboard_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_container)
    FrameLayout container;
    @BindView(R.id.dashboard_progress)
    ProgressBar progress;
    @BindView(R.id.dashboard_nav)
    NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setupDrawerAndToolbar();
        if (savedInstanceState == null) {
            showFragment(new ContactsFragment());
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onShowProgress(ShowProgress showProgress) {
        container.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onHideProgress(HideProgress hideProgress) {
        progress.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.START))
            drawer.closeDrawer(Gravity.START);
        else if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStack();
        else if (getSupportFragmentManager().getBackStackEntryCount() == 1)
            finish();
        else super.onBackPressed();
    }

    private void setupDrawerAndToolbar() {
        // Tell activity that it has a toolbar
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // Handle clicks on drawer
        drawer.addDrawerListener(toggle);
        // Sync state to have a hamburger menu icon
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_contacts: {
                        showFragment(new ContactsFragment());
                        return true;
                    }
                    case R.id.menu_clients: {
                        showFragment(new ClientsFragment());
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void showFragment(Fragment fragment) {
        String tag = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();

        if (manager.findFragmentByTag(tag) == null) {
            // This fragment does not lie on back stack, need to be added
            manager.beginTransaction()
                    // Add a tag to prevent duplicating insertions of the same fragment
                    .replace(R.id.fragment_container, fragment, tag)
                    .addToBackStack(tag)
                    .commit();
        } else {
            // Get the fragment from the back stack
            manager.popBackStackImmediate(tag, 0);
        }
        drawer.closeDrawer(Gravity.START);
    }
}
