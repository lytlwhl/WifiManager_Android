package com.leeee.wifimanager.wifiManager;


import android.content.Context;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class CommandServer {

    private Context mContext;
    private Connection mConnection;
    private UiCallback mCallback;
    private Thread mCommandReceiver;
    private Thread mSender;
    public static LinkedBlockingQueue<byte[]> mSendDataPool = new LinkedBlockingQueue();


    CommandServer(Context context, Connection connection, UiCallback callback) {
        mContext = context;
        mCallback = callback;
        mConnection = connection;
    }

    public void startServer() {
        mCommandReceiver = new Thread(new receiveRunnable());
        mCommandReceiver.start();

        mSender = new Thread(new SendRunnable());
        mSender.start();

        mCallback.onConnected();
    }

    public void shutdownServer() {
        mConnection.close();
        mCallback.onDisconnected();
    }

    private class receiveRunnable implements Runnable {
        @Override
        public void run() {
            byte[] buffer = new byte[1024];

            try {
                while (mConnection.mInputStream.read(buffer) != -1) {

                    /*do something*/
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                byte[] data = null;
                synchronized (mSendDataPool) {
                    data = mSendDataPool.poll();

                    if (data == null) {
                        continue;
                    }
                    try {
                        mConnection.mOutputStream.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
