package com.example.abdeali.irrigation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout d1;
    public Intent it;
    private ActionBarDrawerToggle abt;
    private NavigationView navview;
    private FirebaseDatabase database ,dbse;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    Switch sv;
    private DatabaseReference ref, ref_motor;
    static int mode;
    static TextView tv;
    static String moisture_value;
    Button aut, man, b;



    public void go_notification(String msg){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder bld = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Irrigation")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);


        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{400,400});
            assert manager != null;
            bld.setChannelId(NOTIFICATION_CHANNEL_ID);
            manager.createNotificationChannel(notificationChannel);
        }
        assert manager != null;
        manager.notify(0, bld.build());
    }

    public void onClick_mode(View view){
        aut.setBackgroundResource(android.R.drawable.btn_default);
        man.setBackgroundResource(android.R.drawable.btn_default);
        view.setBackgroundColor(Color.parseColor("#00ff00"));
        if (view.getId() == R.id.a){
            mode = 1;
        }else{
            mode = 0;
        }
    }



    public void check(String moisture_value){
        if (mode == 1){
            if (Integer.parseInt(moisture_value) <= 0){
                sv.setChecked(true);
                go_notification("Moisture is less pump is ON");
            }else{
                sv.setChecked(false);
                go_notification("Moisture is sufficient pump is OFF");
            }
        }else if (mode == 0){

        }
        /*if (Integer.parseInt(moisture_value) <= 0){
            if (mode == 1){
                sv.setChecked(true);
                go_notification("your moisture is less then needed auto mode has turned on the pump.");
            }else{
                go_notification("Your Moisture is less than needed");
            }
        }else{
            if (mode == 1){
                sv.setChecked(false);
            }else{

            }
        }*/
        if (sv.isChecked()){
            ref_motor.setValue("1");
        }else{
            ref_motor.setValue("0");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        //UI initialization

        d1 = (DrawerLayout)findViewById(R.id.dr);
        abt = new ActionBarDrawerToggle(this,d1,R.string.Open,R.string.Close);
        abt.setDrawerIndicatorEnabled(true);
        d1.addDrawerListener(abt);
        abt.syncState();
        navview = (NavigationView)findViewById(R.id.navview);
        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.i1){
                    it = new Intent(MainActivity.this,MainActivity.class);
                    startActivity(it);
                }else if(id == R.id.i2){
                    it = new Intent(MainActivity.this,Recog.class);
                    startActivity(it);
                }else{
                    it = new Intent(MainActivity.this,weather_data.class);
                    startActivity(it);
                }

                return true;
            }
        });
        aut = (Button)findViewById(R.id.a);
        man = (Button)findViewById(R.id.m);
        sv = (Switch)findViewById(R.id.switch1);
        tv = (TextView)findViewById(R.id.textView);

        //Database Initialization

        database = FirebaseDatabase.getInstance();
        //dbse = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref_motor = database.getReference("Motor");

        //Database Extract

        ref.child("Moisture").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                moisture_value = dataSnapshot.getValue().toString();
                tv.setText(moisture_value);
                check(moisture_value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                tv.setText("Failed to read :"+ databaseError.toException());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
