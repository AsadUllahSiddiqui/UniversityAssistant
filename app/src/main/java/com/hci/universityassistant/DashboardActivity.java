package com.hci.universityassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.hci.universityassistant.ui.flashcard.FlashcardFragment;

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

public class DashboardActivity extends AppCompatActivity
{

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if ("ASSESSMENT_ADDED".equals(getIntent().getStringExtra("status")))
        {
            Toast.makeText(this, "Assessment added successfully!", Toast.LENGTH_SHORT).show();
        }
        else if ("FLASHCARD_SAVED".equals(getIntent().getStringExtra("status")))
        {
            Toast.makeText(this, "Flashcard saved successfully!", Toast.LENGTH_SHORT).show();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if ("EDIT_ASSESSMENT".equals(getIntent().getStringExtra("goto")))
        {
            Fragment fragment = new AddAssessmentFragment();

            Bundle args = new Bundle();

            args.putString("key", getIntent().getStringExtra("key"));
            args.putString("title", getIntent().getStringExtra("title"));
            args.putString("subject", getIntent().getStringExtra("subject"));
            args.putString("description", getIntent().getStringExtra("description"));
            args.putString("dueDate", getIntent().getStringExtra("dueDate"));

            fragment.setArguments(args);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_holder, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if ("EDIT_FLASHCARD".equals(getIntent().getStringExtra("goto")))
        {
            Fragment fragment = new AddFlashcardFragment();

            Bundle args = new Bundle();

            args.putString("key", getIntent().getStringExtra("key"));
            args.putString("subject", getIntent().getStringExtra("subject"));
            args.putString("front", getIntent().getStringExtra("front"));
            args.putString("back", getIntent().getStringExtra("back"));
            args.putString("notes", getIntent().getStringExtra("notes"));

            fragment.setArguments(args);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_holder, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if ("FLASHCARD_FRAGMENT".equals(getIntent().getStringExtra("goto")))
        {
            Fragment fragment = new FlashcardFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_holder, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void showAbout(MenuItem item)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("About");

        alertDialog.setMessage(Html.fromHtml("<br><b>" + "Developed by: " + "</b>" + "Muhammad Asadullah, Muhammad Mehlab, and Sajid Ali"));
        alertDialog.setIcon(R.drawable.teamwork);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
//                            Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }
}