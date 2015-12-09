package com.xyf.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.xyf.utils.Configs;
import com.xyf.utils.LogUtils;
import com.xyf.video.adapter.FileExplorerAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shxiayf on 2015/11/10.
 */
public class FileExplorerActivity extends Activity implements AdapterView.OnItemClickListener{

    private static final String LASTPATH = "...";

    private String BASEPATH = "";
    private String CPATH = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filelist);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) )
        {
            new AlertDialog.Builder(this)
                    .setTitle("notification")
                    .setMessage("no sdcard found on this phone!")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .create()
                    .show();
            return;
        }

        BASEPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        CPATH = BASEPATH;


        initViews();

        initDatas();
    }

    private List<String> m_datas;
    private void initDatas() {

        File fpath = new File(CPATH);
        if (!fpath.isDirectory())
        {
            finish();
            return;
        }

        m_datas = getFileList(fpath.getAbsolutePath());

        m_adapter = new FileExplorerAdapter(this,m_datas);
        m_listview.setAdapter(m_adapter);
    }

    private ListView m_listview;
    private FileExplorerAdapter m_adapter;

    private void initViews() {

        m_listview = (ListView) findViewById(R.id.filelist_list);
        m_listview.setOnItemClickListener(this);

    }


    private String getNextPath(String path)
    {
        return path.substring(0,path.lastIndexOf("/"));
    }

    private List<String> getFileList(String path)
    {
        LogUtils.i(FileExplorerActivity.class.getSimpleName(),"path:" + path);
        File f = new File(path);
        File[] mfiles = f.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                if (s.startsWith(".")) {
                    return false;
                }
                return true;
            }
        });

        List<String> m_list = new ArrayList<String>();
        if (!path.equals(BASEPATH))
        {
            m_list.add(LASTPATH);
        }

        for (int i = 0 ;i<mfiles.length;i++)
        {
            m_list.add(mfiles[i].getName());
        }

        return m_list;

    }

    private boolean isMediaFile(String name)
    {
        if (name.endsWith(".mp4") ||name.endsWith(".rmvb") || name.endsWith(".3gp"))
        {
            return true;
        }

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String filename = (String) m_adapter.getItem(i);

        if (filename.equals(LASTPATH))
        {
            CPATH = getNextPath(CPATH);
        }
        else
        {
            CPATH =  CPATH + File.separator + filename;
        }

        File nextDir = new File(CPATH);

        if (nextDir.isDirectory())
        {
            m_datas = getFileList(CPATH);
            m_adapter.setM_lists(m_datas);
            m_adapter.notifyDataSetChanged();
        }
        else
        {
            if (isMediaFile(nextDir.getName()))
            {
                Intent intent = new Intent();
                intent.putExtra("media_path",nextDir.getAbsolutePath());
                setResult(Configs.CODE_LOCAL_SUCCESS,intent);
                finish();
            }
            else
            {
                CPATH = getNextPath(CPATH);
            }
        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            if (CPATH.equals(BASEPATH))
            {
                finish();
            }
            else
            {
                CPATH = getNextPath(CPATH);
                m_datas = getFileList(CPATH);
                m_adapter.setM_lists(m_datas);
                m_adapter.notifyDataSetChanged();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
