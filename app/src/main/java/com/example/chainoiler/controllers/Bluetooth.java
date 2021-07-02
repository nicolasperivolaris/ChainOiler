package com.example.chainoiler.controllers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.example.chainoiler.controllers.utils.BluetoothListener;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Bluetooth {
    private final String deviceName = "ChainOiler";
    private final List<BluetoothListener> listeners;
    private BluetoothDevice pump;
    private BluetoothSocket btSocket;
    private boolean connected;

    private byte connectionAttempts;

    Bluetooth() {
        listeners = new LinkedList<>();
    }

    public void connect() {
        try {
            connectionAttempts++;
            BluetoothAdapter BluetoothAdap = BluetoothAdapter.getDefaultAdapter();
            for (BluetoothDevice device : BluetoothAdap.getBondedDevices()) {
                String name = device.getName();
                if (name.equals(deviceName))
                    pump = device;
            }

            UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

            if (btSocket == null || !isConnected()) {
                BluetoothAdap = BluetoothAdapter.getDefaultAdapter();

                // This will connect the device with address as passed
                BluetoothDevice hc = BluetoothAdap.getRemoteDevice(pump.getAddress());
                btSocket = hc.createInsecureRfcommSocketToServiceRecord(myUUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

                btSocket.connect();
                connected = true;
                listen();
            }
        } catch (IOException e) {
            connected = false;
            e.printStackTrace();
            for (BluetoothListener listener : listeners) {
                listener.connectionFailed();
            }
        }
    }

    private void listen() {
        while (isConnected()) {
            char c;
            int i = 0;
            StringBuilder sb = new StringBuilder();
            try {
                while ((c = (char) btSocket.getInputStream().read()) != '\n' && i < 100) {
                    sb.append(c);
                    i++;
                }
                for (BluetoothListener listener : listeners) {
                    listener.messageReceived(sb.toString().trim());
                }
            } catch (IOException e) {
                connected = false;
                for (BluetoothListener listener : listeners) {
                    listener.connectionBroken();
                }
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void disconnect() {
        connected = false;
        try {
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        if (btSocket != null) {
            try { // Converting the string to bytes for transferring
                btSocket.getOutputStream().write(msg.getBytes());
            } catch (IOException e) {
                connected = false;
                e.printStackTrace();
            }
        }
    }

    public void registerBluetoothListener(BluetoothListener listener) {
        listeners.add(listener);
    }

    public byte getConnectionAttempts() {
        return connectionAttempts;
    }
}
