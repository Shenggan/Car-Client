package com.example.weakdy.c;

import android.graphics.Bitmap;

/**
 * Created by zqm on 17-10-22.
 */

public interface DataListener {
    void onDirty(Bitmap bufferedImage);
    void conv(int t);
}
