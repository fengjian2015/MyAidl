// ServerManager.aidl
package com.yese.mutual;

// Declare any non-default types here with import statements
import com.yese.mutual.IRemoteServiceCallback;

interface ServerManager {

    void pullUpService(String content,in Map activatedPackage);
}
