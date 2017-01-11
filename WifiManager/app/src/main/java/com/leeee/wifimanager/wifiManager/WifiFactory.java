package com.leeee.wifimanager.wifiManager;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.regex.Pattern;

public class WifiFactory {
    private Context mContext;
    private boolean mIsConnected = false;

    public WifiFactory(Context context) {
        mContext = context;

    }

    public CommandServer createServer(final String ipAddress, final String port, UiCallback callback) {
        if (!checkIP(ipAddress)){
            return null;
        }

        final Connection[] connection = {null};
        final Object lock = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                connection[0] = new Connection(ipAddress, port);
                synchronized (lock) {
                    try {
                        connection[0].open();
                        mIsConnected = true;
                        lock.notify();

                    } catch (IOException e) {
                        Log.e("lyt", "IO error");
                        mIsConnected = false;
                        lock.notify();
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        Log.e("lyt", "NumberFormatException error");
                        mIsConnected = false;
                        lock.notify();
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (mIsConnected) {
            CommandServer commandServer = new CommandServer(mContext, connection[0], callback);
            return commandServer;
        } else {
            return null;
        }
    }

    private boolean checkIP(String str) {
        Pattern pattern = Pattern
                .compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
                        + "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(str).matches();
    }
}
