package com.karim.ATPtech;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karim.ATPtech.Fragments.AddBidFragment;
import com.karim.ATPtech.Fragments.BidsFragment;
import com.karim.ATPtech.Fragments.ChangeDataFragment;
import com.karim.ATPtech.Fragments.MainFragment;
import com.karim.ATPtech.Fragments.MyBidsFragment;
import com.karim.ATPtech.Fragments.SendErrorFragment;
import com.karim.ATPtech.Fragments.TechFragment;
import com.karim.ATPtech.Model.Request.RemoveTokenRequest;
import com.karim.ATPtech.Model.Request.UpdateTokenRequest;
import com.karim.ATPtech.Model.Response.RemoveTokenResponse;
import com.karim.ATPtech.Model.Response.UpdateTokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView tvNumber, tvCall1, tvCall2, tvSite, tvMail, tvGeo;
    SharedPreferences preferences;
    String phone;
    private ActionBarDrawerToggle mDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:8 (843) 567-54-50"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);

        drawer.openDrawer(GravityCompat.START);
        try {
            Class fragmentClass = MainFragment.class;
            Fragment fragment = (Fragment)fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        preferences = getSharedPreferences("ATPTech", MODE_PRIVATE);
        if (preferences.contains("login")) {
            phone = preferences.getString("login", "");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    int counter = 0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            counter++;
            if(counter == 7){
                Toast.makeText(getApplicationContext(), "Пасхалочка!", Toast.LENGTH_SHORT).show();
                counter = 0;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Class fragmentClass = null;

        int id = item.getItemId();

        if (id == R.id.tech) {
            fragmentClass = TechFragment.class;
        } else if (id == R.id.bids) {
            fragmentClass = BidsFragment.class;
        } else if (id == R.id.myBids) {
            fragmentClass = MyBidsFragment.class;
        } else if(id == R.id.addBid) {
            fragmentClass = AddBidFragment.class;
        } else if (id == R.id.changeData) {
            fragmentClass = ChangeDataFragment.class;
        } else if (id == R.id.sendError) {
            fragmentClass = SendErrorFragment.class;
        } else if (id == R.id.logout) {
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            removeToken(phone);
            SharedPreferences preferences = getSharedPreferences("ATPTech", MODE_PRIVATE);
            preferences.edit().clear().apply();
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Вставляем фрагмент, заменяя текущий фрагмент
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        // Выделяем выбранный пункт меню в шторке
        item.setChecked(true);
        // Выводим выбранный пункт в заголовке
        setTitle(item.getTitle());

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // Hide keyboard when navigation drawer is open
        drawer.addDrawerListener(new DrawerLayout.DrawerListener(){
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {}
            @Override public void onDrawerOpened(View drawerView) {}
            @Override public void onDrawerClosed(View drawerView) {}
            @Override public void onDrawerStateChanged(int newState) {
                //DeviceUtils.hideVirtualKeyboard(LaunchActivity.this, drawerLayout);
                final InputMethodManager imm = (InputMethodManager)MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawer.getWindowToken(), 0);
            }
        });
        return true;
    }

    public void removeToken(final String login){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        RemoveTokenRequest lr = new RemoveTokenRequest(login);

        Call<RemoveTokenResponse> call = null;
        call = service.removetoken(lr);
        call.enqueue(new Callback<RemoveTokenResponse>() {
            @Override
            public void onResponse(Call<RemoveTokenResponse> call, Response<RemoveTokenResponse> response) {}

            @Override
            public void onFailure(Call<RemoveTokenResponse> call, Throwable t) { }
        });
    }
}


