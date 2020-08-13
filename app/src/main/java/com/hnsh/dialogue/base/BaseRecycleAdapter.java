package com.hnsh.dialogue.base;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.hnsh.dialogue.utils.NoDoubleClickUtils;
import com.hnsh.dialogue.views.MyItemClickListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 封装 RecycleAdapter
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleAdapter.BaseViewHolder> {


    protected List<T> datas;

    private View mConvertView;
    public BaseRecycleAdapter(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mConvertView= LayoutInflater.from(parent.getContext()).inflate(getLayoutId(),parent,false);
        return new BaseViewHolder(mConvertView);
    }



    @Override
    public void onBindViewHolder(com.hnsh.dialogue.base.BaseRecycleAdapter.BaseViewHolder holder, final int position) {

        bindData(holder,position);

    }


    /**
     * 刷新数据
     * @param datas
     */
    public void refresh(List<T> datas){
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public List<T> getDatas(){
        return this.datas;
    }

    /**
     * 清空数据
     */
    public void clearData(){
        this.datas.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     * @param datas
     */
    public void addData(List<T> datas){

        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     *  绑定数据
     * @param holder  具体的viewHolder
     * @param position  对应的索引
     */
    protected abstract void bindData(BaseViewHolder holder, int position);



    @Override
    public int getItemCount() {

        return datas==null?0:datas.size();
    }

    public T getItem(int position) {

        try {
            return datas.get(position);
        }catch (Exception e){
        }
        return null;
    }

    /**
     * 封装ViewHolder ,子类可以直接使用
     */
    public class BaseViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        private Map<Integer, View> mViewMap;
        private View itemMainView;
        public BaseViewHolder(View itemView) {
            super(itemView);
            this.itemMainView=itemView;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mViewMap = new HashMap<>();
        }

        /**
         * 获取设置的view
         * @return
         */
        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int viewId){
            View view = mViewMap.get(viewId);

            if(view == null){
                view = mConvertView.findViewById(viewId);
                mViewMap.put(viewId, view);
            }
            return (T)view;
        }

        public void  setClickView(View v){
            v.setOnClickListener(this);
        }

        /**
         * 设置文本属性
         * @param text
         */

        public void setText(int viewId, CharSequence text){
            TextView tv =  getView(viewId);
            tv.setText(text);
        }

        public void setImageResource(int viewId, int resId){
            ImageView iv = getView(viewId);
            iv.setImageResource(resId);
        }

        public void setImageBitmap(int viewId, Bitmap bitmap){
            ImageView iv =  getView(viewId);
            iv.setImageBitmap(bitmap);
        }

        public View getRootView(){
            return itemMainView;
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null){
                if(NoDoubleClickUtils.isFastClick()){
                    return;
                }
                itemClickListener.onItemClick(v,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(itemClickListener != null){
                if(NoDoubleClickUtils.isFastClick()){
                    return false;
                }
                itemClickListener.onItemLongClick(v,getPosition());
            }
            return false;
        }
    }

    /**
     * 获取子item
     * @return
     */
    public abstract int getLayoutId();



    /**
     * 设置文本属性
     * @param view
     * @param text
     */
    public void setItemText(View view,String text){
        if(view instanceof TextView){
            ((TextView) view).setText(text);
        }
    }



    private MyItemClickListener itemClickListener;
    public MyItemClickListener getItemClickListener(){
        return itemClickListener;
    }
    public void setOnItemClickListener(MyItemClickListener l){
        this.itemClickListener = l;
    }



}
