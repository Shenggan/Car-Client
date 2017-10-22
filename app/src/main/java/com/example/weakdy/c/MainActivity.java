package com.example.weakdy.c;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity {
    public BluetoothSocket mmSocket;
    public static OutputStream mmOutputStream;
    public static InputStream mmInputStream;
    //public SocketServer server;


    public static BufferedInputStream inputStream = null;
    public static BufferedOutputStream outputStream = null;
    public static Socket socket = null;
    public static ByteArrayOutputStream byteArray = null;
    public static boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        socket = new Socket();


        //Set IP Address
        TextView IP = (TextView) findViewById(R.id.IP);
        final TextView IP_address = (TextView) findViewById(R.id.IP_address);
        final TextView wifi = (TextView) findViewById(R.id.wifi);
        IP.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        IP_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        wifi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);

        refresh(IP_address,wifi);


        final MaterialDialog mMaterialDialog = new MaterialDialog(this)
                .setTitle("注意事项")
                .setMessage("请确保WIFI处于连接状态!\n请确保所连接的设备在同一局域网下！");
        mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        }).setNegativeButton("CANCEL", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });


        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , IP_connect.class));
            }
        });
    }
    public void refresh(TextView IP_address,TextView wifi){
        IP_address.setText(getIpAddress());
        if(get_wifi_info().equals("<unknown ssid>")){
            wifi.setText("No WIFI Connection");
        }
        else wifi.setText("WIFI:"+get_wifi_info());
    }

    private String getIpAddress(){
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        String ipAddressFormatted = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return ipAddressFormatted;
    }

    private String get_wifi_info(){
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wi = wifiManager.getConnectionInfo();
        return wi.getSSID();
    }
}


