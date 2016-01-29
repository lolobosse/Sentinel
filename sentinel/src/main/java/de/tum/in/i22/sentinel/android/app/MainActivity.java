package de.tum.in.i22.sentinel.android.app;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import de.tum.in.i22.sentinel.android.app.fragment.InstrumentFragment;
import de.tum.in.i22.sentinel.android.app.fragment.PlayStoreFragment;
import de.tum.in.i22.sentinel.android.app.fragment.PolicyEditor;
import de.tum.in.i22.sentinel.android.app.fragment.PostInstrumentFragment;
import de.tum.in.i22.sentinel.android.app.fragment.SettingsFragment;
import de.tum.in.i22.sentinel.android.app.fragment.StatusFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set the content main as the content at the creation of the activity
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainViewContainer, new StatusFragment());
        ft.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Utils.passPoliciesFromRawToFile(this);
        Utils.initDefaultFiles(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_editor) { // Editor fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainViewContainer, new PolicyEditor());
            clearFragmentStack();
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_status) { // Status fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainViewContainer, new StatusFragment());
            clearFragmentStack();
            ft.commit();

        } else if (id == R.id.nav_instrument) { // Instrument fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainViewContainer, new InstrumentFragment());
            clearFragmentStack();
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_settings) { // Settings fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainViewContainer, new SettingsFragment());
            clearFragmentStack();
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_playstore) { // PlayStore fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainViewContainer, new PlayStoreFragment());
            clearFragmentStack();
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_PostInstrument) { // PostInstrument fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainViewContainer, new PostInstrumentFragment());
            clearFragmentStack();
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clearFragmentStack(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

}
