package com.example.musicforlife.utilitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.LruCache;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class CacheHelper {
    private LruCache<String, Bitmap> memoryCache;
    public static CacheHelper instance;
    private CacheHelper(){
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }
    public static CacheHelper Instance(){
        if(instance == null){
            instance = new CacheHelper();
        }
        return instance;
    }

    public void addBitmapToMemoryCache(String key) {
        if (getBitmapFromMemCache(key) == null) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(key);
            if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
                InputStream inputStream = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
                mediaMetadataRetriever.release();
                Bitmap bitmapValue = BitmapFactory.decodeStream(inputStream);
                memoryCache.put(key, bitmapValue);
            }
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }
}
