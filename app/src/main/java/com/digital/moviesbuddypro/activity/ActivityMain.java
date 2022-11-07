package com.digital.moviesbuddypro.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.digital.moviesbuddypro.R;
import com.digital.moviesbuddypro.fragment.FragmentExplore;
import com.digital.moviesbuddypro.fragment.FragmentHome;
import com.digital.moviesbuddypro.fragment.FragmentWishlist;

public class ActivityMain extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        //openFragment(HomeFragment.newInstance("", ""));

        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.homeIconButton:
                                openFragment(FragmentHome.newInstance("", ""));
                                return true;
                            case R.id.exploreIconButton:
                                openFragment(FragmentExplore.newInstance("", ""));
                                return true;
                            case R.id.wishlistIconButton:
                                openFragment(FragmentWishlist.newInstance("Wishlist", ""));
                                return true;
                        }
                        return false;
                    }
                };

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(FragmentHome.newInstance("", ""));
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Dialog dDialog = new Dialog(ActivityMain.this, R.style.FullScreenDialogStyle);
        dDialog.setContentView(R.layout.dialog_exit);

        dDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.cancel();
            }
        });

        dDialog.findViewById(R.id.cancelDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.cancel();
            }
        });

        dDialog.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.exit(0);
            }
        });

        dDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dDialog.show();
    }
}
