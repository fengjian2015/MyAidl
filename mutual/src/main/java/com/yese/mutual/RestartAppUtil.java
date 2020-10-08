package com.yese.mutual;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/*
 *   author:fickle
 *   date:2020/10
 */
public class RestartAppUtil {

    private static final String TAG="Mutual";

    public static boolean restartApp(Context context, String packageName){
        if(isApkInstalled(context,packageName)&&!isAppRunning(context,packageName)){
            return true;
        }else{
            return false;
        }
    }


    //判断应用是否安装
    public static boolean isApkInstalled(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            Log.d(TAG,"isApkInstalled false");
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            Log.d(TAG,"isApkInstalled true");
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG,"isApkInstalled false");
            return false;
        }
    }

    private static boolean isAppRunning(Context context,String packageName){
        Log.d(TAG,"isAppRunning 检查："+packageName);
        if (packageName.equals(context.getPackageName())){
            Log.d(TAG,"isAppRunning  true");
            return true;
        }
        List<ActivityManager.RunningServiceInfo> list = new ArrayList<>();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        list = manager.getRunningServices(1000);
        for(int i=0;i<list.size();i++){
//            Log.d(TAG, "isAppRunning:" + list.get(i).service.getClassName() + " 包名" + list.get(i).service.getPackageName());
            if (MutualServiceManage.getInstance().SERVICE_NAME.equals(list.get(i).service.getClassName())
                    &&packageName.equals(list.get(i).service.getPackageName())) {
                Log.d(TAG,"isAppRunning  true");
                return true;
            }
        }
        Log.d(TAG,"isAppRunning  false");
        return false;
    }

}
