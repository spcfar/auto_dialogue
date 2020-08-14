package com.hnsh.dialogue.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.dosmono.logger.Logger;

import java.util.Stack;

/**
 * @author lingu
 * @create 2019/9/10 14:00
 * @Describe
 */
public class AppManager {
    private static Stack<Activity> m_stack_activity;
    private static AppManager m_instance;

    private Activity currentActivitiy;
    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (m_instance == null) {
            m_instance = new AppManager();
        }
        return m_instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (m_stack_activity == null) {
            m_stack_activity = new Stack<Activity>();
        }
        m_stack_activity.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if(m_stack_activity==null)return null;
        Activity activity = m_stack_activity.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if(m_stack_activity==null)return ;
        Activity activity = m_stack_activity.lastElement();
        finishActivity(activity);
    }



    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if(m_stack_activity==null)return ;
        try {
            if (activity != null) {
                m_stack_activity.remove(activity);
                activity.finish();
                activity = null;
            }
        }catch (Exception e){
            Logger.e(e,"e");
        }

    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if(m_stack_activity==null)return;
        for (Activity activity : m_stack_activity) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束指定类名的Activity以外的所有activity
     */
    public void finishXAllActivity(Class<?> cls) {
        Logger.d("[AppManager] "+m_stack_activity);
        printAllActivity();
        if(m_stack_activity==null || m_stack_activity.size()==0)return;

        try {
            Activity activity = null;
            for (int i = 0, size = m_stack_activity.size(); i < size; i++) {
                if( m_stack_activity.get(i)!=null){
                    if(  m_stack_activity.get(i).getClass().equals(cls)){
                        activity = m_stack_activity.get(i);
                    }else {
                        m_stack_activity.get(i).finish();
                    }

                }
            }
            m_stack_activity.clear();
            if(activity!=null){
                m_stack_activity.add(activity);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 查找activity 是否存在
     */
    public boolean findActivity(Class<?> cls) {
        if(m_stack_activity==null)return false;


        for (Activity activity : m_stack_activity) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if(m_stack_activity==null || m_stack_activity.size()==0)return;

        try {
            for (int i = 0, size = m_stack_activity.size(); i < size; i++) {
                if (null != m_stack_activity.get(i)) {
                    m_stack_activity.get(i).finish();
                }
            }
            m_stack_activity.clear();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void printAllActivity() {
        if(m_stack_activity==null || m_stack_activity.size()==0)return;

        try {
            for (int i = 0, size = m_stack_activity.size(); i < size; i++) {
                if (null != m_stack_activity.get(i)) {
                    Logger.d("[AppManager] printAllActivity:"+m_stack_activity.get(i).getComponentName().getClassName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Activity getCurrentActivitiy() {
        return currentActivitiy;
    }

    public void setCurrentActivitiy(Activity currentActivitiy) {
        this.currentActivitiy = currentActivitiy;
    }
}
