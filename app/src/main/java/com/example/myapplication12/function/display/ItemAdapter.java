package com.example.myapplication12.function.display;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication12.R;

import java.util.List;

public class ItemAdapter extends BaseAdapter {

    public LayoutInflater mLayoutInflater;
    public List<Item> list;
    public Context mContext;
    public ItemAdapter(Context context, List<Item> list){
        this.mContext=context;
        this.list=list;
        this.mLayoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=mLayoutInflater.inflate(R.layout.query_lv_items,null);

            viewHolder.mTextView=(TextView) convertView.findViewById(R.id.lv_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //从list取出对象
        Item item=list.get(position);
        //设置item的内容

        viewHolder.mTextView.setText(item.getPhotoName());
        return convertView;
    }
    private static class ViewHolder{

        public TextView mTextView;
    }

}