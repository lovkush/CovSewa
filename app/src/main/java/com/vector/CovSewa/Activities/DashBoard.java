package com.vector.CovSewa.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.vector.CovSewa.OnBoarding;
import com.vector.CovSewa.R;
import com.vector.CovSewa.SignInActivity;
import com.vector.CovSewa.UserData;
import com.vector.CovSewa.UserDataViewModel;
import com.vector.CovSewa.ui.AboutUs.AboutUsFragment;
import com.vector.CovSewa.ui.PrivacyPolicy;
import com.vector.CovSewa.ui.SupportUs.SupportUsFragment;
import com.vector.CovSewa.ui.Terms.TermsFragment;
import com.vector.CovSewa.ui.home.HomeFragment;
import com.vector.CovSewa.ui.myDonation.MyDonationFragment;
import com.vector.CovSewa.ui.myProfile.MyProfileFragment;
import com.vector.CovSewa.ui.myRequest.MyRequestFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.content.ContentValues.TAG;

public class DashBoard extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;

    Fragment currentFragment = new HomeFragment(),fragment;
    String title,UID,email;
    Toolbar toolbar;
    ConstraintLayout appBarLayout;
    private ImageView appBarImageView,navBarImageView;
    private UserData value;
    TextView userName,userEmail;

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
            email = mAuth.getCurrentUser().getEmail();
            Log.d("UID", "onCreate: "+UID);
        }
        prepareData();

        //setTheme(R.style.Theme_CovSeva);
        setContentView(R.layout.dash_board);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackground(new ColorDrawable(getResources().getColor(R.color.primaryColor)));
        toolbar.setNavigationOnClickListener(view -> {
            setFragment(new HomeFragment());
            toolbar.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.VISIBLE);
        });

        View v1 = findViewById(R.id.appBarLayout);
        appBarImageView = v1.findViewById(R.id.appbarImage);

        setFragment(new HomeFragment());


        appbarEmail = findViewById(R.id.appbarEmail);
        appbarName = findViewById(R.id.appbarName);

        appbarName.setTextColor(getResources().getColor(R.color.white));
        appbarEmail.setTextColor(getResources().getColor(R.color.white));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomAppBar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, bottomAppBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
         userName = headerView.findViewById(R.id.navHeaderUserName);
         userEmail = headerView.findViewById(R.id.navHeaderUserEmail);
         navBarImageView = headerView.findViewById(R.id.navbarImage);






        appBarLayout = findViewById(R.id.appBarLayout);

        appBarLayout.setBackground(new ColorDrawable(getResources().getColor(R.color.app_barColor)));
        fragment = new HomeFragment() ;

        navigationView.setNavigationItemSelectedListener(item -> {
            title=item.getTitle().toString();
           switch (item.getItemId()){
               case  R.id.nav_home:
                   fragment=new HomeFragment();
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

               case R.id.nav_terms:
                   fragment = new TermsFragment();
                   toolbar.setVisibility(View.VISIBLE);
                   appBarLayout.setVisibility(View.GONE);
                   break;

               case R.id.nav_privacy_policy:
                   fragment = new PrivacyPolicy();
                   toolbar.setVisibility(View.VISIBLE);
                   appBarLayout.setVisibility(View.GONE);
                   break;

               case R.id.logout:
                   mAuth.signOut();
                   Intent intent = new Intent(this, SignInActivity.class);
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
            Log.d("TAG", "setFragment: "+ currentFragment);
            fragmentTransaction.remove(currentFragment);
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.
                    replace(R.id.nav_host_fragment,fragment).commit();
            toolbar.setTitle(title);
            currentFragment= fragment;
        }

    }

    private void prepareData(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User").child(UID);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if(value!=null){
                    value = dataSnapshot.getValue(UserData.class);
                    userEmail.setText(email);
                    userName.setText(value.getName());
                    setImage("ProfileImage/"+value.getContact());
                    UserDataViewModel viewModel = new ViewModelProvider(DashBoard.this).get(UserDataViewModel.class);
                    viewModel.selectItem(value);
                    Log.d(TAG, "Value is: " + value);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    private void setImage(String userPhoto) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        if(userPhoto==null)
            return;
        StorageReference photoReference= storageReference.child(userPhoto);
        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                appBarImageView.setImageBitmap(bmp);
                navBarImageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(DashBoard.this, "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }

}