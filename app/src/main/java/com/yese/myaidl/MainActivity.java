package com.yese.myaidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yese.webadil.Book;
import com.yese.webadil.BookManager;
import com.yese.webadil.IRemoteServiceCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button bt1;
    private EditText et_content;
    private TextView tv_content;
    private BookManager mBookManager;
    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;

    //包含Book对象的list
    private List<Book> mBooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        bt1=findViewById(R.id.bt1);
        tv_content=findViewById(R.id.tv_content);
        et_content=findViewById(R.id.et_content);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });
    }

    /**
     * 按钮的点击事件，点击之后调用服务端的addBookIn方法
     *
     */
    public void addBook() {
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!mBound) {
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBookManager == null) return;

        Book book = new Book();
        book.setName("APP研发录In");
        book.setPrice(30);
        try {
            Log.e("jason_adil","客户端add："+ book.toString());
            mBookManager.addBook(book);
            mBookManager.addContent(et_content.getText().toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getBook() throws RemoteException {
        if(!mBound){
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBookManager==null)return;
        Log.e("jason_adil","客户端get："+mBookManager.getBooks().toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            attemptToBindService();
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mBound) {
//            unbindService(mServiceConnection);
//            mBound = false;
//        }
//    }

    /**
     * 尝试与服务端建立连接
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.yese.myaidl.AIDLService");
        intent.setPackage("com.yese.myaidl");//对应的包名
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("jason_adil","客户端链接成功:"+name.toString());
            mBookManager = BookManager.Stub.asInterface(service);
            mBound = true;
            if (mBookManager != null) {
                try {
                    mBookManager.registerCallback(serviceCallback);
                    mBooks = mBookManager.getBooks();
                    Log.e("jason_adil","客户端获取值："+mBooks.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("jason_adil","客户端disconnected：");
            try {
                mBookManager.unregisterCallback(serviceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBound = false;
        }
    };


    IRemoteServiceCallback serviceCallback=new IRemoteServiceCallback.Stub(){

        @Override
        public void valueChanged(final String book){
            Log.e("jason_adil","客户端接受到回调："+book);
            tv_content.post(new Runnable() {
                @Override
                public void run() {
                    tv_content.setText(book);
                }
            });
        }
    };

}
