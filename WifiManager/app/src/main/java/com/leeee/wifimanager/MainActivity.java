package com.leeee.wifimanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.leeee.wifimanager.wifiManager.CommandServer;
import com.leeee.wifimanager.wifiManager.UiCallback;
import com.leeee.wifimanager.wifiManager.WifiFactory;

public class MainActivity extends AppCompatActivity implements UiCallback{
    private Button mConnectBtn = null;
    private Button mDisconnectBtn = null;

    private EditText mIpEditText = null;
    private EditText mPortEditText = null;

    private String mIpAddress = "";
    private String mIpPort = "";

    private WifiFactory mWifiFactory = null;
    private CommandServer mCommandServer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mConnectBtn = (Button) findViewById(R.id.connect_btn);
        mDisconnectBtn = (Button) findViewById(R.id.disconnect_btn);

        mIpEditText = (EditText) findViewById(R.id.ip_editText);
        mPortEditText = (EditText) findViewById(R.id.port_editText);

        mConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIpAddress = mIpEditText.getText().toString();
                mIpPort = mPortEditText.getText().toString();

                mWifiFactory = new WifiFactory(MainActivity.this);
                Log.e("lyt", "ip = " + mIpAddress + " port = " + mIpPort);
                mCommandServer = mWifiFactory.createServer(mIpAddress, mIpPort, MainActivity.this);

                if (mCommandServer == null) {
                    Toast.makeText(getApplicationContext(), "Connection error!", Toast.LENGTH_SHORT).show();
                } else {
                    mCommandServer.startServer();
                }
            }
        });

        mDisconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommandServer != null) {
                    mCommandServer.shutdownServer();
                }
            }
        });
    }

    @Override
    public void onConnected() {
        Toast.makeText(getApplicationContext(), "Connect succeed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(getApplicationContext(), "Disconnected!", Toast.LENGTH_SHORT).show();
    }
}
