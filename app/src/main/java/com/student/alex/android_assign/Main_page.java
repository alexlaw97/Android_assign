package com.student.alex.android_assign;

import android.app.ActionBar;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Main_page extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RelativeLayout mRelativeLayout;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FloatingActionButton mFloatingActionButton1, mFloatingActionButton2, mFloatingActionButton3;
    private Main_page.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Note"));
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mRelativeLayout = findViewById(R.id.layout_main);



        mFloatingActionButton1 = findViewById(R.id.float1);
        mFloatingActionButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_logout));
        mFloatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mRelativeLayout, "Sign Out Successful", Snackbar.LENGTH_LONG)
                        .show();
                mAuth.signOut();
            }
        });

        mFloatingActionButton2 = findViewById(R.id.float2);
        mFloatingActionButton2.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_black_24dp));
        mFloatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main_page.this, Insert_page.class));
            }
        });

        mFloatingActionButton3 = findViewById(R.id.float3);
        mFloatingActionButton3.setImageDrawable(getResources().getDrawable(R.drawable.ic_note_add_black_24dp));
        mFloatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main_page.this, text_note.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Main_page.this, login_page.class));
                }
            }
        };
    }
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onBackPressed()
    {
        finish();
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
          switch(position){
              case 0:
                  Tab1Note tab1 = new Tab1Note();
                  return tab1;

              case 1:
                  Tab2List tab2 = new Tab2List();
                  return tab2;

              default:
                  return null;

          }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position) {
                case 0:
                    return "Note";
                case 1:
                    return "List";
            }
            return null;
        }
    }
}
