package com.xyf.database;

import android.provider.BaseColumns;

/**
 * Created by shxiayf on 2015/11/9.
 */
public class VideoDataBase implements BaseColumns {

    public static final String DB_NAME = "video.db";

    public static final String TB_NAME = "video_list";

    public static final String COL_NAME = "v_name";
    public static final String COL_PATH = "v_path";
    public static final String COL_IMGURL = "v_img";

}
