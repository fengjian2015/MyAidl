package com.yese.mutual;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.MainThread;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yese.mutual.MutualUtil.TAG;

/*
 *   author:fickle
 *   date:2020/10
 */
public class MutualServiceManage {
    private ServerManager serverManager;
    private Context mContext;
    public static MutualServiceManage instance = null;
    /**
     * 服务地址
     */
    public final String SERVICE_NAME = "com.yese.mutual.MutualService";
    /**
     * 标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
     */
    private boolean mBound = false;
    /**
     * 记录是否第一次启动
     */
    private boolean isFirst = true;
    /**
     * 待发送的消息
     */
    private List<String> sendList = new ArrayList<>();
    /**
     * 回调集合
     */
    private static List<IRemoteServiceCallback> callbackLists = new ArrayList<>();
    /**
     * 记录包名和启动时间
     */
    private Map<String, Long> activatedPackage = new HashMap();

    /**
     * 延迟时间
     */
    private final long DELAY_TIME = 5000;

    private Handler handler = new Handler(Looper.getMainLooper());

    //单例
    public static MutualServiceManage getInstance() {
        if (instance == null) {
            instance = new MutualServiceManage();
        }
        return instance;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        activatedPackage.put(context.getPackageName(), System.currentTimeMillis());
        bindService();
    }

    /**
     * 唤起服务
     */
    public void pullUpService(String content) {
        try {
            if (!mBound) {
                bindService();
                sendList.add(content);
                return;
            }
            serverManager.pullUpService(content, activatedPackage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mBound) {
            mContext.unbindService(mServiceConnection);
            mBound = false;
        }
    }

    public ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "链接成功:" + name.toString());
            serverManager = ServerManager.Stub.asInterface(service);
            mBound = true;
            activatedPackage.put(name.getPackageName(), System.currentTimeMillis());
            sendList.add(mContext.getPackageName() + "链接成功" + name.getPackageName());
            for (String content : sendList) {
                pullUpService(content);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "disconnected");
            activatedPackage.remove(name.getPackageName());
            mBound = false;
            pullUpService("重新链接" + name.getPackageName());
        }
    };


    public void setActivatedPackage(Map activatedPackage) {
        this.activatedPackage.putAll(activatedPackage);
    }

    public void setIRemoteServiceCallback(IRemoteServiceCallback callback) {
        if (callback == null || callbackLists.contains(callback)) return;
        callbackLists.add(callback);
    }

    public List<IRemoteServiceCallback> getCallbackLists() {
        return callbackLists;
    }

    private void bindService() {
        if (activatedPackage.size() <= 1) {
            handler.postDelayed(mutualRunnable, DELAY_TIME);
        } else {
            attemptToBindService();
        }
    }

    /**
     * 尝试与服务端建立连接
     */
    private void attemptToBindService() {
        String packageName = MutualUtil.getUnRunPackageName(mContext, activatedPackage);
        Log.d(TAG, "需要启动的包名：" + packageName);
        if (TextUtils.isEmpty(packageName)) return;

        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, SERVICE_NAME));
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    Runnable mutualRunnable = new Runnable() {
        @Override
        public void run() {
            attemptToBindService();
        }
    };
}
