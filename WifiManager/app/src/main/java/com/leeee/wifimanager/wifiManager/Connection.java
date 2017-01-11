package com.leeee.wifimanager.wifiManager;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection {
    private String mIpAddr;
    private String mIpPort;

    public Socket mSocket;
    public InputStream mInputStream = null;
    public OutputStream mOutputStream = null;

    public Connection(String ipAddr, String port) {
        this.mIpAddr = ipAddr;
        this.mIpPort = port;
    }

    public void open() throws IOException {
        Log.e("lyt", "mIpAddr = " + mIpAddr + " mIpPort = " + mIpPort);
        mSocket = new Socket(mIpAddr, Integer.valueOf(mIpPort));
        mInputStream = mSocket.getInputStream();
        mOutputStream = mSocket.getOutputStream();
    }

    public void close() {
        try {
            if (mSocket != null && mSocket.isConnected()) {
                mSocket.shutdownOutput();
                mSocket.shutdownInput();
                mInputStream.close();
                mOutputStream.close();
                mSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
