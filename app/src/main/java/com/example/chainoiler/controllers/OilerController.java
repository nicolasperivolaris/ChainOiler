package com.example.chainoiler.controllers;

import com.example.chainoiler.controllers.utils.Actions;
import com.example.chainoiler.controllers.utils.BluetoothListener;
import com.example.chainoiler.controllers.utils.Modes;
import com.example.chainoiler.controllers.utils.resultlisteners.OilerListener;

import java.util.EnumMap;
import java.util.Map;

public class OilerController implements BluetoothListener {
    private final Map<Modes, byte[]> modesLimits;
    private final OilerListener oilerListener;
    public Bluetooth bluetooth;
    private float temp, humi;
    private Modes currentMode;

    public OilerController(OilerListener oilerListener) {
        bluetooth = new Bluetooth();
        Thread t = new Thread(() -> bluetooth.connect());
        t.start();
        modesLimits = new EnumMap<>(Modes.class);

        modesLimits.put(Modes.Dry, new byte[]{0, 90});
        modesLimits.put(Modes.Wet, new byte[]{90, 100});
        modesLimits.put(Modes.Cold, new byte[]{-99, 5});
        modesLimits.put(Modes.Hot, new byte[]{5, 99});

        this.oilerListener = oilerListener;
        bluetooth.registerBluetoothListener(this);
    }

    public boolean isConnected() {
        return bluetooth.isConnected();
    }

    public byte[] getHumidityLimits(Modes mode) {
        return modesLimits.get(mode);
    }

    public float getTemperature() {
        return temp;
    }

    public float getHumidity() {
        return humi;
    }

    public Bluetooth getBluetooth() {
        return bluetooth;
    }

    @Override
    public void connectionBroken() {
        bluetooth.connect();
    }

    @Override
    public void connectionSucceed() {
        getTemp();
        getHumi();
        getMode();
    }


    @Override
    public void connectionFailed() {
        if (bluetooth.getConnectionAttempts() < 10)
            bluetooth.connect();
        else throw new RuntimeException("Connection Error");
    }

    @Override
    public void messageReceived(String msg) {
        String[] cmds = msg.split(":");
        switch (cmds[0]) {
            case "temp":
                temp = Float.parseFloat(cmds[1]);
                oilerListener.notifyTemperature(temp);
                break;
            case "humi":
                humi = Float.parseFloat(cmds[1]);
                oilerListener.notifyHumidity(humi);
                break;
            case "mode":
                currentMode = Modes.valueOf(cmds[1]);
                oilerListener.notify(currentMode);
                break;
            case "cmd":
                oilerListener.notify(Actions.valueOf(cmds[1]));
                break;
        }
    }

    public Modes getCurrentMode() {
        return currentMode;
    }

    public void setMode(Modes mode) {
        sendAsync("mode:" + mode.name());
    }

    public void setAction(Actions actions) {
        sendAsync("cmd:" + actions.name());
    }

    private void sendAsync(String cmd) {
        Thread t = new Thread(() -> bluetooth.send(cmd));
        t.start();
    }

    private void getTemp() {
        sendAsync("ask:temp");
    }

    private void getHumi() {
        sendAsync("ask:humi");
    }

    private void getMode() {
        sendAsync("ask:mode");
    }
}
