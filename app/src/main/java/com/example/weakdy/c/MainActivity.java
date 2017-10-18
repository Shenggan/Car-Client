package com.example.weakdy.c;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
        setContentView(R.layout.activity_main);
        socket = new Socket();
        //Set IP Address
        TextView IP_content = (TextView) findViewById(R.id.IP_content);
        IP_content.setText("Your IP Address is:"+getIpAddress());
        TextView wifi_info = (TextView) findViewById(R.id.wifi_info);
        if(get_wifi_info().equals("<unknown ssid>")){
            wifi_info.setText("No WIFI Connection");
        }
        else wifi_info.setText("Wifi:"+get_wifi_info());
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , IP_connect.class));
            }
        });
    }
    private String getIpAddress(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        String ipAddressFormatted = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return ipAddressFormatted;
    }

    private String get_wifi_info(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wi = wifiManager.getConnectionInfo();
        return wi.getSSID();
    }
}


