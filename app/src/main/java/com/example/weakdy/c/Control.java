package com.example.weakdy.c;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import android.support.design.widget.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.gcssloop.widget.RockerView;
import android.widget.ImageView;
import java.io.InputStream;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import java.io.InputStreamReader;
import java.io.OutputStream;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.util.UUID;
import java.util.Set;
import java.io.BufferedReader;

import android.os.Handler;
import android.os.Message;


import com.gcssloop.widget.RockerView;

public class Control extends AppCompatActivity {

    public static TextView output;
    public static ImageView img_view;
    public OutputStream mmOutputStream;
    public InputStream mmInputStream;
    public BluetoothSocket mmSocket;


    private BluetoothDevice mmDevice;
    private BluetoothAdapter mBTAdapter;
    private UUID uuid;
    private BufferedReader pht;

    public static String bluetoothmsg;
    public static boolean freeze = false;
    public static boolean once = false;
    public static String re_json = "None Results";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBTAdapter.getBondedDevices();

        if(pairedDevices.size()!=0)
            mmDevice = pairedDevices.iterator().next();
        uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        }
        catch (Exception e){
        }


        new Thread(new Runnable(){
            public void run() {
                while(true) {
                    try {
                        if(sendData(bluetoothmsg)) {
                                System.out.println("Bluetooth send succefully!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                Thread.sleep(1000);
                        continue;
                        }
                        mmSocket.connect();
                        mmOutputStream = mmSocket.getOutputStream();
                        mmInputStream = mmSocket.getInputStream();
                        pht=new BufferedReader(new InputStreamReader(mmInputStream));
                    } catch (Exception e) {}
                }
            }
        }).start();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_control);

        output = (TextView) findViewById(R.id.text_output);
        final Button loginVoice = (Button) findViewById(R.id.btn_voice);
        final Button SensorExit = (Button) findViewById(R.id.SensorExit);
        final TextView output = (TextView) findViewById(R.id.text_output);
        final RockerView rocker = (RockerView) findViewById(R.id.rocker);
        final Button loginSensor = (Button) findViewById(R.id.btn_sensor);

        loginVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Control.this, VoiceActivity.class);
                startActivityForResult(intent,2);
            }
        });
        img_view = (ImageView)this.findViewById(R.id.imageView3);
        try {
            BitmapDrawable bmpMeizi = new BitmapDrawable(getAssets().open("@drawable/background"));
            Bitmap mBitmap = bmpMeizi.getBitmap();
            img_view.setImageBitmap(mBitmap);
            System.out.println("Image!!!!!!!!!!!!!!!!!!!!!***********************************");
        }
        catch (java.io.IOException e){
            System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!***********************************");
        }

        loginSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("123");
                Intent intent = new Intent(Control.this, SensorActivity.class);
                startActivity(intent);
            }
        });

        if (null != rocker){
            rocker.setListener(new RockerView.RockerListener() {

                @Override
                public void callback(int eventType, int currentAngle, float currentDistance) {
                    switch (eventType) {
                        case RockerView.EVENT_ACTION:
                            // 触摸事件回调7
                            //output.setText(currentAngle);
                            System.out.println("angle============================================"+currentAngle);
                            //info_display.setText("angle="+currentAngle+" - distance"+currentDistance);
                            if(currentAngle>315||0<currentAngle&&currentAngle<=45){Control.bluetoothmsg="d" ;}
                            else if(45<currentAngle&&currentAngle<135){Control.bluetoothmsg="w" ;}
                            else if(135<=currentAngle&&currentAngle<225){Control.bluetoothmsg="a" ;}
                            else if(225<=currentAngle&&currentAngle<=315){Control.bluetoothmsg="s" ;}
                            else{Control.bluetoothmsg="x" ;}
                            break;
                        case RockerView.EVENT_CLOCK:
                            break;
                    }
                }
            });
        }

        FloatingActionButton fb1 = (FloatingActionButton) findViewById(R.id.fb1);
        fb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                output.setText("");
                rocker.setVisibility(View.VISIBLE);
                loginVoice.setVisibility(View.INVISIBLE);
                loginSensor.setVisibility(View.INVISIBLE);
                SensorExit.setVisibility(View.INVISIBLE);
            }
        });

        FloatingActionButton fb2 = (FloatingActionButton) findViewById(R.id.fb2);
        fb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                output.setText("");
                rocker.setVisibility(View.INVISIBLE);
                loginVoice.setVisibility(View.VISIBLE);
                loginSensor.setVisibility(View.INVISIBLE);
                SensorExit.setVisibility(View.INVISIBLE);
            }
        });

        FloatingActionButton fb3 = (FloatingActionButton) findViewById(R.id.fb3);
        fb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                output.setText("");
                rocker.setVisibility(View.INVISIBLE);
                loginVoice.setVisibility(View.INVISIBLE);
                loginSensor.setVisibility(View.VISIBLE);
                SensorExit.setVisibility(View.VISIBLE);
            }
        });

        FloatingActionButton fb4 = (FloatingActionButton) findViewById(R.id.fb4);
        fb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (freeze == false) {
                    freeze = true;
                    rocker.setVisibility(View.INVISIBLE);
                    output.setText("Anylsing...");
                } else {
                    output.setText("");
                    freeze = false;
                    once = false;
                    rocker.setVisibility(View.VISIBLE);
                }
                loginVoice.setVisibility(View.INVISIBLE);
                loginSensor.setVisibility(View.INVISIBLE);
                SensorExit.setVisibility(View.INVISIBLE);
            }
        });
        output.setText("");
        rocker.setVisibility(View.VISIBLE);
        loginVoice.setVisibility(View.INVISIBLE);
        loginSensor.setVisibility(View.INVISIBLE);
        SensorExit.setVisibility(View.INVISIBLE);
        /*
        findViewById(R.id.pink_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Control.this, "Clicked pink Floating Action Button", Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.setter);
        button.setSize(FloatingActionButton.SIZE_MINI);
        button.setColorNormalResId(R.color.pink);
        button.setColorPressedResId(R.color.pink_pressed);
        button.setIcon(R.drawable.ic_fab_star);
        button.setStrokeVisible(false);

        final View actionB = findViewById(R.id.action_b);

        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("Hide/Show Action above");
        actionC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        menuMultipleActions.addButton(actionC);

        final FloatingActionButton removeAction = (FloatingActionButton) findViewById(R.id.button_remove);
        removeAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FloatingActionsMenu) findViewById(R.id.multiple_actions_down)).removeButton(removeAction);
            }
        });

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.white));
        ((FloatingActionButton) findViewById(R.id.setter_drawable)).setIconDrawable(drawable);

        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionA.setTitle("Action A clicked");
            }
        });

        // Test that FAMs containing FABs with visibility GONE do not cause crashes
        findViewById(R.id.button_gone).setVisibility(View.GONE);

        final FloatingActionButton actionEnable = (FloatingActionButton) findViewById(R.id.action_enable);
        actionEnable.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                menuMultipleActions.setEnabled(!menuMultipleActions.isEnabled());
            }
        });*/

        //FloatingActionsMenu rightLabels = (FloatingActionsMenu) findViewById(R.id.right_labels);
        /*FloatingActionButton addedOnce = new FloatingActionButton(this);
        addedOnce.setTitle("Added once");
        rightLabels.addButton(addedOnce);

        FloatingActionButton addedTwice = new FloatingActionButton(this);
        addedTwice.setTitle("Added twice");
        //addedTwice.setSize(10);
        rightLabels.addButton(addedTwice);
        rightLabels.removeButton(addedTwice);
        rightLabels.addButton(addedTwice);*/
    }

    static public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //完成主界面更新,拿到数据
                    Bitmap data = (Bitmap)msg.obj;
                    img_view.setImageBitmap(data);
                    break;
                default:
                    break;
            }
        }

    };

    static public Handler mHandler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    output.setText(re_json);
                    System.out.println(re_json);
                    break;
                default:
                    break;
            }
        }

    };


//    @Override
//    public void onClick(View v) {
//        System.out.println(v.getId());
//        switch (v.getId()) {
//            case R.id.btn_voice:
//                Intent intent = new Intent(Control.this,VoiceActivity.class);
//                startActivity(intent);
////                   Intent intent = new Intent(Control.this, VoiceActivity.class);
////                   startActivityForResult(intent,2);
//
////                VoiceActivity.this.finish();
//                break;
//            case R.id.btn_sensor:
//             //   Intent intent = new Intent(Control.this, SensorActivity.class);
//             //   startActivityForResult(intent,2);
////                VoiceActivity.this.finish();
//                break;
//        }
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getExtras().getString("result");
        output.setText(result);
    }

    boolean sendData(String m){
        try{
            String msg = m;
            mmOutputStream.write(msg.getBytes());
            System.out.println("Data Sent!");
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
