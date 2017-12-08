package com.example.weakdy.c;

/**
 * Created by Leonardo on 2017/10/18.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gcssloop.widget.RockerView;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class VoiceActivity extends Activity {

    private static final String TAG = VoiceActivity.class.getSimpleName();
    private TextView output;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_control);

        final Button loginVoice = (Button) findViewById(R.id.btn_voice);
        final Button SensorExit = (Button) findViewById(R.id.SensorExit);
        final RockerView rocker = (RockerView) findViewById(R.id.rocker);
        final Button loginSensor = (Button) findViewById(R.id.btn_sensor);

        rocker.setVisibility(View.INVISIBLE);
        loginVoice.setVisibility(View.INVISIBLE);
        loginSensor.setVisibility(View.INVISIBLE);
        SensorExit.setVisibility(View.INVISIBLE);

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=59e5e80a");
        startSpeechDialog();
    }

    private void startSpeechDialog() {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, null);
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                if (!isLast) {
                    //解析语音
                    String result = parseVoice(recognizerResult.getResultString());
                    Intent intent = new Intent();
                    //把返回数据存入Intent
                    intent.putExtra("result", result);
                    //设置返回数据
                    VoiceActivity.this.setResult(2, intent);
                    //关闭Activity
                    //output.setText(result);
                    VoiceActivity.this.finish();
                    //
                    //tv.setText(result);
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        //4.显示dialog，接收语音输入
        mDialog.show();

    }

    /**
     * 解析语音json
     */
    public String parseVoice(String resultString) {
        //TextView textleft = (TextView) findViewById(R.id.text2);
        Gson gson = new Gson();
        Voice voiceBean = gson.fromJson(resultString, Voice.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<Voice.WSBean> ws = voiceBean.ws;
        for (Voice.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        String text = sb.toString();
        String stringA = "前进";
        String stringB = "后退";
        String stringC = "停止";
        String stringD = "左转";
        String stringE = "右转";
        if (text.equals(stringA)) {
            Control.bluetoothmsg="w" ;
            //textleft.setText("f");
            //sendMessage("F#");
        }
        if (text.equals(stringB)) {
            Control.bluetoothmsg="s" ;
            //textleft.setText("b");
            //sendMessage("B#");
        }
        if (text.equals(stringC)) {
            Control.bluetoothmsg="x" ;
            //textleft.setText("s");
            //sendMessage("P#");
        }
        if (text.equals(stringD)) {
            Control.bluetoothmsg="a" ;
            //textleft.setText("l");
            //sendMessage("L#");
        }
        if (text.equals(stringE)) {
            Control.bluetoothmsg="d" ;
            //textleft.setText("r");
            //sendMessage("R#");
        }
        return sb.toString();
    }

    public class Voice {

        public ArrayList<WSBean> ws;

        public class WSBean {
            public ArrayList<CWBean> cw;
        }

        public class CWBean {
            public String w;
        }
    }
}

