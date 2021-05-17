package com.example.CovSewa;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.CovSewa.ui.AboutUs.AboutUsFragment;
import com.example.CovSewa.ui.SupportUs.SupportUsFragment;
import com.example.CovSewa.ui.Terms.TermsFragment;
import com.example.CovSewa.ui.home.HomeFragment;
import com.example.CovSewa.ui.myDonation.MyDonationFragment;
import com.example.CovSewa.ui.myProfile.MyProfileFragment;
import com.example.CovSewa.ui.myRequest.MyRequestFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class DashBoard extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;

    Fragment currentFragment = new HomeFragment(),fragment;
    String title,UID,phone;
    Toolbar toolbar;
    ConstraintLayout appBarLayout;
    private ImageView imageView;



    TextView appbarEmail, appbarName;
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Log.d("UID", "onCreate: null");

            startActivity(new Intent(this, OnBoarding.class));
            finish();
        } else {
            UID = mAuth.getCurrentUser().getUid();
            phone = mAuth.getCurrentUser().getEmail();
            Log.d("UID", "onCreate: "+UID);
        }

        setTheme(R.style.themeSplashScreen);
        setContentView(R.layout.dash_board);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackground(new ColorDrawable(getResources().getColor(R.color.primaryColor)));
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");
        toolbar.setNavigationOnClickListener(view -> {
            setFragment(new HomeFragment());
            toolbar.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.VISIBLE);
        });

        setFragment(new HomeFragment());


        appbarEmail = findViewById(R.id.appbarEmail);
        appbarName = findViewById(R.id.appbarName);

        appbarName.setTextColor(getResources().getColor(R.color.white));
        appbarEmail.setTextColor(getResources().getColor(R.color.white));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomAppBar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_my_profile, R.id.nav_request,R.id.nav_donation,R.id.nav_about,R.id.nav_support,R.id.nav_terms)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, bottomAppBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.navHeaderUserName);
        TextView userEmail = headerView.findViewById(R.id.navHeaderUserEmail);

        userEmail.setText(phone);
        userName.setText(UID);





        appBarLayout = findViewById(R.id.appBarLayout);

        appBarLayout.setBackground(new ColorDrawable(getResources().getColor(R.color.app_barColor)));
        fragment = new HomeFragment() ;

        navigationView.setNavigationItemSelectedListener(item -> {
            title=item.getTitle().toString();
           switch (item.getItemId()){
               case  R.id.nav_home:
                   fragment=new TermsFragment();
                   toolbar.setVisibility(View.VISIBLE);
                   appBarLayout.setVisibility(View.GONE);
                   break;
               case R.id.nav_about:
                   fragment=new AboutUsFragment();
                   toolbar.setVisibility(View.VISIBLE);
                   appBarLayout.setVisibility(View.GONE);
                   break;
               case R.id.nav_donation:
                   fragment = new MyDonationFragment();
                   toolbar.setVisibility(View.VISIBLE);
                   appBarLayout.setVisibility(View.GONE);
                   Log.d("TAG", "onNavigationItemSelected: HOME");
                   break;

               case R.id.nav_request:
                       fragment = new MyRequestFragment();
                       toolbar.setVisibility(View.VISIBLE);
                       appBarLayout.setVisibility(View.GONE);
                       break;
               case R.id.nav_my_profile:
                   fragment = new MyProfileFragment();
                   toolbar.setVisibility(View.VISIBLE);
                   appBarLayout.setVisibility(View.GONE);
                   break;
               case R.id.nav_support:
                   fragment = new SupportUsFragment();
                   toolbar.setVisibility(View.VISIBLE);
                   appBarLayout.setVisibility(View.GONE);
                   break;

               case R.id.logout:
                   mAuth.signOut();
                   Intent intent = new Intent(this,SignInActivity.class);
                   finish();
                   startActivity(intent);
                   break;

               default:
                  fragment = new HomeFragment();
           }
            setFragment(fragment);
            Log.d("item id", "onNavigationItemSelected: "+ item.getTitle());
            DrawerLayout drawer1 =  findViewById(R.id.drawer_layout);
            drawer1.closeDrawer(GravityCompat.START);
            return false;
        });
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                                                    @Override
                                                    public boolean onMenuItemClick(MenuItem item) {
                                                        Log.d("TAG", "onMenuItemClick:  notification");
                                                        return false;
                                                    }
                                                });

                FloatingActionButton homeButton = findViewById(R.id.fab);

        homeButton.setOnClickListener(v->{
            setFragment(new HomeFragment());
            toolbar.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.VISIBLE);
            currentFragment = new HomeFragment();
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setFragment( Fragment fragment){
        if(fragment!=currentFragment){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            Log.d("TAG", "setFragment: "+ currentFragment);
            fragmentTransaction.
                    replace(R.id.nav_host_fragment,fragment).commit();
            toolbar.setTitle(title);
            currentFragment= fragment;
        }

    }
}