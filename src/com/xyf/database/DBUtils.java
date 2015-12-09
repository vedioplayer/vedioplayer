package com.xyf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.xyf.model.VideoItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shxiayf on 2015/11/7.
 */
public class DBUtils {

    private final static class Utils {
        public static final DBUtils INSTANCE = new DBUtils();
    }

    public static DBUtils getInstances()
    {
        return Utils.INSTANCE;
    }

    private MyDBOpenHelper m_openHelper;
    public void initDB(Context mCtx)
    {
        if (m_openHelper == null)
        {
            m_openHelper = new MyDBOpenHelper(mCtx,VideoDataBase.DB_NAME,null,1);
        }
    }

    public List<VideoItemModel> queryDB(Context mCtx)
    {
        List<VideoItemModel> m_list = new ArrayList<VideoItemModel>();

        if (m_openHelper == null)
        {
            initDB(mCtx);
        }

        SQLiteDatabase sqlite = m_openHelper.getReadableDatabase();
        Cursor m_cursor = sqlite.query(VideoDataBase.TB_NAME,null,null,null,null,null,null);

        if (m_cursor.moveToNext())
        {
            m_cursor.moveToFirst();

            do {
                String name = m_cursor.getString(m_cursor.getColumnIndex(VideoDataBase.COL_NAME));
                String path = m_cursor.getString(m_cursor.getColumnIndex(VideoDataBase.COL_PATH));
                String img = m_cursor.getString(m_cursor.getColumnIndex(VideoDataBase.COL_IMGURL));
                VideoItemModel tmp = new VideoItemModel();
                tmp.setName(name);
                tmp.setImageUri(img);
                tmp.setVideoPath(path);
                m_list.add(tmp);
            }while (m_cursor.moveToNext());
        }


        return m_list;
    }

    public long insert(Context mCtx,VideoItemModel model)
    {
        if (m_openHelper == null)
        {
            initDB(mCtx);
        }

        ContentValues values = new ContentValues();
        values.put(VideoDataBase.COL_NAME,model.getName());
        values.put(VideoDataBase.COL_PATH,model.getVideoPath());
        values.put(VideoDataBase.COL_IMGURL,model.getImageUri());

        SQLiteDatabase sqlite = m_openHelper.getWritableDatabase();
        return sqlite.insert(VideoDataBase.TB_NAME,VideoDataBase.COL_IMGURL,values);
    }

    public int delete(Context mCtx,VideoItemModel model)
    {
        if (m_openHelper == null)
        {
            initDB(mCtx);
        }

        SQLiteDatabase sqlite = m_openHelper.getWritableDatabase();
        return sqlite.delete(VideoDataBase.TB_NAME,VideoDataBase.COL_NAME + " = ? and " + VideoDataBase.COL_PATH + " = ?",new String[]{model.getName(),model.getVideoPath()});
    }

    private class MyDBOpenHelper extends SQLiteOpenHelper
    {

        public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql = "CREATE TABLE " + VideoDataBase.TB_NAME + " (" +
                            VideoDataBase._ID  + " INTEGER PRIMARY KEY," +
                            VideoDataBase.COL_NAME + " TEXT NOT NULL," +
                            VideoDataBase.COL_IMGURL + " TEXT," +
                            VideoDataBase.COL_PATH + " TEXT NOT NULL);";

            sqLiteDatabase.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

}
