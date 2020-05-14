package com.example.ergasia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.InputType;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Provider;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements LocationListener  {
    float movement_speed;
    SQLiteDatabase database;
    Layout lay1;
    boolean flag=false,warning;
    LocationManager locationManager;
    TextView text1,text2,text3,speed;
    Button btTest;
    Intent intent;
    static final int REQ_CODE = 654;
    SharedPreferences preferences;
    TextSpeech ts;
    Button bt1;
    SpeechRecognizer speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text3=findViewById(R.id.textView8);
        database = openOrCreateDatabase("Test",MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS Paravaseis(Longtitude REAL,Langtitude REAL,Speed REAL,Timestamp TEXT)");



        ts = new TextSpeech(this);
        text1=findViewById(R.id.textView2);
        text2=findViewById(R.id.textView7);
        speed=findViewById(R.id.textView4);
        preferences = getSharedPreferences("SpeedLimit",MODE_PRIVATE);
        speed.setText(preferences.getString("Limit","1"));
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }



    public void Warning(Float lang, Float longt, Float speed, java.util.Date time){
        //Date d=new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        String timestamp= sdf.format(time);

        if(speed>=Integer.parseInt(preferences.getString("Limit","1")) && Integer.parseInt(preferences.getString("Limit","1")) !=1){
            database.execSQL("INSERT INTO Paravaseis VALUES('"+lang+"','"+longt+"','"+speed+"','"+timestamp+"');");
            if(warning){
                Toast.makeText(this,"Ypervash oriou ",Toast.LENGTH_SHORT).show();

                ColorChange(Color.RED);
                ts.speak("Speed Limit Reached!");
                warning=false;
            }
        }else if (speed<Integer.parseInt(preferences.getString("Limit","1"))){
            warning=true;
            ColorChange(Color.WHITE);
        }
    }
    public void OnStopClick(View view){
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location_enable("Disable");
        }
    }


    public void Location_enable(String str){
        if(str=="Enable"){
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
               // text1.setText("Location is \n enabled");
            }
        }else if (str=="Disable"){
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                //text1.setText("Location is disabled \n for battery efficiency");
            }
        }

    }
    public  void SpeedLimitClick(View view){
        final SharedPreferences.Editor editor= getSharedPreferences("SpeedLimit", MODE_PRIVATE).edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Speed Limit");


        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.putString("Limit", input.getText().toString());
                editor.apply();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }
    public void ColorChange(int color){
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Location_enable("Enable");
    }
    public void OnStartClick(View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(
                    this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQ_CODE);
            //Location_enable("Enable");
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Location_enable("Enable");
        }
    }
    @Override
    public void onProviderDisabled(String s) {
        text1.setText("Location is disabled \n for battery efficiency");
        text2.setText("");
    }
    @Override
    public void onProviderEnabled(String s) {
        text1.setText("Location is \n enabled");
    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //metatropi se xiliometra
        movement_speed=((location.getSpeed()*3600)/1000);
        text1.setText(location.getLatitude()+","+location.getLongitude());
        text2.setText(movement_speed+" km/h");

        Warning( (float)location.getLatitude(),(float)location.getLongitude(),movement_speed, Calendar.getInstance().getTime());
    }
}
