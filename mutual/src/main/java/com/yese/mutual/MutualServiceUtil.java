package com.yese.mutual;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

/*
 *   author:jason
 *   date:2019/5/2014:28
 */
public class MutualServiceUtil {
    private ServerManager serverManager;
    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;
    private IRemoteServiceCallback serviceCallback;
    private Context mContext;

    public static MutualServiceUtil instance = null;

    //单例
    public static MutualServiceUtil getInstance() {
        if (instance == null) {
            instance = new MutualServiceUtil();
        }
        return instance;
    }

    public void init(Context context){
        mContext=context.getApplicationContext();
        attemptToBindService();
    }

    /**
     * 唤起服务
     */
    public void pullUpService(String content) {
        try {
            if (!mBound) {
                attemptToBindService();
                return;
            }
            if (serverManager == null) return;
            serverManager.pullUpService(content);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试与服务端建立连接
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.yese.mutual.MutualService");
        intent.setPackage("com.example.jason.webaidl");//对应的包名
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void stop() {
        if (mBound) {
            mContext.unbindService(mServiceConnection);
            mBound = false;
        }

    }

    public void setIRemoteServiceCallback(IRemoteServiceCallback callback){
        serviceCallback=callback;
        if (!mBound) {
            attemptToBindService();
            return;
        }
        try {
            serverManager.registerCallback(serviceCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("jason_adil", "客户端链接成功:" + name.toString());
            serverManager = ServerManager.Stub.asInterface(service);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("jason_adil", "客户端disconnected");
            try {
                serverManager.unregisterCallback(serviceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBound = false;
        }
    };

}
