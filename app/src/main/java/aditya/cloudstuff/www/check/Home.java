package aditya.cloudstuff.www.check;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    StatesFragment statesFragment;
    CapitalFragment capitalFragment;
    Cards cards;
    student_profile studentProfile;
    webview webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentProfile = new student_profile();
        studentProfile.onAttach(getApplicationContext());
        setContentView(R.layout.home_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        Log.v("MyApp", "onStart() Home");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.v("MyApp", "onResume() Home");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.v("MyApp", "onPause() Home");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v("MyApp", "onStop() Home");
        super.onStop();
    }

    @Override
    public void finish() {
        Log.v("MyApp", "finish() Home");
        super.finish();
    }

    @Override
    public void onBackPressed() {
        Log.v("MyApp", "onBackPressed() Home");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        Log.v("MyApp", "onDestroy() Home");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_states) {
            statesFragment = new StatesFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment, statesFragment);
            fragmentTransaction.commit();

            Toast.makeText(getApplicationContext(), "States", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_capitals) {
            studentProfile = new student_profile();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment, studentProfile);
            fragmentTransaction.commit();
            Toast.makeText(getApplicationContext(), "Capitals", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_cards) {
            cards = new Cards();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment, cards );
            fragmentTransaction.commit();
            Toast.makeText(getApplicationContext(), "Cards", Toast.LENGTH_SHORT).show();
        }

        else if (id == R.id.nav_webview) {
            webView = new webview();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment,webView );
            fragmentTransaction.commit();
            Toast.makeText(getApplicationContext(), "WebView", Toast.LENGTH_SHORT).show();
        }

        else if (id == R.id.nav_signout) {
            Toast.makeText(getApplicationContext(), "Signing Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, login.class);
            intent.putExtra("LogOut", true );
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
