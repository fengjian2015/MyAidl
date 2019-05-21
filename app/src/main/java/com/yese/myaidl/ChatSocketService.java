package com.yese.myaidl;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 检测ChatSocket连接状态，断开则重连
 * Created by Darren on 2018/12/21.
 */
public class ChatSocketService extends IntentService {

    public ChatSocketService() {
        super("ChatSocketService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("jason_adil","启动应用2聊天服务");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        while (true) {
            try {
                Thread.sleep(8 * 1000);
                checkSocketState();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkSocketState() {
        Log.e("jason_adil","持续打印应用2 ");
    }

}
