package com.yese.mutual;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/*
 *   author:fickle
 *   date:2020/10
 */
public class MutualUtil {
    public static final String TAG = "Mutual";


    /**
     * 获取未运行的应用
     * @param context
     * @return
     */
    public static String getUnRunPackageName(Context context,Map<String,Long> activatedPackage) {
        try {
            JSONArray jsonArray = new JSONArray(JsonUtil.getPackegNameJson(context));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String packageName = jsonObject.getString(JsonUtil.JSON_PACKAGE_NAME);
                if (isAllowRestart(packageName,activatedPackage) && RestartAppUtil.restartApp(context, packageName)) {
                    return packageName;
                }
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String packageName = jsonObject.getString(JsonUtil.JSON_PACKAGE_NAME);
                if (RestartAppUtil.restartApp(context, packageName)) {
                    return packageName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否允许启动对应应用
     *
     * @param packageName 包名
     * @return true 允许
     */
    public static boolean isAllowRestart(String packageName,Map<String,Long> activatedPackage) {
        if (!activatedPackage.containsKey(packageName)) return true;
        Log.d(TAG, "记录的已启动包：" + activatedPackage.size());
        for (Map.Entry<String, Long> entry : activatedPackage.entrySet()) {
            if (entry.getKey().equals(packageName) && !timeCompare(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 大于30秒
     *
     * @param time
     * @return true 大于
     */
    private static boolean timeCompare(long time) {
        long current = System.currentTimeMillis();
        if (current - time > 5*60 * 1000) {
            return true;
        } else {
            return false;
        }
    }
}
