package com.hnsh.dialogue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hnsh.dialogue.R;
import com.hnsh.dialogue.bean.WifiEncriptEntity;

import java.util.List;

/**
 * @项目名： BIZ_Project
 * @包名： com.dosmono.common.adatper
 * @文件名: WifiEncriptionAdapter
 * @创建者: zer
 * @创建时间: 2019/3/11 10:29
 * @描述： TODO
 */
public class WifiEncriptionAdapter extends BaseAdapter {
    private Context mContext;
    private List<WifiEncriptEntity> mDatas;

    public WifiEncriptionAdapter(Context context, List<WifiEncriptEntity> datas){
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WifiEncryptionViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_wifi_encription_type_layout, null);
            holder = new WifiEncryptionViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (WifiEncryptionViewHolder) convertView.getTag();
        }

        WifiEncriptEntity entity = mDatas.get(position);
        if (entity != null){
            holder.mEncryptName.setText(entity.getName());
        }

        return convertView;
    }

    private class WifiEncryptionViewHolder{

        private final TextView mEncryptName;

        public WifiEncryptionViewHolder(View view){
            mEncryptName = view.findViewById(R.id.tv_encryption_name);
        }
    }
}
