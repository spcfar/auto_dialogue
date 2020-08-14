package com.hnsh.dialogue.services

import com.dosmono.universal.entity.push.Packet
import com.dosmono.universal.push.IMPush
import com.dosmono.universal.push.IPushCallback

/**
 * Created by Unpar on 18/1/22.
 */
abstract class SimplePushCallback : IPushCallback {
    private var isConnected = false

    override fun onBind(success: Boolean, userId: String?) {
        isConnected = success
        onConnectChanged(isConnected)
    }

    override fun onConnected() {
        isConnected = false
        onConnectChanged(isConnected)
    }

    override fun onDestroy() {
        isConnected = false
        onConnectChanged(isConnected)
    }

    override fun onDisconnected() {
        isConnected = false
        onConnectChanged(isConnected)
    }

    override fun onKickUser(deviceId: String?, userId: String?) {

    }

    override fun onReceivePush(mpush: IMPush, sessionId: Int, packet: Packet<*>?, extData: ByteArray?) {

    }

    override fun onUnbind(success: Boolean, userId: String?) {
        isConnected = false
        onConnectChanged(isConnected)
    }

    abstract fun onConnectChanged(connect: Boolean)
}