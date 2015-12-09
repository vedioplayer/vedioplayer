package com.xyf.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.xyf.database.DBUtils;
import com.xyf.model.VideoItemModel;
import com.xyf.utils.Configs;
import com.xyf.utils.LogUtils;
import com.xyf.video.adapter.VideoListItemAdapter;

import java.io.File;
import java.util.List;

public class VideoListActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private static final String TAG = VideoListActivity.class.getSimpleName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initViews();
        initDatas();
    }

    private ListView m_list;
    private TextView m_empty;

    private void initViews() {

        m_list = (ListView) findViewById(R.id.main_list);
        m_empty = (TextView) findViewById(R.id.main_empty);

        m_list.setOnItemClickListener(this);
        m_list.setOnItemLongClickListener(this);
        m_empty.setOnClickListener(this);
    }

    private List<VideoItemModel> m_data;
    private VideoListItemAdapter m_adapter;

    private void initDatas()
    {
        DBUtils.getInstances().initDB(this);
        m_data = DBUtils.getInstances().queryDB(this);
        m_adapter = new VideoListItemAdapter(this,m_data);
        m_list.setAdapter(m_adapter);

        if (m_data.size() <= 0)
        {
            m_list.setVisibility(View.GONE);
            m_empty.setVisibility(View.VISIBLE);
        }
        else
        {
            m_list.setVisibility(View.VISIBLE);
            m_empty.setVisibility(View.GONE);

        }


    }


    @Override
    public void onClick(View view) {
        if (view == m_empty)
        {
            Intent intent = new Intent(this,VideoAddAcitivity.class);
            startActivityForResult(intent,Configs.CODE_ADD_VEDIO);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (i == 0)
        {
            Intent intent = new Intent(this,VideoAddAcitivity.class);
            startActivityForResult(intent,Configs.CODE_ADD_VEDIO);
        }
        else
        {
            VideoItemModel currentModel = (VideoItemModel)(m_list.getAdapter()).getItem(i);

            if (currentModel == null)
            {
                LogUtils.e(TAG,"model is empty");
                return;
            }

            if (currentModel.getVideoPath().startsWith("http"))
            {
                Intent intent = new Intent(this,VedioPlayActivity.class);
                intent.putExtra("title",currentModel.getName());
                intent.putExtra("path",currentModel.getVideoPath());
                startActivity(intent);
            }
            else
            {
                File dstFile = new File(currentModel.getVideoPath());

                if (dstFile.exists())
                {
                    Intent intent = new Intent(this,VedioPlayActivity.class);
                    intent.putExtra("title",currentModel.getName());
                    intent.putExtra("path",currentModel.getVideoPath());
                    startActivity(intent);
                }
                else
                {
                    deleteDialog("this file is not exits,do you want to delete this item?",currentModel);
                }
            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Configs.CODE_ADD_VEDIO)
        {
            if (resultCode == Configs.CODE_ADD_SUCCESS)
            {
                m_data = DBUtils.getInstances().queryDB(this);

                m_adapter.setM_listModel(m_data);
                m_adapter.notifyDataSetChanged();
            }
        }

    }

    private void deleteDialog(String message,final VideoItemModel model)
    {
        new AlertDialog.Builder(this)
                .setTitle("notification")
                .setMessage(message)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k) {
                        int result = DBUtils.getInstances().delete(VideoListActivity.this,model);

                        if (result > 0)
                        {
                            Toast.makeText(VideoListActivity.this,"delete success!",Toast.LENGTH_SHORT).show();

                            m_data.remove(model);
                            ((VideoListItemAdapter)m_list.getAdapter()).setM_listModel(m_data);
                            ((VideoListItemAdapter)m_list.getAdapter()).notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0)
        {
            deleteDialog("you will delete it?", (VideoItemModel) m_list.getAdapter().getItem(i));
        }


        return true;
    }
}
