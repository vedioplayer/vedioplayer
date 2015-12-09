package com.xyf.video;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xyf.utils.LogUtils;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by shxiayf on 2015/11/5.
 */
public class VedioPlayActivity extends Activity implements MediaPlayer.OnCompletionListener,MediaPlayer.OnInfoListener{

    private static final String TAG = VedioPlayActivity.class.getSimpleName();

    private String m_path;
    private String m_title;
    private VideoView m_videoView;

    private MediaController m_mediaController;
    private GestureDetector m_Gesture;

    private int mVolumn = -1;
    private float mBrightress = -1f;

    private AudioManager m_AudioManager;

    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;

    private LinearLayout m_layout_volumn;
    private TextView m_volumn_number;
    private LinearLayout m_layout_brightress;
    private TextView m_brightress_number;

    private class MyGesture extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            LogUtils.i(TAG,"onDoubleTap");
            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
            else
                mLayout++;
            if (m_videoView != null)
                m_videoView.setVideoLayout(mLayout, 0);

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LogUtils.i(TAG,"onScroll");
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 4.0 / 5)// ”“±ﬂª¨∂Ø
                onVolumnChange((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 5.0)// ◊Û±ﬂª¨∂Ø
                onBrightressChange((mOldY - y) / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (!LibsChecker.checkVitamioLibs(this))
        {
            finish();
            return;
        }

        setContentView(R.layout.vedioplay);

        m_path = getIntent().getStringExtra("path");
        m_title = getIntent().getStringExtra("title");

        if (TextUtils.isEmpty(m_path))
        {
            finish();
            return;
        }

        m_videoView = (VideoView) findViewById(R.id.video_view);
        m_videoView.setOnCompletionListener(this);
        m_videoView.setOnInfoListener(this);

        m_layout_volumn = (LinearLayout) findViewById(R.id.vedio_volumn_layout);
        m_volumn_number = (TextView) findViewById(R.id.vedio_volumn_number);
        m_layout_brightress = (LinearLayout) findViewById(R.id.vedio_brightress_layout);
        m_brightress_number = (TextView) findViewById(R.id.vedio_brightress_number);

        if (m_path.startsWith("http:"))
        {
            m_videoView.setVideoURI(Uri.parse(m_path));
        }else
        {
            m_videoView.setVideoPath(m_path);
        }

        m_mediaController = new MediaController(this);
        m_mediaController.setFileName(m_title);
        m_videoView.setMediaController(m_mediaController);
        m_videoView.requestFocus();

        m_Gesture = new GestureDetector(this,new MyGesture());
        m_AudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);


        startPlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (m_videoView != null)
        {
            m_videoView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (m_videoView != null)
        {
            m_videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (m_videoView != null)
        {
            m_videoView.stopPlayback();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (m_Gesture.onTouchEvent(event))
        {
            return true;
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }

        return super.onTouchEvent(event);
    }

    private void onVolumnChange(float p)
    {
        LogUtils.i(TAG,"onVolumnChange:" + p);

        if (mVolumn == -1)
        {
            mVolumn = m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            if (mVolumn < 0)
            {
                mVolumn = 0;
            }
        }

        int mMaxValue = m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        int index = (int) (p * mMaxValue) + mVolumn;
        if (index > 100)
        {
            index = 100;
        }
        else if (index < 0)
        {
            index = 0;
        }

        m_AudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,index,0);

        m_layout_volumn.setVisibility(View.VISIBLE);
        m_volumn_number.setText(String.format("%d",(int)(index * 100 / mMaxValue)));
    }

    private void onBrightressChange(float p)
    {
        LogUtils.i(TAG,"onBrightressChange:" + p);

        if (mBrightress < 0)
        {
            mBrightress = getWindow().getAttributes().screenBrightness;
            if (mBrightress <= 0.00f)
            {
                mBrightress = 0.50f;
            }
            if (mBrightress < 0.01f)
            {
                mBrightress = 0.01f;
            }
        }

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = mBrightress + p;
        if (lp.screenBrightness > 1.0f)
        {
            lp.screenBrightness = 1.0f;
        }
        else if (lp.screenBrightness < 0.01f)
        {
            lp.screenBrightness = 0.01f;
        }
        getWindow().setAttributes(lp);


        m_layout_brightress.setVisibility(View.VISIBLE);
        m_brightress_number.setText(String.format("%d",(int)(lp.screenBrightness * 100 / 1)));
    }

    private void endGesture()
    {
        mVolumn = -1;
        mBrightress = -1f;

        mDissmissHandler.removeMessages(0);
        mDissmissHandler.sendEmptyMessageAtTime(0,500);
    }

    private Handler mDissmissHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            m_layout_volumn.setVisibility(View.GONE);
            m_layout_brightress.setVisibility(View.GONE);
        }
    };

    private void stopPlayer()
    {
        if (m_videoView != null)
        {
            m_videoView.pause();
        }
    }

    private void startPlayer()
    {
        if (m_videoView != null)
        {
            m_videoView.start();
        }
    }

    private boolean isPlaying()
    {
        return m_videoView != null && m_videoView.isPlaying();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        LogUtils.i(TAG,"onCompletion");
    }

    private boolean needResume = false;

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what)
        {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if(isPlaying())
                {
                    stopPlayer();
                    needResume = true;
                }

                break;

            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                if (needResume)
                {
                    startPlayer();
                    needResume = false;
                }

                break;

            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                LogUtils.i(TAG,"download:" + extra);

                break;

        }

        return true;
    }


}
