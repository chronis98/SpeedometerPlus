package com.example.ergasia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;

import java.util.ArrayList;

import static com.example.ergasia.MainActivity.REQ_CODE;

public class MenuActivity extends AppCompatActivity {
    TextSpeech ts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ts=new TextSpeech(this);
    }

    public void Speedometer(View view){
        //Anoigma speedometer activity
        Call_Activity(MainActivity.class);
    }
    public void DatabaseSearch(View view){
        //Anoigma database activity
        Call_Activity(Main2Activity.class);
    }
    public void MapView(View view){
        //Anoigma map activity
        Call_Activity(MapsActivity.class);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode==742 && resultCode==RESULT_OK){
                ArrayList<String> results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                StringBuffer buff = new StringBuffer();
                for (String str : results) {
                    buff.append(str.toLowerCase()+"\n");
                }
                //Anagnrish lexewn kai anoigma antistoixn activity mazi me speech mhnuma
                if (buff.toString().contains("map")){
                    ts.speak("Opening Maps Activity");
                    Call_Activity(MapsActivity.class);
                } else if (buff.toString().contains("database search")){
                    ts.speak("Opening Database search activity");
                    Call_Activity(Main2Activity.class);
                } else if (buff.toString().contains("speedometer")){
                    ts.speak("Opening Speedometer activity");
                    Call_Activity(MainActivity.class);
                }

            }


    }
    public void StartRec(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Please say something!");
        startActivityForResult(intent,742);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //elegxos gia permissions kai enarksi hxografishs
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        StartRec();
    }

    public void Call_Activity(Class act){
        Intent intent = new Intent(this, act);
        intent.putExtra("caller", "MenuActivity");
        startActivity(intent);
    }
    public void SpeechRecognition(View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(
                    this,new String[]{Manifest.permission.RECORD_AUDIO},REQ_CODE);

        }else {
          StartRec();
        }

    }
}
