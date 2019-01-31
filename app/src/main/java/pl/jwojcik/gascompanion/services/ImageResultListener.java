package pl.jwojcik.gascompanion.services;

import android.graphics.Bitmap;


public interface ImageResultListener {
    public void onResult(boolean isSuccess, String error, Bitmap bitmap);
}
