package com.yese.mutual;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/*
 *   author:jason
 *   date:2019/5/2014:16
 */
public class MutualService extends Service {
    private List<IRemoteServiceCallback> callbackList=new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("jason_adil","服务端onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serverManager;
    }

    private ServerManager.Stub serverManager=new ServerManager.Stub() {
        @Override
        public void pullUpService(String content){
            Log.e("jason_adil","收到启动服务"+content);
            pullUpServiceCallback(content);
        }

        @Override
        public void registerCallback(IRemoteServiceCallback cb){
            if(cb==null||callbackList.contains(cb))return;
            callbackList.add(cb);
        }

        @Override
        public void unregisterCallback(IRemoteServiceCallback cb){
            if(cb==null||!callbackList.contains(cb))return;
            callbackList.remove(cb);
        }
    };

    private void pullUpServiceCallback(String content){
        for (IRemoteServiceCallback iRemoteServiceCallback:callbackList){
            try {
                iRemoteServiceCallback.pullUpService(content);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
