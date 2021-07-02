package com.example.chainoiler.controllers.utils;

import java.util.EventListener;

public interface BluetoothListener extends EventListener {
    void connectionBroken();

    void connectionSucceed();

    void connectionFailed();

    void messageReceived(String msg);
}
