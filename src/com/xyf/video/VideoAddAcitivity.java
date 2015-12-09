package com.xyf.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.xyf.database.DBUtils;
import com.xyf.model.VideoItemModel;
import com.xyf.utils.Configs;

import java.io.File;

/**
 * Created by shxiayf on 2015/11/9.
 */
public class VideoAddAcitivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vedioadd);

        initViews();
        initDatas();
    }

    private void initDatas() {
    }

    private EditText m_Username;
    private EditText m_Path;
    private EditText m_ImageView;
    private Button m_add;
    private Button m_addlocal;
    private void initViews() {

        m_Username = (EditText) findViewById(R.id.vedioadd_username);
        m_Path = (EditText) findViewById(R.id.vedioadd_path);
        m_ImageView = (EditText) findViewById(R.id.vedioadd_imgpath);
        m_add = (Button) findViewById(R.id.vedioadd_add);
        m_addlocal = (Button) findViewById(R.id.vedioadd_addlocal);

        m_add.setOnClickListener(this);
        m_addlocal.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.vedioadd_add)
        {
            String name = m_Username.getText().toString().trim();
            String path = m_Path.getText().toString().trim();
            String imagepath = m_Path.getText().toString().trim();

            if (TextUtils.isEmpty(name))
            {
                return;
            }

            if (TextUtils.isEmpty(path))
            {
                return;
            }

            if (TextUtils.isEmpty(imagepath))
            {
                imagepath = "";
            }


            DBUtils.getInstances().initDB(this);

            VideoItemModel model = new VideoItemModel();
            model.setName(name);
            model.setVideoPath(path);
            model.setImageUri(imagepath);

            long result = DBUtils.getInstances().insert(this,model);

            if (result > 0)
            {
                setResult(Configs.CODE_ADD_SUCCESS);
                finish();
            }
            else
            {
                Toast.makeText(this,"name or path error!",Toast.LENGTH_SHORT).show();
            }
        }
        else if (R.id.vedioadd_addlocal == view.getId())
        {
            Intent intent = new Intent(this,FileExplorerActivity.class);
            startActivityForResult(intent,Configs.CODE_LOCAL_VEDIO);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Configs.CODE_LOCAL_VEDIO == requestCode)
        {
            if (Configs.CODE_LOCAL_SUCCESS == resultCode)
            {
                if (data != null)
                {
                    String path = data.getStringExtra("media_path");
                    if (TextUtils.isEmpty(path))
                    {
                        return;
                    }

                    File dstfile = new File(path);
                    if (dstfile.exists())
                    {
                        m_Path.setText(dstfile.getAbsolutePath());
                        m_Username.setText(dstfile.getName());
                    }


                }
            }
        }
    }
}
