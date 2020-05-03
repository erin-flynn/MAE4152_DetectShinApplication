package com.example.detecshinfinal;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Main2Activity extends BlunoLibrary implements AdapterView.OnItemSelectedListener{
    private Button buttonScan;
    private TextView serialReceivedText;
    EditText fileName;
    Button export;
    float data;
    int time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        onCreateProcess();


        serialReceivedText = (TextView) findViewById(R.id.dataWindow);

        TextView mTitleWindow = (TextView) findViewById(R.id.titleWindow);

        mTitleWindow.setText("Accelerometer Data");

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }
        else if(!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            int REQUEST_ENABLE_BT=1;
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            serialBegin(115200);


        }
        else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            serialBegin(115200);


        }


        buttonScan = (Button) findViewById(R.id.bluetooth);					//initial the button for scanning the BLE device
        buttonScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED){
                    boolean requestCheck = ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                    if (requestCheck){
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }else {
                        new AlertDialog.Builder(Main2Activity.this)
                                .setTitle("Permission Required")
                                .setMessage("Please enable location permission to use this application.")
                                .setNeutralButton("I Understand", null)
                                .show();
                    }
                }else {
                    buttonScanOnClickProcess(); //Alert Dialog for selecting the BLE device
                }//Alert Dialog for selecting the BLE device

            }
        });



        fileName = (EditText) findViewById(R.id.fileName);
        fileName.setEnabled(true);
        export = (Button) findViewById(R.id.export);

        export.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000 );
                }
                String filename = fileName.getText().toString();
                String content = serialReceivedText.getText().toString();

                if(!filename.equals("") && !content.equals("")) {
                    saveTextAsFile(filename, content);
                }
            }
        });





    }
    protected void onResume(){
        super.onResume();
        System.out.println("BlUNOActivity onResume");
        onResumeProcess();														//onResume Process by BlunoLibrary
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();														//onPause Process by BlunoLibrary
    }

    protected void onStop() {
        super.onStop();
        onStopProcess();														//onStop Process by BlunoLibrary
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }


    @Override
    public void onConnectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
        switch (theConnectionState) {											//Four connection state
            case isConnected:
                buttonScan.setText("Connected");

                break;
            case isConnecting:
                buttonScan.setText("Connecting");

                break;
            case isToScan:
                buttonScan.setText("Scan");

                break;
            case isScanning:
                buttonScan.setText("Scanning");

                break;
            case isDisconnecting:
                buttonScan.setText("isDisconnecting");

                break;
            default:
                break;
        }
    }



    @Override
    public void onSerialReceived(String theString) {
        //startActivity(new Intent(MainActivity.this, Main2Activity.class));

        //Once connection data received, this function will be called
        // TODO Auto-generated method stub
        //String[] xyz=theString.split("\\\\r?\\\\n");
        //double x=Double.parseDouble(xyz[0]);
        //double y=Double.parseDouble(xyz[1]);
        //double z=Double.parseDouble(xyz[2]);
        //double result=Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
        //String resultant=Double.toString(result);

        serialReceivedText.append(theString);							//append the text into the EditText
        //data=Float.parseFloat(theString);
        //time=(Integer.parseInt(MainActivity.interval))*1000;
        //long t= System.currentTimeMillis();
        //long end = t+time;
        //float total=0;
        //int i=0;
        //while(System.currentTimeMillis() < end) {
            //if(data<3){

            //}
            //else{
               // total=total+data;
               /* i=i+1;
            }
        }
        float avgaccel;
        avgaccel=total/i;*/
        //The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
        ((ScrollView)serialReceivedText.getParent()).fullScroll(View.FOCUS_DOWN);
    }

    private void saveTextAsFile(String filename, String content) {
        String fileName = filename + ".txt";

        //create file
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);

        //write to file
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Saving!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length==0 ){

        }
        else{
            switch(requestCode) {
                case 1000:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}




