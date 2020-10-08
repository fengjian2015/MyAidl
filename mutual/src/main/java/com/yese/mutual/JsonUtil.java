package com.yese.mutual;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/*
 *   author:fickle
 *   date:2020/10
 */
public class JsonUtil {
    public static final String JSON_PACKAGE_NAME="packageName";
    public static final String JSON_ASSETS_NAME="mutual.json";
    /**
     * 读取assets本地json
     * @param fileName
     * @param context
     * @return
     */
    public static String getPackegNameJson(Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(JSON_ASSETS_NAME)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
