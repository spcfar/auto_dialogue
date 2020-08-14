package com.hnsh.dialogue.bean.cbs;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

/**
 * Created by Administrator on 2018/2/2 0002.
 */
@Entity
public class MessageContent extends BaseRequest {
    @Id(autoincrement = true)
    private Long id;
    private String quid;
    private int session;
    private int state;

    private String portrait;
    private String nickName;
    private String content;

    @Unique
    private long sendTimeMs;
    private int langId;

    private int msgType;
    private boolean isPlaying;

    private String mid;//精准用语id
    private String targetText;

    private boolean isSynthesyed;//该条消息是否合成过

    @Transient
    private List<AnswerBean> answers;//回答精准用语

    @Generated(hash = 2132819651)
    public MessageContent(Long id, String quid, int session, int state,
                          String portrait, String nickName, String content, long sendTimeMs,
                          int langId, int msgType, boolean isPlaying, String mid,
                          String targetText, boolean isSynthesyed) {
        this.id = id;
        this.quid = quid;
        this.session = session;
        this.state = state;
        this.portrait = portrait;
        this.nickName = nickName;
        this.content = content;
        this.sendTimeMs = sendTimeMs;
        this.langId = langId;
        this.msgType = msgType;
        this.isPlaying = isPlaying;
        this.mid = mid;
        this.targetText = targetText;
        this.isSynthesyed = isSynthesyed;
    }

    @Generated(hash = 395736529)
    public MessageContent() {
    }

    public List<AnswerBean> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerBean> answers) {
        this.answers = answers;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTargetText() {
        return targetText;
    }

    public void setTargetText(String targetText) {
        this.targetText = targetText;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getLangId() {
        return langId;
    }

    public void setLangId(int langId) {
        this.langId = langId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public long getSendTimeMs() {
        return sendTimeMs;
    }

    public void setSendTimeMs(long sendTimeMs) {
        this.sendTimeMs = sendTimeMs;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsPlaying() {
        return this.isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean getIsSynthesyed() {
        return this.isSynthesyed;
    }

    public void setIsSynthesyed(boolean isSynthesyed) {
        this.isSynthesyed = isSynthesyed;
    }



    public String getQuid() {
        return this.quid;
    }

    public void setQuid(String quid) {
        this.quid = quid;
    }


    @Override
    public String toString() {
        return "MessageContent{" +
                "id=" + id +
                ", quid='" + quid + '\'' +
                ", session=" + session +
                ", state=" + state +
                ", portrait='" + portrait + '\'' +
                ", nickName='" + nickName + '\'' +
                ", content='" + content + '\'' +
                ", sendTimeMs=" + sendTimeMs +
                ", langId=" + langId +
                ", msgType=" + msgType +
                ", isPlaying=" + isPlaying +
                ", mid='" + mid + '\'' +
                ", targetText='" + targetText + '\'' +
                ", isSynthesyed=" + isSynthesyed +
                ", answers=" + answers +
                '}';
    }
}
