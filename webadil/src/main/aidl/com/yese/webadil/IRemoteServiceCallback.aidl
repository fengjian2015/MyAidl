// IRemoteServiceCallback.aidl
package com.yese.webadil;

import com.yese.webadil.Book;

// Declare any non-default types here with import statements

interface IRemoteServiceCallback {
    void valueChanged(String content);
}
