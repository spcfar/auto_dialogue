package com.hnsh.dialogue.utils;


import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/11/1.
 */

public class EventBusHelper {
    /**
     * 注册EventBus
     */
    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    /**
     * 取消注册EventBus
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * 发布订阅事件
     */
    public static void post(Object event) {
        if(event== null)return;
        EventBus.getDefault().post(event);
    }

    public static void post(String code, Object event){
        EventMessage message = new EventMessage(code,event);
        EventBus.getDefault().post(message);
    }


    /**
     * 发布粘性订阅事件
     */
    public static void postSticky(Object event) {
        EventBus.getDefault().postSticky(event);
    }

    /**
     * 移除指定的粘性订阅事件
     */
    public static <T> void removeStickyEvent(Class<T> eventType) {
        T stickyEvent = EventBus.getDefault().getStickyEvent(eventType);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }

    /**
     * 取消事件传送
     */
    public static void cancelEventDelivery(Object event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }

    /**
     * 移除所有的粘性订阅事件
     */
    public static void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }

}
