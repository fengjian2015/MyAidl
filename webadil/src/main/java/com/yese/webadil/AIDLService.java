package com.yese.webadil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yese.webadil.Book;
import com.yese.webadil.BookManager;
import com.yese.webadil.IRemoteServiceCallback;

import java.util.ArrayList;
import java.util.List;

/*
 *   author:jason
 *   date:2019/5/1716:59
 */
public class AIDLService extends Service {
    //包含Book对象的list
    private List<Book> mBooks = new ArrayList<>();
    private List<IRemoteServiceCallback> iRemoteServiceCallbacks=new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("jason_adil","服务端onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("jason_adil","服务端onBind"+intent.toString());
        return mBookManager;
    }

    private BookManager.Stub mBookManager = new BookManager.Stub() {
        @Override
        public List<Book> getBooks(){
            synchronized (this) {
                if (mBooks != null) {
                    return mBooks;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book){
            if (book==null)return;
            if (mBooks == null) {
                mBooks = new ArrayList<>();
            }
            if (!mBooks.contains(book)) {
                mBooks.add(book);
            }
            Log.e("jason_adil","服务端接受到的值："+mBooks.toString());

        }

        @Override
        public void addContent(String content) {
            myCallback(content);
        }

        @Override
        public void registerCallback(IRemoteServiceCallback cb) {
            iRemoteServiceCallbacks.add(cb);
        }

        @Override
        public void unregisterCallback(IRemoteServiceCallback cb) {
            iRemoteServiceCallbacks.remove(cb);
        }
    };

    private void myCallback(String content){
        for (IRemoteServiceCallback iRemoteServiceCallback:iRemoteServiceCallbacks){
            try {
                iRemoteServiceCallback.valueChanged(content);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


}
