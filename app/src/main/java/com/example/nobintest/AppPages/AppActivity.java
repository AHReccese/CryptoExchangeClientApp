package com.example.nobintest.AppPages;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.nobintest.AppPages.About.AboutActivity;
import com.example.nobintest.AppPages.Language.LanguageActivity;
import com.example.nobintest.AppPages.SignInUp.LoginActivity;
import com.example.nobintest.AppPages.UserProfile.ProfileActivity;
import com.example.nobintest.AppPages.Security.SecurityActivity;
import com.example.nobintest.AppPages.adapters.HomeFragmentAdapter;
import com.example.nobintest.AppPages.adapters.NewsFragmentAdapter;
import com.example.nobintest.AppPages.adapters.ToolsFragmentAdapter;
import com.example.nobintest.AppPages.adapters.TradeFragmentAdapter;
import com.example.nobintest.AppPages.adapters.WalletFragmentAdapter;
import com.example.nobintest.DataManegment.AppDataManager;
import com.example.nobintest.R;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Locale;

import nl.joery.animatedbottombar.AnimatedBottomBar;


public class AppActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setPage();
    }

    private void setPage() {
        setContentView(R.layout.activity_app);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

        userName = navigationView.getHeaderView(0).findViewById(R.id.user_name);

        AppDataManager appDataManager = AppDataManager.getInstance(getApplication());

        if (!appDataManager.getProfileState().equals(AppDataManager.USER_STATE_GUEST)) {
            navigationView.getMenu().findItem(R.id.nav_sign_out).setIcon(R.drawable.ic_sign_out);
            navigationView.getMenu().findItem(R.id.nav_sign_out).setTitle(R.string.sign_out);
            userName.setText(appDataManager.getUserName());
        } else {
            userName.setText(R.string.guest);
        }


        viewPager = findViewById(R.id.vp_pages);

        AnimatedBottomBar bottomNavBar = findViewById(R.id.bottom_nav_bar);
        bottomNavBar.setOnTabSelectListener(bottomNavigationListener);

        PagerAdapter pagerAdapter = new HomeFragmentAdapter(getSupportFragmentManager(), AppActivity.this);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tbl_pages);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setLanguage() {
        AppDataManager appDataManager = AppDataManager.getInstance(AppActivity.this);
        //Locale locale = new Locale(appDataManager.getLanguage());
        Locale locale = new Locale(appDataManager.getLanguage());
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        //configuration.setLayoutDirection(locale);

        //getApplicationContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        //recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            shareScreenShot();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareScreenShot() {

        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap mBitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        File file = new File(getExternalCacheDir() + "/sharable_image.jpg");

        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String time = String.format("%1$tY-%<tm-%<td %<tR", Calendar.getInstance());
        String shareText = getString(R.string.app_share_text_1, time, getString(R.string.app_download_url));

        Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.example.nobintest.fileProvider", file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share Image"));

    }

    private void shareApp() {
        String shareText = getString(R.string.app_share_text_2, getString(R.string.app_download_url));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.setType("text/plain");

        startActivity(Intent.createChooser(shareIntent, "Share Text"));
    }

    private void rateApp() {
        Toast.makeText(this, R.string.app_wil_be_published, Toast.LENGTH_LONG).show();
    }

    private AnimatedBottomBar.OnTabSelectListener bottomNavigationListener =
        new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NotNull AnimatedBottomBar.Tab tab1) {
                PagerAdapter pagerAdapter = null;
                switch (i1) {
                    case 0:
                        pagerAdapter = new NewsFragmentAdapter(getSupportFragmentManager(), AppActivity.this);
                        viewPager.setAdapter(pagerAdapter);
                        break;
                    case 1:
                        pagerAdapter = new WalletFragmentAdapter(getSupportFragmentManager(), AppActivity.this);
                        viewPager.setAdapter(pagerAdapter);
                        break;
                    case 2:
                        pagerAdapter = new HomeFragmentAdapter(getSupportFragmentManager(), AppActivity.this);
                        viewPager.setAdapter(pagerAdapter);
                        break;
                    case 3:
                        pagerAdapter = new TradeFragmentAdapter(getSupportFragmentManager(), AppActivity.this);
                        viewPager.setAdapter(pagerAdapter);
                        break;
                    case 4:
                        pagerAdapter = new ToolsFragmentAdapter(getSupportFragmentManager(), AppActivity.this);
                        viewPager.setAdapter(pagerAdapter);
                        break;
                }
            }

            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {
                // do nothing
            }
        };


    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
        new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_security:
                        intent = new Intent(getApplication(), SecurityActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        intent = new Intent(getApplication(), ProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_share:
                        shareApp();
                        break;
                    case R.id.nav_rate:
                        rateApp();
                        break;
                    case R.id.nav_about:
                        intent = new Intent(getApplication(), AboutActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_sign_out:
                        if (item.getTitle().toString().equals(getString(R.string.sign_in))) {
                            signIn(item);
                        } else {
                            signOut(item);
                        }
                        break;
                    case R.id.nav_language:
                        finish();
                        intent = new Intent(getApplication(), LanguageActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        // nothing
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        };

    private void signIn(MenuItem item) {
        finish();
        startActivity(new Intent(AppActivity.this, LoginActivity.class));
    }

    private void signOut(MenuItem item) {
        AppDataManager appDataManager = AppDataManager.getInstance(getApplication());
        appDataManager.setProfileState(AppDataManager.USER_STATE_GUEST);
        userName.setText(R.string.guest);

        item.setIcon(R.drawable.ic_sign_in);
        item.setTitle(R.string.sign_in);

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.back_again_to_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

}