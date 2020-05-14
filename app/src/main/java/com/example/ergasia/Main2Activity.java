package com.example.ergasia;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    EditText edit1;
    String value;
    Button bt;
    SQLiteDatabase database;
    android.widget.ArrayAdapter<String> adapter;
    android.widget.ListView list;
    ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bt=findViewById(R.id.button3);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.execSQL("Delete from Paravaseis;");
            }
        });
    }

    public void PromptUser(final String mode){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(mode=="Month") builder.setTitle("Enter prefered month of current Year (1-12)");
        else if(mode=="Year")builder.setTitle("Enter prefered Year");

        final EditText inputt = new EditText(this);

        inputt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        builder.setView(inputt);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               value=inputt.getText().toString();
                Toast.makeText(getApplicationContext(),value,Toast.LENGTH_LONG).show();
                if(mode=="Year")SqlQuery("Select Longtitude,Langtitude,Speed,Timestamp  from Paravaseis where substr(Timestamp,1,4)='"+value+"' ;");
                else SqlQuery("Select Longtitude,Langtitude,Speed,Timestamp  from Paravaseis where substr(Timestamp,6,2)='"+value+"' ;");

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                value="";
                dialog.cancel();
            }
        });

        builder.show();


    }
    public void SqlQuery(String str){
        arrayList = new java.util.ArrayList<>();
        database = openOrCreateDatabase("Test",MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery(str,null);
        if (cursor.getCount()==0) {
            Toast.makeText(this,"No records found!",Toast.LENGTH_LONG).show();
        }else {
            StringBuffer buffer = new StringBuffer();
            while (cursor.moveToNext()){
                buffer.append(cursor.getString(0));
                buffer.append(",  ");
                buffer.append(cursor.getString(1));
                buffer.append(",   ");
                buffer.append(cursor.getString(2));
                buffer.append(",  ");
                buffer.append((cursor.getString(3)));
                buffer.append("\n");
            }
            arrayList.add(buffer.toString());
        }
        list = findViewById(R.id.ListView);
        adapter = new android.widget.ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(adapter);
    }

    public void All(View view){
        //SqlQuery("Select Longtitude,Langtitude,Speed,Timestamp as a from Paravaseis where substr(Timestamp,1,4)='1969' ;");
        //SqlQuery("Select * from Paravaseis ;");
        SqlQuery("Select Longtitude,Langtitude,Speed,Timestamp  from Paravaseis  ;");
    }

    public void Month(View view){
        PromptUser("Month");


    }

    public void Year(View view){
     PromptUser("Year");




    }
}
