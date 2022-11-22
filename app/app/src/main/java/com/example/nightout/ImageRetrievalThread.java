package com.example.nightout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageRetrievalThread extends Thread {

    private String imageURL;
    private ImageView imageView;

    public ImageRetrievalThread(String imageURL, ImageView imageView) {
        this.imageURL = imageURL;
        this.imageView = imageView;
    }
    @Override
    public void run() {
        setBitmapFromURL();
    }
    // sourced from outside resource
    public void setBitmapFromURL() {
        try {
            Log.e("src",imageURL);
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            imageView.setImageBitmap(myBitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
        }
    }
}
