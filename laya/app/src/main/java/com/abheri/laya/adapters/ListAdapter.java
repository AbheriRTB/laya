package com.abheri.laya.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abheri.laya.R;
import com.abheri.laya.models.AudioObject;

import java.util.ArrayList;

/**
 * Created by sahana on 16/7/17.
 */

public class ListAdapter extends BaseAdapter {

    private final int[] mPositionClicked;
    private Context mContext;
    private ArrayList<AudioObject> items = new ArrayList<>();

    public ListAdapter(Context context, ArrayList<AudioObject> items, int[] mPositionClicked) {
        this.mContext = context;
        this.items = items;
        this.mPositionClicked = mPositionClicked;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view = View.inflate(mContext, R.layout.list_view_item,null);
            holder = new ViewHolder();
            holder.tv = (TextView) view.findViewById(R.id.text1);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        if(i==mPositionClicked[0]){
            holder.tv.setBackgroundColor(ContextCompat.getColor(mContext,R.color.list_brown));
        }else{
            holder.tv.setBackgroundColor(ContextCompat.getColor(mContext,R.color.transparent));
        }


        holder.tv.setText(items.get(i).getAudioName());
        return view;
    }

    private class ViewHolder{
        TextView tv;
    }

}
