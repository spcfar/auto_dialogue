package com.hnsh.dialogue.services

import com.hnsh.dialogue.bean.cbs.*


/**
 * Created by Unpar on 18/1/22.
 */
interface IMessageService {
    fun addCallback(tag: String, callback: IMessageCallback)
    fun delCallback(tag: String)

    fun getBinding(body: GetBindingRequest): Int
    fun geDeviceCode(body: GetCodeRequest): Int
    fun createBinding(body: CreateBindingRequest): Int
    fun confirmBinding(body: ConfirmBindingReq): Int
    fun unBinding(body: UnbindingRequest): Int
    fun getIdentity(body: GetIdentityRequest): Int
    fun setIdentity(body: IdentityInfo): Int
    fun sendMessage(body: MessageContent): Int

    /**
    * Create by <YANG TAO> on <2018/7/6>
    * 边检项目增加
    */
    fun setSlaveLanguage(body: SetLanguageReq): Int
    fun getSlaveSynchInfo(body: SynchInfo): Int
    fun setSlaveParams(body: SetParamsReq): Int
    fun finishDialogue(body: BaseRequest): Int
    fun getBindIdentity(body: BaseRequest): Int

    /**海南移动定制增加*/
    fun setActiveLanguage(body: IdentityInfo): Int

    /**海南边检增加自定义同步消息*/
    fun sendCustomMessage(body: CustomCommand): Int
}