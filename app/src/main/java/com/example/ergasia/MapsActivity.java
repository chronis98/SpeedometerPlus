package com.example.ergasia;

import androidx.fragment.app.FragmentActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SQLiteDatabase database;
    int x=0;
    ArrayList<LatLng> marker = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        database = openOrCreateDatabase("Test",MODE_PRIVATE,null);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Cursor cursor = database.rawQuery("SELECT * FROM Paravaseis",null);
        if (cursor.getCount()==0) {
            //text3.setText("No records found");
        } else {
            StringBuffer buffer = new StringBuffer();
            while (cursor.moveToNext()){

                //Eisagwgh shmeiwn paraviashs
                marker.add (new LatLng(Double.valueOf(cursor.getString(0)), Double.valueOf(cursor.getString(1))));
                x+=1;
            }
            //text3.setText(buffer.toString());
        }

        for(int y=0; y<marker.size(); y++){
            mMap.addMarker(new MarkerOptions().position(marker.get(y)).title("Paraviash Oriou"));
        }
        if(x>0)mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.get(x-1)));
    }
}
