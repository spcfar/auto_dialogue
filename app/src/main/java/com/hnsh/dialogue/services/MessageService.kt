package com.hnsh.dialogue.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.dosmono.logger.Logger
import com.dosmono.universal.common.Constant
import com.dosmono.universal.common.Error
import com.dosmono.universal.entity.http.BaseMsg
import com.dosmono.universal.entity.http.BaseReply
import com.dosmono.universal.entity.push.Packet
import com.dosmono.universal.gson.GsonFactory
import com.dosmono.universal.push.IMPush
import com.dosmono.universal.push.mpush.MPushHelper
import com.dosmono.universal.thread.TimeoutManager
import com.google.gson.reflect.TypeToken
import com.hnsh.dialogue.bean.cbs.*
import com.hnsh.dialogue.constants.TSRConstants
import com.hnsh.dialogue.mvp.models.PrefsModel
import com.hnsh.dialogue.mvp.models.PrefsSettingDefaultModel
import com.hnsh.dialogue.mvp.models.ReportModel
import com.hnsh.dialogue.utils.BindingInfoUtil
import com.hnsh.dialogue.utils.BroadcastsUtil
import com.hnsh.dialogue.utils.CommonUtil
import com.hnsh.dialogue.utils.PrefsUtils
import java.io.Serializable
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Unpar on 18/1/22.
 */
class MessageService : Service(), IMessageService {
    private val TAG = javaClass.simpleName
    private val SESSION_MIN_VALUE = 10000

    private var mPush: IMPush? = null
    private val mCallbacks = mutableMapOf<String, IMessageCallback>()

    private var isConnected = false

    private var timeoutManager: TimeoutManager<PushToken>? = null

    private var mReport: ReportModel? = null

//    private val messageArray = SparseArray<MessageContent>()
//    private val commandArray = SparseArray<String>()

    private val synchRecvArray = mutableMapOf<String, BaseRequest>()
    private val synchSendArray = mutableMapOf<Int, BaseRequest>()

    private val seesionId = AtomicInteger(SESSION_MIN_VALUE)
    private val messageId = AtomicInteger(0)

    private val synchManager = object : SynchManager() {

        override fun onRefresh(list: List<String>) {
            val body = SynchInfo()
            body.msgList = list
            body.deviceId = CommonUtil.getDeviceId()

            getSlaveSynchInfo(body)
        }

        override fun onTimeout(session: Int, query: String) {
            handlerTimeout(PushToken(query, session))
        }
    }

    inner class MessageBinder : Binder() {
        val service: MessageService
            get() = this@MessageService
    }

    override fun onBind(intent: Intent?): IBinder {
        return MessageBinder()
    }

    override fun onCreate() {
        super.onCreate()

        initPush()
        initTimeoutManager()
        initReport()
    }

    override fun onDestroy() {
        super.onDestroy()

        mCallbacks.clear()
    }

    override fun addCallback(tag: String, callback: IMessageCallback) {
        mCallbacks[tag] = callback
        if(isConnected) callback.onConnected()
    }

    override fun delCallback(tag: String) {
        mCallbacks.remove(tag)
    }

    private fun initReport() {
        mReport = ReportModel(this)
    }

    private fun initPush() {
        MPushHelper.addCallback(TAG, object : MPushHelper.ICallback {
            override fun onConnected(push: IMPush?) {
                mPush = push
                mPush?.addCallback(TAG, pushCallback)
            }

            override fun onDisconnected() {
                mPush?.delCallback(TAG)
                mPush = null
            }
        })
    }

    private fun initTimeoutManager() {
        timeoutManager = TimeoutManager(object : TimeoutManager.ICallback<PushToken> {
            override fun onTimeout(session: PushToken) {
                Logger.i("on timeout: $session")
                handlerTimeout(session)
            }
        })
    }

    private val pushCallback = object : SimplePushCallback() {

        override fun onReceivePush(mpush: IMPush, sessionId: Int, packet: Packet<*>?) {
            onReceivePacket(sessionId, packet)
        }

        override fun onConnectChanged(connect: Boolean) {
            if(isConnected != connect) {
                isConnected = connect
                for ((_, callback) in mCallbacks) {
                    when(isConnected) {
                        true -> callback.onConnected()
                        false -> callback.onDisconnected()
                    }
                }
            }
        }

        override fun onBind(success: Boolean, userId: String?) {
            super.onBind(success, userId)
            if(success) {
                mReport?.report()

                val request = GetBindingRequest(CommonUtil.getDeviceId())
                getBinding(request)
            }
        }

        override fun onAck(sessionId: Int) {

        }
    }

    private inline fun <reified T> genericType() = object: TypeToken<T>(){}.type

    private fun pushPacket(packet: Packet<*>, session: Int, checkTimeout: Boolean): Int {
        val json = GsonFactory.getGson().toJson(packet)
        Logger.d("request session: $session,  push : $json")
        var state = mPush?.sendPush(session, json.toByteArray(Constant.PUSH_ENCODE)) ?: Error.ERR_PUSH_UNBUNDLED
        Logger.d("request state: $state")
        when(state) {
            Error.ERR_NONE -> {
                if(checkTimeout) {
                    timeoutManager?.addTask(PushToken(packet.query, session), TSRConstants.PUSH_TIMEOUT)
                }
            }
            else -> onError(state)
        }
        return state
    }

    private fun <T> pushPacket(body: T?, query: String): Int {
        return pushPacket(body, query, genSeesionId())
    }

    private fun <T> pushPacket(body: T?, query: String, session: Int): Int {
        return pushPacket(body, query, session,true)
    }

    private fun <T> pushPacket(body: T?, query: String, session: Int, checkTimeout: Boolean): Int {
        val baseMsg = BaseMsg(body)
        val packet = Packet(Constant.PUSH_TYPE_IQ, query, null, null, "1.0.0", baseMsg)
        return pushPacket(packet, session, checkTimeout)
    }

    private fun <T> parserBody(body: Any?, type: Type): T? {
        var reply: T? = null
        if (body != null) {
            val json = JSON.toJSONString(body)
            Logger.i("receive push : $json")
            reply = GsonFactory.getGson().fromJson<T>(json, type)
        }
        return reply
    }

    private data class Pairs<T>(val state: Int, var reply: T? = null)

    private fun <T> handlerReply(state: Int, reply: BaseReply<T>?): Pairs<T> {
        var errorCode = state
        var response: T? = null
        when(state) {
            Error.ERR_NONE, Error.HTTP_SUCCESS -> {
                when(reply?.code) {
                    Error.ERR_NONE, Error.HTTP_SUCCESS -> {
                        response = reply.body
                    }else -> {
                        errorCode = reply?.code ?: -1
                    }
                }
            }
        }
        return Pairs(errorCode, response)
    }

    /***
     * 处理接收
     ***/
    private fun onReceivePacket(session: Int, packet: Packet<*>?) {

        when(packet?.msgName) {
            Constant.PUSH_TYPE_IQ -> {
                timeoutManager?.cancel(PushToken(packet.query, session))
                Logger.d("receiver session: $session, packet $packet")
                when(packet.query) {
                    TSRConstants.PUSH_SEND_MESSAGE -> {
                        val type = genericType<BaseReply<BaseRequest>>()
                        val reply = parserBody<BaseReply<BaseRequest>>(packet.body, type)
                        handlerSendMessage(Error.ERR_NONE, session, reply)
                    }
                    TSRConstants.PUSH_RECE_MESSAGE -> {
                        val body = packet.body
                        if(body != null) {
                            val type = genericType<BaseMsg<MessageContent>>()
                            val reply = parserBody<BaseMsg<MessageContent>>(packet.body, type)
                            val state = if(reply != null) Error.ERR_NONE else -1
                            handlerRecvMessage(state, session, reply?.body)
                        }
                    }
                    TSRConstants.PUSH_GET_SYCHINFO -> {
                        val type = genericType<BaseReply<SynchInfo>>()
                        val reply = parserBody<BaseReply<SynchInfo>>(packet.body, type)
                        handlerSynch(Error.ERR_NONE, reply)
                    }
                    TSRConstants.PUSH_SET_PARAMS -> {
                        val type = genericType<BaseReply<BaseRequest>>()
                        val reply = parserBody<BaseReply<BaseRequest>>(packet.body, type)
                        handlerSetParams(Error.ERR_NONE, session, reply)
                    }
                    TSRConstants.PUSH_RECV_SET_PARAMS -> {
                        val type = genericType<BaseReply<SetParamsReq>>()
                        val reply = parserBody<BaseReply<SetParamsReq>>(packet.body, type)
                        handlerRecvSetParams(Error.ERR_NONE, session, reply)
                    }
                    TSRConstants.PUSH_SET_LANGUAGE -> {
                        val type = genericType<BaseReply<BaseRequest>>()
                        val reply = parserBody<BaseReply<BaseRequest>>(packet.body, type)
                        handlerSetLanguage(Error.ERR_NONE, session, reply)
                    }
                    TSRConstants.PUSH_RECV_SET_LANGUAGE -> {
                        val type = genericType<BaseReply<SetLanguageReq>>()
                        val reply = parserBody<BaseReply<SetLanguageReq>>(packet.body, type)
                        handlerRecvSetLanguage(Error.ERR_NONE, session, reply)
                    }
                    TSRConstants.PUSH_END_DIALOGUE -> {
                        val type = genericType<BaseReply<BaseRequest>>()
                        val reply = parserBody<BaseReply<BaseRequest>>(packet.body, type)
                        handlerEndDialogue(Error.ERR_NONE, session, reply)
                    }
                    TSRConstants.PUSH_RECV_END_DIALOGUE -> {
                        val type = genericType<BaseReply<BaseRequest>>()
                        val reply = parserBody<BaseReply<BaseRequest>>(packet.body, type)
                        handlerRecvEndDialogue(Error.ERR_NONE, session, reply)
                    }
                    TSRConstants.PUSH_CONFIRM_BINDING -> {
                        val type = genericType<BaseReply<BindingInfo>>()
                        val reply = parserBody<BaseReply<BindingInfo>>(packet.body, type)

                        handlerConfirmBinding(Error.ERR_NONE, reply)
                    }
                    TSRConstants.PUSH_GET_CODE -> {
                        val type = genericType<BaseReply<GetCodeResponse>>()
                        val reply = parserBody<BaseReply<GetCodeResponse>>(packet.body, type)
                        handlerGetCode(Error.ERR_NONE, reply)
                    }
                    TSRConstants.PUSH_CREATE_BINDING -> {
                        val type = genericType<BaseReply<BindingInfo>>()
                        val reply = parserBody<BaseReply<BindingInfo>>(packet.body, type)
                        handlerCreateBinding(Error.ERR_NONE, reply)
                    }
                    TSRConstants.PUSH_GET_IDENTITY -> {
                        val type = genericType<BaseReply<IdentityInfo>>()
                        val reply = parserBody<BaseReply<IdentityInfo>>(packet.body, type)
                        handlerGetIdentity(Error.ERR_NONE, reply)
                    }
                    TSRConstants.PUSH_QUERY_BINDING -> {
                        val type = genericType<BaseReply<BindingInfo>>()
                        val reply = parserBody<BaseReply<BindingInfo>>(packet.body, type)
                        handlerGetBinding(Error.ERR_NONE, reply)
                    }
                    TSRConstants.PUSH_SET_IDENTITY -> {
                        val type = genericType<BaseReply<GetIdentityResponse>>()
                        val reply = parserBody<BaseReply<GetIdentityResponse>>(packet.body, type)
                        handlerSetIdentity(Error.ERR_NONE, reply)
                    }
                    TSRConstants.PUSH_UNBINDING -> {
                        val type = genericType<BaseReply<*>>()
                        val reply = parserBody<BaseReply<*>>(packet.body, type)
                        handlerUnBinding(Error.ERR_NONE, reply)
                    }
                    TSRConstants.PUSH_PUSH_UNBIND -> {
                        val type = genericType<BaseReply<*>>()
                        val reply = parserBody<BaseReply<*>>(packet.body, type)
                        handlerPushUnBinding(Error.ERR_NONE, reply)
                    }
                    TSRConstants.PUSH_PUSH_BINDING -> {
                        val type = genericType<BaseReply<BindingInfo>>()
                        val reply = parserBody<BaseReply<BindingInfo>>(packet.body, type)

                        handlerPushBinding(Error.ERR_NONE, reply)
                    }
                    TSRConstants.PUSH_CUSTOMER -> {
                        handerCustomer()
                    }
                    TSRConstants.PUSH_GET_BINDIDENTITY -> {
                        val type = genericType<BaseReply<BindIdentity>>()
                        val reply = parserBody<BaseReply<BindIdentity>>(packet.body, type)
                        handlerGetBindIdentity(Error.ERR_NONE, reply)
                    }
                    TSRConstants.PUSH_SET_ACTIVELANGUAGE -> {

                    }
                    TSRConstants.PUSH_RECV_ACTIVELANGUAGE -> {
                        handerActiveLanguage()
                    }
                    TSRConstants.PUSH_SEND_CUSTOM_MESSAGE -> {
                        val type = genericType<BaseReply<BaseRequest>>()
                        val reply = parserBody<BaseReply<BaseRequest>>(packet.body, type)
                        when(reply?.code?:-1){
                            Error.ERR_NONE,Error.HTTP_SUCCESS -> {
                                handleSendCustomMessage(Error.ERR_NONE, session, reply)
                            }
                            else -> {
                                if (reply?.body is CustomCommand){
                                    for((_, callback) in mCallbacks) {
                                        callback.onSendCustomMessage(reply.code?:-1, TSRConstants.MSG_STATE_FAILURE, reply.body as CustomCommand)
                                    }
                                }
                            }
                        }
                    }
                    TSRConstants.PUSH_RECV_CUSTOM_MESSAGE -> {

                        val type = genericType<BaseReply<CustomCommand>>()
                        val reply = parserBody<BaseReply<CustomCommand>>(packet.body, type)
                        handleRecvCustomMessage(Error.ERR_NONE, session, reply)
                    }
                }
            }
        }
    }

    private fun handerCustomer() {
        val usePattern = PrefsSettingDefaultModel.INSTANCE().usePattern
        if (usePattern == TSRConstants.USE_PATTERN_CLIENT.toInt()){
            PrefsSettingDefaultModel.INSTANCE().usePattern = 1
        }else if (usePattern == TSRConstants.USE_PATTERN_WORKER.toInt()){
            PrefsSettingDefaultModel.INSTANCE().usePattern = 0
        }
        PrefsModel.setSelectedLanguage(applicationContext,0)

        Logger.i("handler customer")
        for((_, callback) in mCallbacks) {
            callback.onCustomer()
        }
    }

    private fun handerActiveLanguage(){
        for((_, callback) in mCallbacks) {
            callback.onReceActiveLanguage()
        }
    }

    private fun handleSendCustomMessage(state: Int, session: Int, reply: BaseReply<BaseRequest>?){
        val pair = handlerStateReply(state, session, TSRConstants.PUSH_SEND_CUSTOM_MESSAGE, reply)
        for((_, callback) in mCallbacks) {
            callback.onSendCustomMessage(pair.state, pair.reply!!, null)
        }
    }

    private fun handleRecvCustomMessage(state: Int, session: Int, reply: BaseReply<CustomCommand>?){
        handlerRecvReply(state, session, TSRConstants.PUSH_RECV_CUSTOM_MESSAGE, reply)
    }

    /***
     * 处理超时
     ***/
    private fun handlerTimeout(token: PushToken) {
        val error = Error.ERR_REPLY_TIMEOUT
        Logger.d("handler timeout: $token")
        when(token.query) {
            TSRConstants.PUSH_SEND_MESSAGE -> {
//                Logger.i("send message timeout : ${token.session}")
                handlerSendMessage(error, token.session, null)
            }
            TSRConstants.PUSH_SET_PARAMS -> {
                handlerSetParams(error, token.session, null)
            }
            TSRConstants.PUSH_SET_LANGUAGE -> {
                handlerSetLanguage(error, token.session, null)
            }
            TSRConstants.PUSH_END_DIALOGUE -> {
                handlerEndDialogue(error, token.session, null)
            }
            TSRConstants.PUSH_CONFIRM_BINDING -> {
                handlerConfirmBinding(error, null)
            }
            TSRConstants.PUSH_GET_IDENTITY -> {
                handlerGetIdentity(error, null)
            }
            TSRConstants.PUSH_SET_IDENTITY -> {
                handlerSetIdentity(error, null)
            }
            TSRConstants.PUSH_QUERY_BINDING -> {
                handlerGetBinding(error, null)
            }
            TSRConstants.PUSH_UNBINDING -> {
                handlerUnBinding(error,null)
            }
            TSRConstants.PUSH_GET_CODE -> {
                handlerGetCode(error,null)
            }
            TSRConstants.PUSH_CREATE_BINDING -> {
                handlerCreateBinding(error,null)
            }
            TSRConstants.PUSH_GET_BINDIDENTITY -> {
                handlerGetBindIdentity(error, null)
            }
            TSRConstants.PUSH_SEND_CUSTOM_MESSAGE -> {
                val body = getAndRemoveSendArray(token.session)
                when(body){
                    is CustomCommand ->{
                        for((_, callback) in mCallbacks) {
                            callback.onSendCustomMessage(Error.ERR_REPLY_TIMEOUT, TSRConstants.MSG_STATE_FAILURE, body)
                        }
                    }
                }

            }
        }

    }

    private fun hasSynchTime(sendTime: Long, timeout: Long): Boolean {
        return if(sendTime > 0 && timeout > 0) {
            (System.currentTimeMillis() - sendTime) < timeout
        }else false
    }

    private fun handlerStateReply(state: Int, session: Int, query: String, reply: BaseReply<BaseRequest>?): Pairs<Int> {
        val pair = handlerReply(state, reply)
        val msgState = when(pair.state) {
            Error.ERR_NONE -> {
                var tState = TSRConstants.MSG_STATE_SENDING
                val msgId = pair.reply?.msgId
                if(!TextUtils.isEmpty(msgId)) {
//                    Logger.i("add session : $session")
                    val sendTime = reply?.body?.sendTime ?: -1
                    if(hasSynchTime(sendTime, reply?.body?.synchTimeout ?: -1)) {
                        synchManager.addTask(SynchTask(session, msgId, query, sendTime))
                    }else {
                        Logger.w("send synch message timeout, message : ${reply?.body}")
                        tState = TSRConstants.MSG_STATE_FAILURE
                    }
                }
                tState
            }
            else -> {
                synchSendArray.remove(session)
                TSRConstants.MSG_STATE_FAILURE
            }
        }
        return Pairs(pair.state, msgState)
    }

    private fun handlerSetLanguage(state: Int, session: Int, reply: BaseReply<BaseRequest>?) {
        val pair = handlerStateReply(state, session, TSRConstants.PUSH_SET_LANGUAGE, reply)
        for((_, callback) in mCallbacks) {
            callback.onSetSlaveLanguage(pair.state, pair.reply!!, null)
        }
    }

    private fun handlerSetParams(state: Int, session: Int, reply: BaseReply<BaseRequest>?) {
        val pair = handlerStateReply(state, session, TSRConstants.PUSH_SET_PARAMS, reply)
        for((_, callback) in mCallbacks) {
            callback.onSetSlaveParams(pair.state, pair.reply!!, null)
        }
    }

    private fun handlerEndDialogue(state: Int, session: Int, reply: BaseReply<BaseRequest>?) {
        val pair = handlerStateReply(state, session, TSRConstants.PUSH_END_DIALOGUE, reply)
        for((_, callback) in mCallbacks) {
            callback.onEndDialogue(pair.state, pair.reply!!, null)
        }
    }

    private fun handlerSendMessage(state: Int, session: Int, reply: BaseReply<BaseRequest>?){
        val body = synchSendArray[session]
        val pair = handlerStateReply(state, session, TSRConstants.PUSH_SEND_MESSAGE, reply)
        when(body) {
            is MessageContent -> {
                body.state = pair.reply!!
//                Logger.i("handler send message: $body")
                for((_, callback) in mCallbacks) {
                    callback.onSendMessage(body)
                }
            }
            else -> synchSendArray.remove(session)
        }
    }

    private fun handlerPushUnBinding(state: Int, reply: BaseReply<*>?) {
        val pairs = handlerReply(state, reply)
        if(state==Error.ERR_NONE){
            //解除绑定
            updatePrefsBindState(TSRConstants.BIND_STATE_UNBIND)
            //重置会话语言
            PrefsModel.setSelectedLanguage(applicationContext, -1)
        }
        for((_, callback) in mCallbacks) {
            callback.onPushUnbind(pairs.state)
        }
    }

    private fun handlerGetCode(state: Int, reply: BaseReply<GetCodeResponse>?) {
        val pairs = handlerReply(state, reply)
        for((_, callback) in mCallbacks) {
            callback.onGetDeviceCode(pairs.state, pairs.reply)
        }
    }

    private fun handlerCreateBinding(state: Int, reply: BaseReply<BindingInfo>?) {
        val pairs = handlerReply(state, reply)
        if(state==Error.ERR_NONE&&pairs.state==Error.ERR_NONE){
            updatePrefsBindState(TSRConstants.BIND_STATE_BINDED)
            BindingInfoUtil.setBindingInfo(applicationContext,pairs.reply)
        }
        for((_, callback) in mCallbacks) {
            callback.onCreateBinding(pairs.state, pairs.reply)
        }
    }

    private fun handlerGetBinding(state: Int, reply: BaseReply<BindingInfo>?) {
        val pairs = handlerReply(state, reply)
        Logger.i("handlerReplyGetBinding $pairs state = $state")
        when(state){
            Error.ERR_NONE -> {
                if (pairs.reply?.state==Error.ERR_NONE){
                    updatePrefsBindState(TSRConstants.BIND_STATE_BINDED)
                    BindingInfoUtil.setBindingInfo(applicationContext,pairs.reply)
                }else{
                    updatePrefsBindState(TSRConstants.BIND_STATE_UNBIND)
                }
            }
        }
        Logger.i("mCallbacks.length = "+mCallbacks.size)
        for((_, callback) in mCallbacks) {
            callback.onGetBinding(pairs.state, pairs.reply)
        }
    }

    private fun handlerUnBinding(state: Int, reply: BaseReply<*>?) {
        val pairs = handlerReply(state, reply)
        if(state==Error.ERR_NONE){
            //解除绑定
            updatePrefsBindState(TSRConstants.BIND_STATE_UNBIND)
            //重置会话语言
//            PrefsModel.setSelectedLanguage(applicationContext, -1)
        }
        for((_, callback) in mCallbacks) {
            callback.onUnBinding(pairs.state)
        }
    }

    private fun handlerGetIdentity(state: Int, reply: BaseReply<IdentityInfo>?) {
        val pairs = handlerReply(state, reply)
        if (null != pairs.reply){
            PrefsSettingDefaultModel.INSTANCE().usePattern = pairs.reply?.identity!!.toInt()
        }
        for((_, callback) in mCallbacks) {
            callback.onGetIdentity(pairs.state, pairs.reply)
        }
    }

    private fun handlerSetIdentity(state: Int, reply: BaseReply<GetIdentityResponse>?) {
        val pairs = handlerReply(state, reply)
        PrefsSettingDefaultModel.INSTANCE().usePattern = pairs.reply?.identity!!.toInt()
        for((_, callback) in mCallbacks) {
            callback.onSetIdentity(pairs.state, pairs.reply)
        }
    }

    private fun handlerConfirmBinding(state: Int, reply: BaseReply<BindingInfo>?) {
        val pairs = handlerReply(state, reply)
        if(state==Error.ERR_NONE || pairs.reply?.state==Error.ERR_NONE){
            updatePrefsBindState(TSRConstants.BIND_STATE_BINDED)
            BindingInfoUtil.setBindingInfo(applicationContext,pairs.reply)
        }
        for((_, callback) in mCallbacks) {
            callback.onConfirmBinding(pairs.state, pairs.reply)
        }
    }

    private fun handlerPushBinding(state: Int, reply: BaseReply<BindingInfo>?) {
        val pairs = handlerReply(state, reply)

        if(state==Error.ERR_NONE){
            //绑定成功
            updatePrefsBindState(TSRConstants.BIND_STATE_BINDED)
            BindingInfoUtil.setBindingInfo(applicationContext,pairs.reply)
        }

        for((_, callback) in mCallbacks) {
            callback.onPushBinding(pairs.state, pairs.reply)
        }
    }

    private fun updatePrefsBindState(state: Int){
        PrefsUtils.setPrefs(this, TSRConstants.BIND_RELATION_FLAG, state)
        Logger.d("bindstate: $state")
    }

    private fun <T : BaseRequest> handlerRecvReply(state: Int, session: Int, query: String, reply: BaseReply<T>?): Pairs<T> {
        val pairs = handlerReply(state, reply)
        when(pairs.state) {
            Error.ERR_NONE -> {
                val body = pairs.reply
                if(body != null) {
                    val sendTime = reply?.body?.sendTime ?: -1
                    if(hasSynchTime(sendTime, reply?.body?.synchTimeout ?: -1)) {
                        synchRecvArray[body.msgId] = body
                        synchManager.addTask(SynchTask(session, body.msgId, query, sendTime))
                        Logger.i("Synch receiver task count: ${synchRecvArray.size}, session : $session, add to task : $body")
                    }else {
                        Logger.w("recv synch message timeout, message : ${reply?.body}")
                    }
                }
            }
            else -> pairs.state
        }
        return pairs
    }

    private fun handlerGetBindIdentity(state: Int, reply: BaseReply<BindIdentity>?){
        val pairs = handlerReply(state, reply)
        PrefsSettingDefaultModel.INSTANCE().usePattern = pairs.reply?.identity!!.toInt()
        for((_, callback) in mCallbacks) {
            callback.onGetbindidentity(pairs.state, pairs.reply)
        }
    }

    private fun handlerRecvMessage(state: Int, session: Int, reply: MessageContent?) {
        handlerRecvReply(state, session, TSRConstants.PUSH_RECE_MESSAGE, BaseReply(state, body = reply))
    }

    private fun handlerRecvSetLanguage(state: Int, session: Int, reply: BaseReply<SetLanguageReq>?) {
        handlerRecvReply(state, session, TSRConstants.PUSH_RECV_SET_LANGUAGE, reply)
    }

    private fun handlerRecvSetParams(state: Int, session: Int, reply: BaseReply<SetParamsReq>?) {
        handlerRecvReply(state, session, TSRConstants.PUSH_RECV_SET_PARAMS, reply)
    }

    private fun handlerRecvEndDialogue(state: Int, session: Int, reply: BaseReply<BaseRequest>?) {
        handlerRecvReply(state, session, TSRConstants.PUSH_RECV_END_DIALOGUE, reply)
    }

    private fun getAndRemoveRecvArray(msgId: String): BaseRequest? {
        Logger.i("remove recv message : $msgId")
        val body = synchRecvArray[msgId]
        if(body != null) { synchRecvArray.remove(msgId) }
        return body
    }

    private fun getAndRemoveSendArray(session: Int): BaseRequest? {
        val body = synchSendArray[session]
        if(body != null) { synchSendArray.remove(session) }
        return body
    }

    private fun handlerSynch(state: Int, reply: BaseReply<SynchInfo>?) {
        val pairs = handlerReply(state, reply)
        Logger.i("reply synch : ${reply?.body}")
        when(pairs.state){
            Error.ERR_NONE -> {
                val msgList = reply?.body?.msgList
                when(msgList?.isNotEmpty()) {
                    true -> {
                        for(item in msgList) {
                            val task = synchManager.getAndRemoveTask(item)
                            Logger.i("synch recv success, session: ${task?.sessionId}, query : ${task?.query}")
                            when(task?.query) {
                                TSRConstants.PUSH_SEND_MESSAGE -> {
                                    val body = getAndRemoveSendArray(task.sessionId)
                                    when(body) {
                                        is MessageContent -> {
                                            body.state = TSRConstants.MSG_STATE_SUCCESS
                                            Logger.i("send message : $body")
                                            for((_, callback) in mCallbacks) {
                                                callback.onSendMessage(body)
                                            }
                                        }
                                    }
                                }
                                TSRConstants.PUSH_RECE_MESSAGE -> {
                                    val body = getAndRemoveRecvArray(task.msgId)
                                    when(body) {
                                        is MessageContent -> {
                                            body.state = TSRConstants.MSG_STATE_SUCCESS
                                            Logger.i("recv message : $body")
                                            Logger.i("mCallbacks.length = "+mCallbacks.size)
                                            for((_, callback) in mCallbacks) {
                                                Logger.i("callback = " +callback)
                                                callback.onRecvMessage(Error.ERR_NONE, body)
                                            }
                                        }
                                    }
                                }
                                TSRConstants.PUSH_SET_PARAMS -> {
                                    val body = getAndRemoveSendArray(task.sessionId)
                                    when(body) {
                                        is SetParamsReq -> {
                                            Logger.i("set params : $body")
                                            for((_, callback) in mCallbacks) {
                                                callback.onSetSlaveParams(Error.ERR_NONE, TSRConstants.MSG_STATE_SUCCESS, body)
                                            }
                                        }
                                    }
                                }
                                TSRConstants.PUSH_RECV_SET_PARAMS -> {
                                    val body = getAndRemoveRecvArray(task.msgId)
                                    when(body) {
                                        is SetParamsReq -> {
                                            Logger.i("recv params : $body")
                                            for((_, callback) in mCallbacks) {
                                                callback.onRecvSetParams(TSRConstants.MSG_STATE_SUCCESS, body)
                                            }
                                        }
                                    }
                                }
                                TSRConstants.PUSH_SET_LANGUAGE -> {
                                    val body = getAndRemoveSendArray(task.sessionId)
                                    when(body) {
                                        is SetLanguageReq -> {
                                            Logger.i("set language : $body")
                                            for((_, callback) in mCallbacks) {
                                                callback.onSetSlaveLanguage(Error.ERR_NONE, TSRConstants.MSG_STATE_SUCCESS, body)
                                            }
                                        }
                                    }
                                }
                                TSRConstants.PUSH_RECV_SET_LANGUAGE -> {
                                    val body = getAndRemoveRecvArray(task.msgId)
                                    when(body) {
                                        is SetLanguageReq -> {
                                            Logger.i("recv language : $body")

                                            Logger.i("[Info] send broadcast language change")
                                            val castIntent = Intent(BroadcastsUtil.EXTRA_ACTION_LAUNCHER_NOTIFY)
                                            castIntent.putExtra("use", 1)
                                            sendBroadcast(castIntent)

                                            PrefsModel.setSelectedLanguage(applicationContext, body.lang)
                                            for((_, callback) in mCallbacks) {
                                                callback.onRecvSetLanguage(TSRConstants.MSG_STATE_SUCCESS, body)
                                            }
                                        }
                                    }
                                }
                                TSRConstants.PUSH_END_DIALOGUE -> {
                                    val body = getAndRemoveSendArray(task.sessionId)
                                    when(body) {
                                        is BaseRequest -> {
                                            Logger.i("end dialogue : $body")
                                            for((_, callback) in mCallbacks) {
                                                callback.onEndDialogue(Error.ERR_NONE, TSRConstants.MSG_STATE_SUCCESS, body)
                                            }
                                        }
                                    }
                                }
                                TSRConstants.PUSH_RECV_END_DIALOGUE -> {
                                    val body = getAndRemoveRecvArray(task.msgId)
                                    when(body) {
                                        is BaseRequest -> {
                                            Logger.i("recv end dialogue : $body")
                                            for((_, callback) in mCallbacks) {
                                                callback.onRecvEndDialogue(TSRConstants.MSG_STATE_SUCCESS, body)
                                            }
                                        }
                                    }
                                }
                                TSRConstants.PUSH_SEND_CUSTOM_MESSAGE -> {
                                    val body = getAndRemoveSendArray(task.sessionId)
                                    when(body) {
                                        is CustomCommand -> {
                                            Logger.i("send custom message : $body")
                                            for((_, callback) in mCallbacks) {
                                                callback.onSendCustomMessage(Error.ERR_NONE, TSRConstants.MSG_STATE_SUCCESS, body)
                                            }
                                        }
                                        else -> {
                                            Logger.d("synch send custom message fail, body not CustomCommand : $body")
                                        }
                                    }
                                }
                                TSRConstants.PUSH_RECV_CUSTOM_MESSAGE -> {
                                    val body = getAndRemoveRecvArray(task.msgId)
                                    when(body) {
                                        is CustomCommand -> {
                                            Logger.i("recv custom message : $body")
                                            for((_, callback) in mCallbacks) {
                                                callback.onReceCustomMessage(Error.ERR_NONE, body)
                                            }
                                        }
                                        else -> {
                                            Logger.d("synch recv custom message fail, body not CustomCommand : $body")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onError(error: Int) {
        for ((_, callback) in mCallbacks) {
            callback.onError(error)
        }
    }

    override fun getBinding(body: GetBindingRequest): Int {
        return pushPacket(body, TSRConstants.PUSH_QUERY_BINDING)
    }

    override fun unBinding(body: UnbindingRequest): Int {
        return pushPacket(body, TSRConstants.PUSH_UNBINDING)
    }

    override fun geDeviceCode(body: GetCodeRequest): Int {
        return pushPacket(body, TSRConstants.PUSH_GET_CODE)
    }

    override fun createBinding(body: CreateBindingRequest): Int {
        return pushPacket(body, TSRConstants.PUSH_CREATE_BINDING)
    }

    override fun getIdentity(body: GetIdentityRequest): Int {
        return pushPacket(body, TSRConstants.PUSH_GET_IDENTITY)
    }

    override fun setIdentity(body: IdentityInfo): Int {
        return pushPacket(body, TSRConstants.PUSH_SET_IDENTITY)
    }

    override fun confirmBinding(body: ConfirmBindingReq): Int {
        return pushPacket(body, TSRConstants.PUSH_CONFIRM_BINDING)
    }

    override fun setActiveLanguage(body: IdentityInfo): Int {
        return pushPacket(body, TSRConstants.PUSH_SET_ACTIVELANGUAGE)
    }

    private fun <T : BaseRequest> sendSynchPacket(query: String, body: T): Pairs<Int> {
        val session = genSeesionId()
        body.sendTime = System.currentTimeMillis()
        body.synchTimeout = TSRConstants.PUSH_TIMEOUT.toLong()

        val state = pushPacket(body, query, session)
        val msgState = when(state) {
            Error.ERR_NONE -> {
//                body.sendTime = System.currentTimeMillis()
//                body.synchTimeout = TSRConstants.PUSH_TIMEOUT.toLong()
                synchSendArray[session] = body
                Logger.i("Synch send task count: ${synchSendArray.size}, session : $session, body : $body")
                TSRConstants.MSG_STATE_SENDING
            }
            else -> TSRConstants.MSG_STATE_FAILURE
        }
        return Pairs(state, msgState)
    }

    override fun sendMessage(body: MessageContent): Int {
        val pair = sendSynchPacket(TSRConstants.PUSH_SEND_MESSAGE, body)
        when(pair.reply) {
            TSRConstants.MSG_STATE_SENDING -> {
                body.session = genMessageId()
            }
        }
        body.state = pair.reply ?: TSRConstants.MSG_STATE_FAILURE
        for((_, callback) in mCallbacks) {
            callback.onSendMessage(body)
        }
        return pair.state
    }

    override fun setSlaveLanguage(body: SetLanguageReq): Int {
        val pair = sendSynchPacket(TSRConstants.PUSH_SET_LANGUAGE, body)
        for((_, callback) in mCallbacks) {
            callback.onSetSlaveLanguage(pair.state, pair.reply!!, body)
        }
        return pair.state
    }

    override fun setSlaveParams(body: SetParamsReq): Int {
        val pair = sendSynchPacket(TSRConstants.PUSH_SET_PARAMS, body)
        for((_, callback) in mCallbacks) {
            callback.onSetSlaveParams(pair.state, pair.reply!!, body)
        }
        return pair.state
    }

    override fun finishDialogue(body: BaseRequest): Int {
        val pair = sendSynchPacket(TSRConstants.PUSH_END_DIALOGUE, body)
        for((_, callback) in mCallbacks) {
            callback.onEndDialogue(pair.state, pair.reply!!, body)
        }
        return pair.state
    }

    override fun sendCustomMessage(body: CustomCommand): Int {
        val pair = sendSynchPacket(TSRConstants.PUSH_SEND_CUSTOM_MESSAGE, body)
        for((_, callback) in mCallbacks) {
            callback.onSendCustomMessage(pair.state, pair.reply!!, body)
        }
        return pair.state
    }

    override fun getSlaveSynchInfo(body: SynchInfo): Int {
        Logger.i("synch refresh : $body")
        return pushPacket(body, TSRConstants.PUSH_GET_SYCHINFO)
    }

    override fun getBindIdentity(body: BaseRequest): Int {
        return pushPacket(body, TSRConstants.PUSH_GET_BINDIDENTITY)
    }

    private fun genMessageId(): Int {
        return messageId.incrementAndGet()
    }

    private fun genSeesionId(): Int {
        var value = seesionId.incrementAndGet()
        if(value >= Int.MAX_VALUE) {
            value = SESSION_MIN_VALUE
            seesionId.set(value)
        }
        return value
    }
}
