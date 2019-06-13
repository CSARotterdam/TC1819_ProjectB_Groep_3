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
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class NavDrawer extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle toggle;
    public ArrayList<HashMap<String,String>> winkelmandje = new ArrayList<>();

    private static final String TAG_PID = "ProductID";
    private static final String TAG_NAME = "ProductName";
    private static final String TAG_AMOUNT = "Amount";
    private static final String TAG_STOCK = "ProductStock";
    private static final String TAG_DATE = "StartDate";

    private TextView detailStock;




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
    public void RemoveWinkelmandjeItem(View v){
        View row = (View) v.getParent().getParent();
        for (int i = 0;i<winkelmandje.size();i++){
            if(winkelmandje.get(i).containsValue(((TextView) row.findViewById(R.id.product_name)).getText().toString())){
                winkelmandje.remove(i);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, Winkelmandje.newInstance());
                transaction.commit();
            }
        }
    }
    public void increase_amount(View view){
        View row =(View) view.getParent().getParent();
        int activeStock = 0;

        for (int i = 0; i< winkelmandje.size();i++){
            if (winkelmandje.get(i).containsValue(((TextView) row.findViewById(R.id.product_name)).getText().toString())){
                activeStock = Integer.parseInt(winkelmandje.get(i).get(TAG_STOCK));

            }
        }
        int amount = Integer.parseInt(((TextView)row.findViewById(R.id.product_amount)).getText().toString());
        if (amount < activeStock) {
            amount++;
            for (int i = 0; i< winkelmandje.size();i++){
                if (winkelmandje.get(i).containsValue(((TextView) row.findViewById(R.id.product_name)).getText().toString())){
                    winkelmandje.get(i).put(TAG_AMOUNT, Integer.toString(amount));
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, Winkelmandje.newInstance());
                    transaction.commit();
                }
            }
        }

    }
    public void decrease_amount(View view){
        View row = (View) view.getParent().getParent();
        int amount = Integer.parseInt(((TextView)row.findViewById(R.id.product_amount)).getText().toString());
        amount --;
        if (amount < 1){
            amount=1;
        }
        for (int i = 0; i< winkelmandje.size();i++){
            if (winkelmandje.get(i).containsValue(((TextView) row.findViewById(R.id.product_name)).getText().toString())){
                winkelmandje.get(i).put(TAG_AMOUNT, Integer.toString(amount));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, Winkelmandje.newInstance());
                transaction.commit();
            }
        }
    }

    public void InventoryCartHandler(View v){
        View btnLayout =(View) v.getParent();
        View row = (View) btnLayout.getParent();
        Boolean alreadyPresent = false;
        int amount = 1;
        for(int i = 0; i < winkelmandje.size();i++){
            if(winkelmandje.get(i).containsValue(((TextView) row.findViewById(R.id.pid)).getText().toString())){
                alreadyPresent = true;
            }
        }
        if(!alreadyPresent) {
            HashMap<String, String> map = new HashMap<String, String>();

            map.put(TAG_PID, ((TextView) row.findViewById(R.id.pid)).getText().toString());
            map.put(TAG_NAME, ((TextView) row.findViewById(R.id.product_name)).getText().toString());
            map.put(TAG_STOCK, ((TextView) row.findViewById(R.id.product_stock)).getText().toString());
            Calendar cal = Calendar.getInstance();
            DateFormat form = new SimpleDateFormat("dd-MM-yyyy");
            map.put(TAG_DATE, form.format(cal.getTime()));
            map.put(TAG_AMOUNT, Integer.toString(amount));

            winkelmandje.add(map);

            FragmentManager fragmentManager = this.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, Winkelmandje.newInstance()).addToBackStack(null);
            transaction.commit();
        }else{
            Toast.makeText(this.getApplicationContext(),
                    "Item already present in winkelmandje",
                    Toast.LENGTH_LONG).show();
        }
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
            case R.id.nav_broken:
                fragmentClass = brokenFragment.class;
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
