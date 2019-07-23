package com.example.abdeali.irrigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class weather_data extends AppCompatActivity implements LocationListener {

    private DrawerLayout d1;
    public static String lat = "1.0";
    public static String lon = "1.0";
    public Intent it;
    private ActionBarDrawerToggle abt;
    private NavigationView navview;
    String Nme = "empty";
    String Temp = "";
    String Humid = "";
    LocationManager lm;
    public TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //tv2 = (TextView)findViewById(R.id.tv4) ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_data);
        tv = (TextView)findViewById(R.id.tv2);

        //check
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


        //UI Initialization
        d1 = (DrawerLayout) findViewById(R.id.drrr);
        abt = new ActionBarDrawerToggle(weather_data.this, d1, R.string.Open, R.string.Close);
        abt.setDrawerIndicatorEnabled(true);
        d1.addDrawerListener(abt);
        abt.syncState();
        navview = (NavigationView) findViewById(R.id.navview);
        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.i1) {
                    it = new Intent(weather_data.this, MainActivity.class);
                    startActivity(it);
                } else if (id == R.id.i2) {
                    it = new Intent(weather_data.this, Recog.class);
                    startActivity(it);
                } else {
                    it = new Intent(weather_data.this, weather_data.class);
                    startActivity(it);
                }

                return true;
            }
        });

        getLocation();

    }

    public void getLocation() {
        try {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);
        } catch (Exception e) {

        }
    }


    @Override
    public void onLocationChanged(Location location) {
        //Log.i("coordinates", "Latitude: " + location.getLatitude() + "Longitude: " + location.getLongitude());
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        Toast.makeText(this, "coordinates,Latitude: " + location.getLatitude() + "Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        while (lat =="1.0");
        down task = new down();
        task.execute("http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=42214426e229d6dc060d743972ca1931");
        tv.setText(""+Nme+"");

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    public class down extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            URL ur;
            HttpURLConnection httpURLConnection = null;
            String res = "";
            try{
                ur = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection)ur.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1){
                    char ch = (char)data;
                    res = res + ch;
                    data = reader.read();
                }
                return res;
            }catch(Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            String weath = "nothing";
            String nm = "not";
            try{
                String Name = "";
                JSONObject job = new JSONObject(res);
                Nme = job.getString("name");
                JSONObject ja = new JSONObject("main");
                Temp = ja.getString("temp");
                Toast.makeText(weather_data.this,Temp,Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                Toast.makeText(weather_data.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

            }
            //Toast.makeText(weather_data.this, res, Toast.LENGTH_LONG).show();
            //Toast.makeText(weather_data.this, nm, Toast.LENGTH_SHORT).show();

        }
    }
}
