package com.hnsh.dialogue.services

import com.hnsh.dialogue.bean.cbs.*


/**
 * Created by Unpar on 18/1/22.
 */
interface IMessageCallback {
    fun onConnected()
    fun onDisconnected()
    fun onError(error: Int)

    fun onGetBinding(state: Int, response: BindingInfo?)
    fun onGetDeviceCode(state: Int, response: GetCodeResponse?)
    fun onCreateBinding(state: Int, body: BindingInfo?)
    fun onConfirmBinding(state: Int, body: BindingInfo?)
    fun onUnBinding(state: Int)
    fun onGetIdentity(state: Int, body: IdentityInfo?)
    fun onSetIdentity(state: Int,body: GetIdentityResponse?)
    fun onRecvMessage(state: Int, response: MessageContent?)
    fun onPushBinding(state: Int, response: BindingInfo?)
    fun onPushUnbind(state: Int)
    fun onCustomer()

    /**
    * Create by <YANG TAO> on <2018/7/6>
    * 边检项目增加
    */
    fun onSetSlaveLanguage(state: Int, msgState: Int, body: SetLanguageReq?)
    fun onGetSynchInfo(info: SynchInfo)
    fun onSetSlaveParams(state: Int, msgState: Int, body: SetParamsReq?)
    fun onEndDialogue(state: Int, msgState: Int, body: BaseRequest?)
    fun onSendMessage(message: MessageContent?)

    fun onRecvSetLanguage(msgState: Int, body: SetLanguageReq?)
    fun onRecvSetParams(msgState: Int, body: SetParamsReq?)
    fun onRecvEndDialogue(msgState: Int, body: BaseRequest?)

    fun onGetbindidentity(state: Int, body: BindIdentity?)

    /**海南移动增加*/
    fun onReceActiveLanguage()

    /**海南边检增加自定义同步消息*/
    fun onSendCustomMessage(state: Int, msgState: Int, body: CustomCommand?)
    fun onReceCustomMessage(state: Int, body: CustomCommand?)
}