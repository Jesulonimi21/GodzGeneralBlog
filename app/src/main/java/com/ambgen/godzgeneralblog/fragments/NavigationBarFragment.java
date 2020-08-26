package com.ambgen.godzgeneralblog.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ambgen.godzgeneralblog.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class NavigationBarFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root= inflater.inflate(R.layout.fragment_navigation_bar, container, false);
        BottomNavigationView navView = root.findViewById(R.id.nav_view);
        NavHostFragment navHostFragment =(NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_fragment);

//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navHostFragment.getNavController());
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @NonNull
    @Override
    public String toString() {
        Log.d("Jesulonimi","I am home NavigationBarFragment");
        return super.toString();
    }
}
