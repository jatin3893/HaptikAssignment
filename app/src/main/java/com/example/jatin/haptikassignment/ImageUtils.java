package com.example.jatin.haptikassignment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jatin on 24-02-2017.
 */
class ImageUtils {
    private static Map<String, Bitmap> sDownloadedImageMap = new HashMap<>();

    static void imageFromUrl(final ImageView imageView, String url) {
        if (url == null)
            return;

        AsyncTask<String, Void, Bitmap> downloadImageTask = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... urls) {
                String imageUrl = urls[0];
                Bitmap imageBitmap = sDownloadedImageMap.get(imageUrl);
                try {
                    if (imageBitmap == null) {
                        InputStream in = new URL(imageUrl).openStream();
                        imageBitmap = BitmapFactory.decodeStream(in);
                        sDownloadedImageMap.put(imageUrl, imageBitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return imageBitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap != null)
                    imageView.setImageBitmap(bitmap);
            }
        };
        downloadImageTask.execute(url);
    }
}
