package com.hnsh.dialogue.mvp.models;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.dosmono.logger.Logger;
import com.dosmono.universal.common.Error;
import com.hnsh.dialogue.bean.cbs.BaseRequest;
import com.hnsh.dialogue.bean.cbs.BindIdentity;
import com.hnsh.dialogue.bean.cbs.BindingInfo;
import com.hnsh.dialogue.bean.cbs.ConfirmBindingReq;
import com.hnsh.dialogue.bean.cbs.CreateBindingRequest;
import com.hnsh.dialogue.bean.cbs.CustomCommand;
import com.hnsh.dialogue.bean.cbs.GetBindingRequest;
import com.hnsh.dialogue.bean.cbs.GetCodeRequest;
import com.hnsh.dialogue.bean.cbs.GetCodeResponse;
import com.hnsh.dialogue.bean.cbs.GetIdentityRequest;
import com.hnsh.dialogue.bean.cbs.GetIdentityResponse;
import com.hnsh.dialogue.bean.cbs.IdentityInfo;
import com.hnsh.dialogue.bean.cbs.MessageContent;
import com.hnsh.dialogue.bean.cbs.SetLanguageReq;
import com.hnsh.dialogue.bean.cbs.SetParamsReq;
import com.hnsh.dialogue.bean.cbs.SynchInfo;
import com.hnsh.dialogue.bean.cbs.UnbindingRequest;
import com.hnsh.dialogue.bean.db.DialogueDbHelper;
import com.hnsh.dialogue.constants.TSRConstants;
import com.hnsh.dialogue.services.IMessageCallback;
import com.hnsh.dialogue.services.IMessageService;
import com.hnsh.dialogue.services.MessageService;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created by Unpar on 18/1/27.
 */

public abstract class MessageModel implements IMessageCallback, IMessageService {

    protected String TAG = getClass().getSimpleName();
    protected Context mContext;
    private MessageService mService;

    private boolean isServiceOk = false;
    private boolean isConnectOk = false;

    public MessageModel(Context context) {
        WeakReference<Context> weakReference = new WeakReference<>(context);
        mContext = weakReference.get();
        connect();
    }

    public void destroy() {
        delCallback(TAG);
        disconnect();
    }

    private void connect() {
        Intent intent = new Intent(mContext, MessageService.class);
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void disconnect() {
        mContext.unbindService(mConnection);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isServiceOk = true;
            try {
                //这是有可能出现 相同类型强转的异常 java.lang.ClassCastException，所以 曲线救国
                mService = ((MessageService.MessageBinder) service).getService();
                mService.addCallback(TAG, MessageModel.this);
                MessageModel.this.onServiceConnected();
                Logger.d(TAG + " onServiceConnected：" + service.getClass().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceOk = false;

            delCallback(TAG);
            mService = null;
            Logger.d(TAG + " onServiceDisconnected");
        }
    };

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 在服务绑定成功
     */
    protected void onServiceConnected() {

    }

    @Override
    public void onConnected() {
        isConnectOk = true;
    }

    @Override
    public void onDisconnected() {
        isConnectOk = false;
    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void addCallback(@NotNull String tag, @NotNull IMessageCallback callback) {
        if (mService != null) mService.addCallback(tag, callback);
    }


    @Override
    public void delCallback(@NotNull String tag) {
        if (mService != null) mService.delCallback(tag);
    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 发送确认绑定请求
     */
    @Override
    public int confirmBinding(@NotNull ConfirmBindingReq body) {
        return mService != null
                ? mService.confirmBinding(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 发送获取绑定信息
     */
    @Override
    public int getBinding(@NotNull GetBindingRequest body) {
        return mService != null
                ? mService.getBinding(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 发送获取配对码请求
     */
    @Override
    public int geDeviceCode(@NotNull GetCodeRequest body) {
        return mService != null
                ? mService.geDeviceCode(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 发送创建绑定信息请求
     */
    @Override
    public int createBinding(@NotNull CreateBindingRequest body) {
        return mService != null
                ? mService.createBinding(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 发送解除绑定请求
     */
    @Override
    public int unBinding(@NotNull UnbindingRequest body) {
        return mService != null
                ? mService.unBinding(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 发送获取身份信息
     */
    @Override
    public int getIdentity(@NotNull GetIdentityRequest body) {
        return mService != null
                ? mService.getIdentity(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 发送设置身份信息
     */
    @Override
    public int setIdentity(@NotNull IdentityInfo body) {
        return mService != null
                ? mService.setIdentity(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 发送聊天消息
     */
    @Override
    public int sendMessage(@NotNull MessageContent body) {
        return mService != null
                ? mService.sendMessage(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    @Override
    public int setSlaveLanguage(@NotNull SetLanguageReq body) {
        return mService != null
                ? mService.setSlaveLanguage(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    @Override
    public int getSlaveSynchInfo(@NotNull SynchInfo body) {
        return mService != null
                ? mService.getSlaveSynchInfo(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    @Override
    public int setSlaveParams(@NotNull SetParamsReq body) {
        return mService != null
                ? mService.setSlaveParams(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    @Override
    public int finishDialogue(@NotNull BaseRequest body) {
        return mService != null
                ? mService.finishDialogue(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    @Override
    public int sendCustomMessage(@NotNull CustomCommand body) {
        return mService != null
                ? mService.sendCustomMessage(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    @Override
    public void onSendCustomMessage(int state, int msgState, @Nullable CustomCommand body) {

    }

    @Override
    public void onReceCustomMessage(int state, @Nullable CustomCommand body) {

    }

    @Override
    public void onGetbindidentity(int state, @Nullable BindIdentity body) {

    }

    @Override
    public int getBindIdentity(@NotNull BaseRequest body) {
        return mService != null
                ? mService.getBindIdentity(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    /**
     * 海南移动从机设置语言
     */
    @Override
    public int setActiveLanguage(@NotNull IdentityInfo body) {
        return mService != null
                ? mService.setActiveLanguage(body)
                : Error.ERR_PUSH_UNBUNDLED;
    }

    @Override
    public void onSetSlaveLanguage(int state, int msgState, @Nullable SetLanguageReq body) {

    }

    @Override
    public void onSetSlaveParams(int state, int msgState, @Nullable SetParamsReq body) {

    }

    @Override
    public void onEndDialogue(int state, int msgState, @Nullable BaseRequest body) {

    }

    @Override
    public void onRecvSetLanguage(int msgState, @Nullable SetLanguageReq body) {

    }

    @Override
    public void onRecvSetParams(int msgState, @Nullable SetParamsReq body) {

    }

    @Override
    public void onRecvEndDialogue(int msgState, @Nullable BaseRequest body) {

    }

    @Override
    public void onGetSynchInfo(@NotNull SynchInfo info) {

    }


    @Override
    public void onSendMessage(@NotNull MessageContent message) {
        message.setMsgType(TSRConstants.DIALOGUE_MSG_TYPE_ONESELF);
        long key = DialogueDbHelper.INSTANCE().insert(message);
        message.setId(key);
    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 响应解除绑定信息
     */
    @Override
    public void onUnBinding(int state) {
        if (state == Error.ERR_NONE) {
            DialogueDbHelper.INSTANCE().deleteAll();
        }
    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 响应设置身份信息
     */
    @Override
    public void onSetIdentity(int state, GetIdentityResponse body) {
    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 接收对方发送消息
     */
    @Override
    public void onRecvMessage(int state, @Nullable MessageContent response) {

    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 服务器推送解绑信息
     */
    @Override
    public void onPushUnbind(int state) {
        if (state == Error.ERR_NONE) {
            DialogueDbHelper.INSTANCE().deleteAll();
        }
    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 服务器推送绑定信息
     */
    @Override
    public void onPushBinding(int state, @Nullable BindingInfo response) {

    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 响应获取身份信息
     */
    @Override
    public void onGetIdentity(int state, @Nullable IdentityInfo body) {

    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 响应获取到配对码
     */
    @Override
    public void onGetDeviceCode(int state, @Nullable GetCodeResponse response) {

    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 响应获取绑定
     */
    @Override
    public void onGetBinding(int state, @Nullable BindingInfo response) {

    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 响应创建绑定
     */
    @Override
    public void onCreateBinding(int state, @Nullable BindingInfo body) {

    }

    /**
     * Create by <YANG TAO> on <18/3/1>
     * 响应确认绑定
     */
    @Override
    public void onConfirmBinding(int state, @Nullable BindingInfo body) {

    }



    /**
     * Create by <YANG TAO> on <18/3/1>
     * 接收推送角色冲突消息
     */
    @Override
    public void onCustomer() {

    }

    /**
     * 海南边检从机设置语言推送消息
     */
    @Override
    public void onReceActiveLanguage() {

    }

    public boolean isServiceOk() {
        return isServiceOk;
    }

    public boolean isConnectOk() {
        return isConnectOk;
    }

}
