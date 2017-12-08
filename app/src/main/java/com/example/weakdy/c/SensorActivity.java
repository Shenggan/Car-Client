package com.example.weakdy.c;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gcssloop.widget.RockerView;

public class SensorActivity extends Activity implements SensorEventListener {
    private SensorManager msensorManager;
    private Sensor msensor;
    private TextView testx;
    private TextView testy;
    private TextView testz;
    private String output;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_control) ;

        msensorManager=(SensorManager)this.getSystemService(SENSOR_SERVICE);//获取SensorManager
        msensor= msensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//获取Sensor
        /*  if(msensorManager==null){
            Log.d("ppaabb","Not Find device");
        }*/
        msensorManager.registerListener(this,msensor,SensorManager.SENSOR_DELAY_GAME);//注册监听器
        final Button loginSensor = (Button) findViewById(R.id.btn_sensor);
        final Button loginVoice = (Button) findViewById(R.id.btn_voice);
        final Button SensorExit = (Button) findViewById(R.id.SensorExit);
        final RockerView rocker = (RockerView) findViewById(R.id.rocker);

        rocker.setVisibility(View.INVISIBLE);
        loginVoice.setVisibility(View.INVISIBLE);
        loginSensor.setVisibility(View.VISIBLE);
        SensorExit.setVisibility(View.VISIBLE);

        SensorExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SensorActivity.this.finish();
            }
        });
    }
    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }
    @Override
    public void onSensorChanged(SensorEvent event){//实现感应检测的监听功能
        if(event.sensor==null){
            return;
        }
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            int x=(int)event.values[0];
            int y=(int)event.values[1];
            int z=(int)event.values[2];
            Intent intent = new Intent();
            if (x>-1 && x<1 &&y>-1 &&y<1 && z>8) {
                //output=("停止");intent.putExtra("result", output);
                Control.bluetoothmsg="x" ;
                //SensorActivity.this.setResult(1, intent);SensorActivity.this.finish();
            }else if(x<-3 &&y>-2&& y<2 && z<8) {
                //output=("前进");intent.putExtra("result", output);
                Control.bluetoothmsg="w" ;
                //SensorActivity.this.setResult(1, intent);//SensorActivity.this.finish();
            }else if(x>3 &&y>-2&& y<2 && z<8) {
                //output=("后退");intent.putExtra("result", output);
                Control.bluetoothmsg="s" ;
                //SensorActivity.this.setResult(1, intent);//SensorActivity.this.finish();
            }else if(y<-4 &&x>-2&& x<2 && z<8){
                //output=("左转");intent.putExtra("result", output);
                Control.bluetoothmsg="a" ;
                //SensorActivity.this.setResult(1, intent);//SensorActivity.this.finish();
            }else if(y>4 &&x>-2&& x<2 && z<8){
                //output=("右转");intent.putExtra("result", output);
                Control.bluetoothmsg="d" ;
                //SensorActivity.this.setResult(1, intent);//SensorActivity.this.finish();
            }
        }

    }

    @Override
    protected void onStop(){
        super.onStop();
        if(msensorManager!=null){
            msensorManager.unregisterListener(this);
        }
    }

}
