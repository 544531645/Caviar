package com.vrexlab.caviar.utils;

/**
 * Created by a on 2016/3/29.
 */


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @Title: ToastUtils.java
 * @Package com.hf.FollowTheInternetFly.com.vrexlab.caviar.utils
 * @Description: (Toast帮助类 ， 多次点击只显示一次 ， 提供线程中执行方法 ， < span style = " font - family : Arial, Helvetica, sans - serif ; " > 在线程中可用 < / span > < span style = " font - family : Arial, Helvetica, sans - serif ; " >)</   span>
 */
public class ToastUtils {

    /**
     * 上下文.
     */
    private static Context mContext = null;
    private static String KEY = "TEXT";
    /**
     * 显示Toast.
     */
    private static final int SHOW_TOAST = 0;
    private static String oldMsg;
    private static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;
    /**
     * 主要Handler类，在线程中可用 what：0.提示文本信息
     */
    private static Handler baseHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    showToast(mContext, msg.getData().getString(KEY));
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 描述：核心判断显示方法
     *
     * @param context
     * @param text
     * @return void
     */
    private static void core(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (text.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_LONG) {
                    toast.show();
                }
            } else {
                oldMsg = text;
                toast.setText(text);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    /**
     * 描述：Toast提示文本.
     *
     * @param text 文本
     */
    public static void showToast(Context context, String text) {
        mContext = context;
        if (!TextUtils.isEmpty(text)) {
            core(context, text);
        }

    }

    /**
     * 描述：Toast提示文本.
     *
     * @param resId 文本的资源ID
     */
    public static void showToast(Context context, int resId) {
        mContext = context;
        if (context != null) {
            core(context, "" + context.getResources().getText(resId));
        }
    }

    /**
     * 描述：在线程中提示文本信息.
     *
     * @param resId 要提示的字符串资源ID，消息what值为0,
     */
    public static void showToastInThread(Context context, int resId) {
        mContext = context;
        Message msg = baseHandler.obtainMessage(SHOW_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(KEY, context.getResources().getString(resId));
        msg.setData(bundle);
        baseHandler.sendMessage(msg);
    }


    public static void showToastInThread(Context context, String text) {
        mContext = context;
        Message msg = baseHandler.obtainMessage(SHOW_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(KEY, text);
        msg.setData(bundle);
        baseHandler.sendMessage(msg);
    }

}

