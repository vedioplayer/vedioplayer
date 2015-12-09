package com.xyf.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.xyf.model.VideoItemModel;
import com.xyf.utils.LoadImageUtils;
import com.xyf.video.R;

import java.util.List;

/**
 * Created by shxiayf on 2015/11/5.
 */
public class VideoListItemAdapter extends BaseAdapter {

    private List<VideoItemModel> m_listModel;
    private Context mContext;

    public VideoListItemAdapter(Context ctx,List<VideoItemModel> list)
    {
        this.mContext = ctx;
        this.m_listModel = list;
    }

    public void setM_listModel(List<VideoItemModel> m_listModel) {
        this.m_listModel = m_listModel;
    }

    class ViewHolder
    {
        RelativeLayout rl_item;
        ImageView video_img;
        TextView video_name;
        TextView video_path;

        RelativeLayout rl_add;
    }

    @Override
    public int getCount() {
        if (m_listModel == null)
        {
            return 0;
        }
        return m_listModel.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        if (m_listModel == null)
        {
            return null;
        }

        if (i == 0)
        {
            return null;
        }

        return m_listModel.get(i-1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder m_Holder = null;
        if (view == null)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_videolist,null);
            m_Holder = new ViewHolder();
            m_Holder.video_img = (ImageView) view.findViewById(R.id.item_videolist_img);
            m_Holder.video_name = (TextView) view.findViewById(R.id.item_videolist_name);
            m_Holder.video_path = (TextView) view.findViewById(R.id.item_videolist_path);
            m_Holder.rl_item = (RelativeLayout) view.findViewById(R.id.item_videolist_item);
            m_Holder.rl_add = (RelativeLayout) view.findViewById(R.id.item_videolist_add);

            view.setTag(m_Holder);
        }
        else
        {
            m_Holder = (ViewHolder) view.getTag();
        }

        if (i == 0)
        {
            m_Holder.rl_add.setVisibility(View.VISIBLE);
            m_Holder.rl_item.setVisibility(View.GONE);
        }
        else
        {
            m_Holder.rl_add.setVisibility(View.GONE);
            m_Holder.rl_item.setVisibility(View.VISIBLE);

            VideoItemModel currentModel = m_listModel.get(i-1);

            m_Holder.video_name.setText(currentModel.getName());
            m_Holder.video_path.setText(currentModel.getVideoPath());

            LoadImageUtils.getInstances().loadImage(m_Holder.video_img,currentModel.getImageUri());
        }

        return view;
    }
}
