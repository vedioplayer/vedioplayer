package com.xyf.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.xyf.video.R;

import java.util.List;

/**
 * Created by shxiayf on 2015/11/10.
 */
public class FileExplorerAdapter extends BaseAdapter {

    private Context mCtx;
    private List<String> m_lists;

    public FileExplorerAdapter(Context context,List<String> strs)
    {
        this.mCtx = context;
        this.m_lists = strs;
    }

    public void setM_lists(List<String> m_lists) {
        this.m_lists = m_lists;
    }

    class ViewHolder
    {
        TextView txt_name;
    }

    @Override
    public int getCount() {
        if (m_lists == null)
        {
            return 0;
        }
        return m_lists.size();
    }

    @Override
    public Object getItem(int i) {
        if (m_lists == null)
        {
            return null;
        }
        return m_lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder = null;
        if ( view == null)
        {
            mHolder = new ViewHolder();
            view = LayoutInflater.from(mCtx).inflate(R.layout.item_files,null);
            mHolder.txt_name = (TextView) view.findViewById(R.id.item_files_name);

            view.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) view.getTag();
        }

        mHolder.txt_name.setText(m_lists.get(i));

        return view;
    }
}
