package com.yese.myaidl;

import android.app.Application;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.yese.mutual.IRemoteServiceCallback;
import com.yese.mutual.MutualServiceUtil;

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
        MutualServiceUtil.getInstance().init(this);
        MutualServiceUtil.getInstance().setIRemoteServiceCallback(new IRemoteServiceCallback.Stub() {
            @Override
            public void pullUpService(String content) throws RemoteException {

                Log.e("jason_aidl","应用2收到pullUpService"+MyApp.this.getPackageName());
                Intent intent = new Intent(MyApp.this, ChatSocketService.class);
                startService(intent);
            }
        });
    }
}
