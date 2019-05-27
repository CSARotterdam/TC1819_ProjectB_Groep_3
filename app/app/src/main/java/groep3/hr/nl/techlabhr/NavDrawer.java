package groep3.hr.nl.techlabhr;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;

public class NavDrawer extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle toggle;
    public ArrayList<HashMap<String,String>> winkelmandje = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        configureDrawer(nvDrawer);

        nvDrawer.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

        getSupportActionBar().setTitle("Categories");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, Categories.newInstance());
        transaction.commit();

    }

    public void configureDrawer(NavigationView nv){

            if (getIntent().getStringExtra("permission").equals("beheerder")) {
                nv.getMenu().setGroupVisible(R.id.nav_beheerder, true);
            }

            if (getIntent().getStringExtra("permission").equals("admin")) {
                nv.getMenu().setGroupVisible(R.id.nav_beheerder,true);
                nv.getMenu().setGroupVisible(R.id.nav_admin,true);
            }

    }
    public void selectDrawerItem(MenuItem menuItem){
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_inventaris:
                fragmentClass = Categories.class;
                break;
            case R.id.nav_winkelmandje:
                fragmentClass = Winkelmandje.class;
                break;
            case R.id.nav_leningen:
                fragmentClass = Mijn_leningen.class;
                break;
            case R.id.nav_info:
                fragmentClass = Informatie.class; 
                break;
            case R.id.nav_add_beheerder:
                fragmentClass = Permissions.class;
                break;
            case R.id.nav_change_stock:
                fragmentClass = Inventaris_aanpassen.class;
                break;
            case R.id.nav_uitgeleend:
                fragmentClass = Uitgeleend.class;
                break;
            default:
                fragmentClass = Placeholder.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
        transaction.commit();


        // Close the navigation drawer
        mDrawer.closeDrawers();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                return true;

            case R.id.action_winkelmandje:
                Fragment fragment = null;
                try {
                    fragment = (Fragment) Winkelmandje.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                nvDrawer.getMenu().findItem(R.id.nav_winkelmandje).setChecked(true);
                setTitle(nvDrawer.getMenu().findItem(R.id.nav_winkelmandje).getTitle());

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        toggle.onConfigurationChanged(newConfig);
    }
}
