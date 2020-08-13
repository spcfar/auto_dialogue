package com.hnsh.dialogue.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dosmono.logger.Logger;
import com.hnsh.dialogue.R;
import com.hnsh.dialogue.bean.ChatMessage;
import com.hnsh.dialogue.views.TailIconTextView;


import java.util.List;


/**
 * @author lingu
 * @create 2019/12/3 16:56
 * @Describe
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int chatLeft = 0;
    public static final int chatRight= 1;



    private Context mContext;
    private List<ChatMessage> list;

    private Rect mBounds;

    public ChatMessageAdapter(Context context, List<ChatMessage> list){
        this.mContext = context;
        this.list = list;
        mBounds =  new Rect(5, -22, 50, 23);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Logger.d("viewType："+viewType);
        RecyclerView.ViewHolder holder = null;
        if (viewType==chatLeft){
            View view =LayoutInflater.from(mContext).inflate(R.layout.adapter_chat_msg_left , null);
            holder = new LeftHolder(view);
        }else {
            View view =LayoutInflater.from(mContext).inflate(R.layout.adapter_chat_msg_right , null);
            holder= new RightHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ChatMessage chatMessage = list.get(position);
        if (chatMessage.msgType == chatLeft){
            LeftHolder leftHolder = (LeftHolder) viewHolder;
            leftHolder.tvSource.setText(chatMessage.sourceMsg);
            leftHolder.tvTranslate.setText(chatMessage.translateMsg);
            leftHolder.tvTranslate.setText(chatMessage.translateMsg, mContext.getResources()
                    .getDrawable(R.drawable.volume_blue_anim), mBounds);

            if(chatMessage.inPlay){
                leftHolder.tvTranslate.startImageAnim();
            }else {
                leftHolder.tvTranslate.stopImageAnim();
            }
            leftHolder.tvTranslate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onPlayListener!=null){
                        onPlayListener.onPlay(position);
                    }
                }
            });
        }else{
            RightHolder rightHolder = (RightHolder) viewHolder;
            rightHolder.tvSource.setText(chatMessage.sourceMsg);
            rightHolder.tvTranslate.setText(chatMessage.translateMsg);

            rightHolder.tvTranslate.setText(chatMessage.translateMsg, mContext.getResources()
                    .getDrawable(R.drawable.volume_white_anim), mBounds);


            if(chatMessage.inPlay){
                rightHolder.tvTranslate.startImageAnim();
            }else {
                rightHolder.tvTranslate.stopImageAnim();
            }
            rightHolder.tvTranslate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onPlayListener!=null){
                        onPlayListener.onPlay(position);
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return  list.get(position).msgType;
    }

    public class LeftHolder extends RecyclerView.ViewHolder{

        private TextView tvSource;
        private TailIconTextView tvTranslate;
        public LeftHolder(@NonNull View itemView) {
            super(itemView);
            tvSource = itemView.findViewById(R.id.tv_left_source);
            tvTranslate = itemView.findViewById(R.id.tv_left_translate);
        }
    }

    public class RightHolder extends  RecyclerView.ViewHolder{
        private TextView tvSource;
        private TailIconTextView tvTranslate;
        public RightHolder(@NonNull View itemView) {
            super(itemView);
            tvSource = itemView.findViewById(R.id.tv_right_source);
            tvTranslate = itemView.findViewById(R.id.tv_right_translate);
        }
    }
    
    private onPlayListener onPlayListener;
    
    public void setOnPlayListener(onPlayListener listener){
        this.onPlayListener =  listener;
    }


    public interface onPlayListener{
        void  onPlay(int position);
    }
}
