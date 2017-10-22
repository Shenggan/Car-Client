package com.example.weakdy.c;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.util.LinkedList;

/**
 * Created by zqm on 17-10-22.
 */

public class ServerSocket extends Thread {
    private java.net.ServerSocket mServer;
    public DataListener mDataListener;
    public LinkedList<Integer> msgs;
    Bitmap cu1r=null;
    public ServerSocket() {
        msgs=new LinkedList<Integer>();
    }
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) ( ((src[offset] & 0xFF)<<24)
                |((src[offset+1] & 0xFF)<<16)
                |((src[offset+2] & 0xFF)<<8)
                |(src[offset+3] & 0xFF));
        return value;
    }
    public void add(int t){
        synchronized (msgs){
            msgs.push(t);
        }
    }
    public void run() {
        super.run();
        //System.out.println("Server is waiting");
        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        java.net.Socket socket = null;
        ByteArrayOutputStream byteArray = null;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (byteArray != null)
                    byteArray.reset();
                else
                    byteArray = new ByteArrayOutputStream();
                System.out.println("ydf:wt 1");
                socket = new java.net.Socket();
                Thread.sleep(1000);

                String ip = IP_connect.IP_addr;
                System.out.println(ip);
                socket.connect(new InetSocketAddress(ip, 8888), 10000); // hard-code server address

                inputStream = new BufferedInputStream(socket.getInputStream());
                outputStream = new BufferedOutputStream(socket.getOutputStream());
                byte[] buff = new byte[256];
                byte[] tmp = null;
                int len = 0;
                String msg = null;
                while (!Thread.currentThread().isInterrupted()&&(len = inputStream.read(buff)) != -1) {
                    System.out.println("ydf:wt 3");
                    msg = new String(buff, 0, len);
                    JsonParser parser = new JsonParser();
                    boolean isJSON = true;
                    JsonElement element = null;
                    try {
                        element =  parser.parse(msg);
                    }
                    catch (JsonParseException e) {
                        System.out.println("exception: " + e);
                        isJSON = false;
                    }
                    if (isJSON && element != null) {
                        //
                        JsonObject obj = element.getAsJsonObject();
                        element = obj.get("type");
                        if (element != null && element.getAsString().equals("data")) {
                            element = obj.get("length");
                            int length = element.getAsInt();
                            element = obj.get("width");
                            int width = element.getAsInt();
                            element = obj.get("height");
                            int height = element.getAsInt();
                            tmp = new byte[length];
                            break;
                        }
                    }
                    else {
                        byteArray.write(buff, 0, len);
                        break;
                    }
                }
                int fp=0;
                if (tmp != null) {
                    JsonObject jsonObj = new JsonObject();
                    jsonObj.addProperty("state", "ok");
                    outputStream.write(jsonObj.toString().getBytes());
                    outputStream.flush();
                    while(!Thread.currentThread().isInterrupted()){
                        synchronized (msgs){
                            while(!msgs.isEmpty()){
                                int t=msgs.poll();
                                outputStream.write(t);
                                outputStream.flush();
                            }
                        }
                        tmp=new byte[4];
                        for(int i=0;i<4;++i) {
                            while(inputStream.read(tmp, i, 1)!=1);
                        }
                        int le1n=bytesToInt2(tmp,0);
                        tmp=new byte[le1n];
                        int cur=0;
                        while(cur<le1n){
                            int t=inputStream.read(tmp,cur,le1n-cur);
                            cur=cur+t;
                        }
                        System.out.println("cxy:reci"+tmp.length+":"+tmp[0]+":"+tmp[500]+":"+tmp[5000]+":"+tmp[tmp.length-5000]);
                        Bitmap uuv= BitmapFactory.decodeByteArray(tmp,0,tmp.length);
                        fp=(fp+1)%80;
                        cu1r=uuv;
                        mDataListener.onDirty(uuv);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
                if (byteArray != null) {
                    byteArray.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void setOnDataListener(DataListener listener) {
        mDataListener = listener;
    }
}

