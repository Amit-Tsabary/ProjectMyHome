package com.example.amitproject11;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.NavController;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.amitproject11.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity{

    public DrawerLayout drawerLayout;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public TextView textViewToChange;
    public static String welcomeName;

    // a standard method that runs whenever the activity is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        welcomeName = "";
        Bundle extras = getIntent().getExtras();
        String[] extra = extras.getString("key").split("_");
        //name_email_id_da
        if (extras != null) {
            welcomeName = extra[0];
            //The key argument here must match that used in the other activity
        }

        textViewToChange = findViewById(R.id.welcome) ;
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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        if(!extra[2].equals("1"))
        {
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.nav_accounts);
            item.setEnabled(false);
        }
    }

    //runs when back arrow in pressed
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.my_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    protected void onRestart() {
        super.onRestart();
    }


}