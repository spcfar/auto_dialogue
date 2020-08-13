package com.hnsh.dialogue.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dosmono.logger.Logger;
import com.hnsh.dialogue.R;
import com.hnsh.dialogue.base.BaseRecycleAdapter;
import com.hnsh.dialogue.bean.LanguageInfo;

import java.util.List;

/**
 * @author lingu
 * @create 2019/12/4 14:09
 * @Describe
 */
public class LanguageAdapter extends BaseRecycleAdapter<LanguageInfo> {


    private Context mContext;

    public LanguageAdapter(Context context, List<LanguageInfo> datas) {
        super(datas);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
//        return R.layout.adapter_language_list;
        return R.layout.adapter_flag_item;
    }


    @Override
    protected void bindData(BaseViewHolder holder, int position) {

//        holder.setText(R.id.tv_flag_name, datas.get(position).subname_zh);
        holder.setText(R.id.tv_flag_name, datas.get(position).name);
        Logger.d("语言名字name===" + datas.get(position).name);
        Logger.d("语言名字subname_zh===" + datas.get(position).subname_zh);
        ImageView ivFlag = holder.getView(R.id.iv_item_flag);

        Glide.with(mContext)
                .load(datas.get(position).flag)
                .into(ivFlag);
    }


}
