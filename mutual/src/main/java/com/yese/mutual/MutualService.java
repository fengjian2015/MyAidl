package com.yese.mutual;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Map;

import static com.yese.mutual.MutualUtil.TAG;



/*
 *   author:fickle
 *   date:2020/10
 */
public class MutualService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"服务启动"+getClass().getCanonicalName());
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serverManager;
    }

    private ServerManager.Stub serverManager=new ServerManager.Stub() {
        @Override
        public void pullUpService(String content,Map activatedPackage){
            pullUpServiceCallback(content);
            MutualServiceManage.getInstance().setActivatedPackage(activatedPackage);
        }
    };

    private void pullUpServiceCallback(String content){
        Log.d(TAG,"收到消息："+content +"   "+MutualServiceManage.getInstance().getCallbackLists().size());
        for (IRemoteServiceCallback iRemoteServiceCallback:MutualServiceManage.getInstance().getCallbackLists()){
            try {
                iRemoteServiceCallback.pullUpService(content);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
