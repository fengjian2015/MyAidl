package com.yese.myaidl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;



public class ServiceActivity extends AppCompatActivity {
    private TextView tv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        initView();
//        MutualServiceUtil.getInstance().init(ServiceActivity.this);
    }


    private void initView() {
        tv_content=findViewById(R.id.tv_content);

        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                initService();
            }
        });

        findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MutualServiceUtil.getInstance().pullUpService("应用2拉起");
            }
        });

    }

    private void initService(){
//        MutualServiceUtil.getInstance().setIRemoteServiceCallback(new IRemoteServiceCallback.Stub() {
//            @Override
//            public void pullUpService(String content) {
//                tv_content.setText(content);
//                Log.e("jason_aidl","应用2收到pullUpService"+content);
////                Intent intent = new Intent(ServiceActivity.this, ChatSocketService.class);
////                startService(intent);
//            }
//        });
    }
}
