package com.xyf.utils;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;
import org.apache.http.HttpConnection;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shxiayf on 2015/11/7.
 */
public class LoadImageUtils {

    private static final class Utils
    {
        public static final LoadImageUtils INSTANCES = new LoadImageUtils();
    }

    public static LoadImageUtils getInstances()
    {
        return Utils.INSTANCES;
    }

    public void loadImage(ImageView img,String url)
    {
        if (img == null || TextUtils.isEmpty(url))
        {
            return;
        }
        ImageAsyncTask task = new ImageAsyncTask(img);
        task.execute(url);
    }

    private class ImageAsyncTask extends AsyncTask<String,Integer,Drawable>
    {
        private ImageView m_imageView;

        public ImageAsyncTask(ImageView img)
        {
            this.m_imageView = img;
        }

        @Override
        protected Drawable doInBackground(String... strings) {

            try
            {
                URL m_url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) m_url.openConnection();

                if (connection.getResponseCode() == 200)
                {
                    Drawable m_drawable = Drawable.createFromStream(connection.getInputStream(),"drawable");

                    return m_drawable;
                }

            }
            catch (Exception e)
            {
                LogUtils.exception(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            if (drawable != null)
            {
                m_imageView.setBackgroundDrawable(drawable);
            }
        }
    }

}
