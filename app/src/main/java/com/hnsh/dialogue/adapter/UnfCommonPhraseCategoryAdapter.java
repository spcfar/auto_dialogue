package com.hnsh.dialogue.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hnsh.dialogue.R;
import com.hnsh.dialogue.bean.QuickwordCategoryBean;

import java.util.List;

/**
 * @项目名： Translator
 * @包名： com.dosmono.settings.adapter
 * @文件名: SettingAdapter
 * @创建者: Administrator
 * @创建时间: 2018/3/15 015 11:05
 * @描述： TODO
 */

public class UnfCommonPhraseCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final List<QuickwordCategoryBean> mDatas;
    private OnItemClickListener mOnItemClickListener;

    private int selectColor = 0x30000000;

    public void setSelectColor(int color){
        this.selectColor = color;
    }



    public UnfCommonPhraseCategoryAdapter(Context context, List<QuickwordCategoryBean> datas){
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_unified_common_phrase_layout, parent, false);
        return new CategoryItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CategoryItemViewHolder viewHolder = (CategoryItemViewHolder) holder;
        QuickwordCategoryBean item = mDatas.get(position);
        viewHolder.mCategoryItemTextView.setText(item.getCategoryName());
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
        int pressColor;
        if(item.isSelected()){
//            pressColor = mContext.getResources().getColor(R.color.unf_phrase_category_press);
            pressColor = selectColor;
        }
        else{
//            pressColor = mContext.getResources().getColor(R.color.unf_phrase_category);
            pressColor = Color.TRANSPARENT;
        }
        viewHolder.rootView.setBackgroundColor(pressColor);
    }

    @Override
    public int getItemCount() {
        if (null != mDatas){
            return mDatas.size();
        }
        return 0;
    }

    private int selectedPosition = 0;
    public boolean select(int position){
       if(selectedPosition == position)
           return false;
       mDatas.get(selectedPosition).setSelected(false);
       mDatas.get(position).setSelected(true);
       notifyItemChanged(selectedPosition);
       notifyItemChanged(position);
       selectedPosition = position;
       return true;
    }

    public QuickwordCategoryBean getBean(int position){
        if(position < 0 || position >= mDatas.size()){
            throw new IllegalArgumentException("position 参数错误,超出数组范围,position = "+position);
        }
        if(mDatas != null && mDatas.size() > 0 ){
            return mDatas.get(position);
        }
        return null;
    }

    private class CategoryItemViewHolder extends RecyclerView.ViewHolder{

        private final TextView mCategoryItemTextView  ;
        private final View rootView ;
        private boolean isSelected = false;

        public CategoryItemViewHolder(View itemView) {
            super(itemView);
            mCategoryItemTextView = itemView.findViewById(R.id.tv_item_unf_common_phrase_name);
            rootView = itemView.findViewById(R.id.item_root_view);
        }


    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
