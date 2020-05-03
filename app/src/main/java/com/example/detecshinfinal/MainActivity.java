package com.example.detecshinfinal;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import com.example.detecshinfinal.login.LoginActivity;


import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity  extends Activity implements AdapterView.OnItemSelectedListener {

    private Button start;
    public static String interval;
    public static String terrain;
    public static String weightlbs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent activityIntent;
        activityIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(activityIntent, 123);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.intervaltimespinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.intervaltimespinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        interval=spinner.getSelectedItem().toString();
        Spinner tspin = findViewById(R.id.spinner);
        terrain=tspin.getSelectedItem().toString();
        EditText weight=findViewById(R.id.weightlbs);
        weightlbs=weight.getText().toString();
        start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




}