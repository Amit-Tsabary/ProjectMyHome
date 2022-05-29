package com.example.amitproject11;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.NavController;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.amitproject11.databinding.ActivityMainBinding;
import com.example.amitproject11.ui.home.HomeFragment;
import com.example.amitproject11.ui.accounts.AccountsFragment;
import com.example.amitproject11.ui.rooms.RoomsFragment;
import com.google.android.material.navigation.NavigationView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity{

    public DrawerLayout drawerLayout;
    private String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public TextView textViewToChange;
    public static String welcomeName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        welcomeName = "";
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String[] extra = extras.getString("key").split("_");
            welcomeName = extra[0];
            //The key argument here must match that used in the other activity
        }

        textViewToChange = (TextView) findViewById(R.id.welcome) ;
        textViewToChange.setText("Welcome " + welcomeName + "!");


        ActionBar actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#4826e0"));
        actionBar.setBackgroundDrawable(colorDrawable);





        drawerLayout = binding.myDrawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_rooms, R.id.nav_settings, R.id.nav_accounts)
                .setOpenableLayout(drawerLayout)
                .build();

        //actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        //drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //actionBarDrawerToggle.syncState();

        //drawerLayout.addDrawerListener(actionBarDrawerToggle);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);




        //navigationView.setNavigationItemSelectedListener((this));
        //NavController navController= Navigation.findNavController(this, R.id.my_drawer_layout);
        //NavigationUI.setupWithNavController(navigationView, navController);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.my_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //@Override
    /*public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    //Button[] buttons = createButtons(2);



    /*public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            Toast.makeText(this, item.getItemId(), Toast.LENGTH_LONG).show();
            if (item.getItemId() == R.id.nav_logout)
            {
                Toast.makeText(this, "logout is pressed", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/



}