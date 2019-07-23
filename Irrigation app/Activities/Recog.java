package com.example.abdeali.irrigation;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class Recog extends AppCompatActivity {

    private DrawerLayout d1;
    public Intent it;
    private ActionBarDrawerToggle abt;
    private NavigationView navview;

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recog);

        //UI Initialization
        d1 = (DrawerLayout)findViewById(R.id.drr);
        abt = new ActionBarDrawerToggle(Recog.this,d1,R.string.Open,R.string.Close);
        abt.setDrawerIndicatorEnabled(true);
        d1.addDrawerListener(abt);
        abt.syncState();
        navview = (NavigationView)findViewById(R.id.navview);
        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.i1){
                    it = new Intent(Recog.this,MainActivity.class);
                    startActivity(it);
                }else if(id == R.id.i2){
                    it = new Intent(Recog.this,Recog.class);
                    startActivity(it);
                }else{
                    it = new Intent(Recog.this,weather_data.class);
                    startActivity(it);
                }

                return true;
            }
        });

        //Check for camera hardware

    }
}
