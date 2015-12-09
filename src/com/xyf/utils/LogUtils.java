package com.xyf.utils;

import android.util.Log;

/**
 * Created by shxiayf on 2015/11/6.
 */
public class LogUtils {

    public static void e(String tag,String message)
    {
        if (Configs.debug)
        {
            Log.e(tag,message);
        }
    }

    public static void i(String tag,String mesage)
    {
        if(Configs.debug)
        {
            Log.i(tag,mesage);
        }
    }

    public static void exception(Exception e)
    {
        if (Configs.debug)
        {
            e.printStackTrace();
        }
    }

}
