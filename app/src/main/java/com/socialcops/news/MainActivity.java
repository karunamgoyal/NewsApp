package com.socialcops.news;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView mTextMessage;
    private EditText searchView;
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    MenuItem prevMenuItem;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportActionBar().setTitle("Headlines");
                    searchView = findViewById(R.id.searchText);
                    searchView.setVisibility(EditText.GONE);
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    searchView = findViewById(R.id.searchText);
                    searchView.setVisibility(EditText.VISIBLE);
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    getSupportActionBar().setTitle("Saved");
                    searchView = findViewById(R.id.searchText);
                    searchView.setVisibility(EditText.GONE);
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbarsearch);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Headlines");
        searchView = findViewById(R.id.searchText);
        searchView.setVisibility(EditText.GONE);
        mTextMessage = (TextView) findViewById(R.id.message);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = findViewById(R.id.viewpager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        //Add fragment
        viewPageAdapter.AddFragment(new NewsFragment(), "");
        viewPageAdapter.AddFragment(new SearchFragment(), "");
        viewPageAdapter.AddFragment(new SavedFragment(), "");
        viewPager.setAdapter(viewPageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    navigation.getMenu().getItem(0).setChecked(false);
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);
                switch (position) {
                    case 0:
                        getSupportActionBar().setTitle("Headlines");
                        searchView = findViewById(R.id.searchText);
                        searchView.setVisibility(EditText.GONE);
                        break;
                    case 1:
                        searchView = findViewById(R.id.searchText);
                        searchView.setVisibility(EditText.VISIBLE);
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Saved");
                        searchView = findViewById(R.id.searchText);
                        searchView.setVisibility(EditText.GONE);

                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //Schdule Notification everyday at 9;
        scheduleNotification();
        if(getUserCountry()!=null)
            Variables.COUNTRY=getUserCountry();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_aboutus) {
            Intent intent = new Intent(this, AboutUS.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void scheduleNotification() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        Intent intent = new Intent(this, MyNotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }
    public String getUserCountry() {
        try {
            final TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }

}
