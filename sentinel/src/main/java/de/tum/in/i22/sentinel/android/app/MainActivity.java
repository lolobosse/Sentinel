package de.tum.in.i22.sentinel.android.app;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import de.tum.in.i22.sentinel.android.app.fragment.InstrumentFragment;
import de.tum.in.i22.sentinel.android.app.fragment.PolicyEditorFragment;
import de.tum.in.i22.sentinel.android.app.fragment.SettingsFragment;
import de.tum.in.i22.sentinel.android.app.fragment.StatusFragment;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.ParseEventInformationTask;
import de.tum.in.www22.enforcementlanguage.PolicyType;


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

        initParser();
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
            ft.replace(R.id.mainViewContainer, new PolicyEditorFragment());
            setTitle("Editor");
            ft.commit();

        } else if (id == R.id.nav_status) { // Status fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainViewContainer, new StatusFragment());
            setTitle("Status");
            ft.commit();

        } else if (id == R.id.nav_instrument) { // Instrument fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainViewContainer, new InstrumentFragment());
            setTitle("Instrument");
            ft.commit();

        } else if (id == R.id.nav_settings) { // Settings fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainViewContainer, new SettingsFragment());
            setTitle("Settings");
            ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initParser() {
//        ParseEventInformationTask peit = new ParseEventInformationTask(this, R.raw.event_information);
//        peit.execute();
        try {

            // unmarshal customer information from file
            IBindingFactory bfact = BindingDirectory.getFactory(PolicyType.class);
            IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
            InputStream in = getResources().openRawResource(R.raw.policy_appsms_duration4);
            PolicyType order = (PolicyType) uctx.unmarshalDocument(in, null);
            for (Iterator<PolicyType.Choice> iter = order.getChoices().iterator(); iter.hasNext(); ) {
                PolicyType.Choice item = iter.next();
                Log.d("MainActivity", item.getPreventiveMechanism().getName());
            }
        }catch(Exception e){

        }
    }
}
