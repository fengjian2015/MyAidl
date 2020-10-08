package com.yese.myaidl;

import android.app.Application;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.yese.mutual.IRemoteServiceCallback;
import com.yese.mutual.MutualServiceManage;

/*
 *   author:jason
 *   date:2019/5/2112:08
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initService();
    }

    private void initService(){
        MutualServiceManage.getInstance().init(this);
        MutualServiceManage.getInstance().setIRemoteServiceCallback(new IRemoteServiceCallback.Stub() {
            @Override
            public void pullUpService(String content) throws RemoteException {

                Log.e("jason_aidl",getPackageName()+"收到："+content);
//                Intent intent = new Intent(MyApp.this, ChatSocketService.class);
//                startService(intent);
            }
        });
    }
}
