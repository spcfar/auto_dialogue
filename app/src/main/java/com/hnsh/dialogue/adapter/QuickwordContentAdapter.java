package com.hnsh.dialogue.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hnsh.dialogue.R;
import com.hnsh.dialogue.bean.QuickwordContentBean;

import java.util.List;

/**
 * @项目名： inspection
 * @包名： com.dosmono.dialogue.adapter
 * @文件名: CommonPhraseAdapter
 * @创建者: zer
 * @创建时间: 2018/6/28 10:51
 * @描述： TODO
 */
public class QuickwordContentAdapter extends RecyclerView.Adapter<QuickwordContentAdapter.QuickwordContentViewHolder> {

    private final Context mContext;
    private final List<QuickwordContentBean> mDatas;
    private OnItemClickCallback mCallback;

    public QuickwordContentAdapter(Context context, List<QuickwordContentBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @NonNull
    @Override
    public QuickwordContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.unf_item_qa_layout, parent, false);
        return new QuickwordContentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuickwordContentViewHolder holder, final int position) {
        QuickwordContentBean item = mDatas.get(position);
        holder.mTvContent.setText(item.getContent());

        holder.mTvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCallback) {
                    mCallback.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null != mDatas) {
            return mDatas.size();
        }
        return 0;
    }

    public class QuickwordContentViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvContent;

        public QuickwordContentViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tv_qa_item_content);
        }
    }

    public interface OnItemClickCallback {
        void onItemClick(int position);
    }

    public void setOnItemClickCallback(OnItemClickCallback callback) {
        this.mCallback = callback;
    }
}
