package com.example.weakdy.c;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_control) ;

        msensorManager=(SensorManager)this.getSystemService(SENSOR_SERVICE);//获取SensorManager
        msensor= msensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//获取Sensor
      /*  if(msensorManager==null){
            Log.d("ppaabb","Not Find device");
        }*/
        msensorManager.registerListener(this,msensor,SensorManager.SENSOR_DELAY_GAME);//注册监听器
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
                output=("停止");intent.putExtra("result", output);SensorActivity.this.setResult(1, intent);SensorActivity.this.finish();
            }else if(x<-3 &&y>-2&& y<2 && z<8) {
                output=("前进");intent.putExtra("result", output);SensorActivity.this.setResult(1, intent);SensorActivity.this.finish();
            }else if(x>3 &&y>-2&& y<2 && z<8) {
                output=("后退");intent.putExtra("result", output);SensorActivity.this.setResult(1, intent);SensorActivity.this.finish();
            }else if(y<-4 &&x>-2&& x<2 && z<8){
                output=("左转");intent.putExtra("result", output);SensorActivity.this.setResult(1, intent);SensorActivity.this.finish();
            }else if(y>4 &&x>-2&& x<2 && z<8){
                output=("右转");intent.putExtra("result", output);SensorActivity.this.setResult(1, intent);SensorActivity.this.finish();
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
