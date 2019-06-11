package com.example.android.avantealertsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.avantealertsystem.Fragments.AlertFragment;
import com.example.android.avantealertsystem.Fragments.AlertItem;
import com.example.android.avantealertsystem.Fragments.HomeContentFragment;

public class  MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private ViewPager viewPager;




    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        UserDetails.userName="avanteadmin";
        UserDetails.cid = 92099;
        UserDetails.password = "av701Q!pl,";



        Log.e("Token" , UserDetails.token+"");




        Toolbar toolbar = findViewById(R.id.toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_alert);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("ALERT");
                        builder.setMessage("Sending in: ");
                        builder.setCancelable(true);
                        builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Snackbar.make(view, "Alert Sent!", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                            BackgroundWorker dismissAlerts = new BackgroundWorker(getApplicationContext());
                            dismissAlerts.execute("dismissAlerts");
                            //mNoAlerts.setVisibility(View.GONE);
                            //alertFragment.addView(view1);

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        final AlertDialog alertDialog = builder.create();
                new CountDownTimer(6000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        alertDialog.setMessage("Sending in: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        alertDialog.setMessage("Alert Sent!");

                        if(alertDialog.isShowing()){
                            Snackbar.make(view, "Alert Sent!", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            alertDialog.cancel();

                        }
                    }
                }.start();



                        alertDialog.show();



            }
        });

        toolbar.setTitle("AVANTE Alerts");
        toolbar.setTitleTextColor(getResources().getColor(R.color.ic_white_background));
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navigation_home);
        HomeContentFragment adapter = new HomeContentFragment(getSupportFragmentManager());

        adapter.addFragment(new AlertFragment(),"AlertFragment");
        viewPager.setAdapter(adapter);


    }

    public void setViewPager(int fragment){
        viewPager.setCurrentItem(fragment);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BackgroundWorker alertWorker = new BackgroundWorker(this);
        alertWorker.execute("getAlerts");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
