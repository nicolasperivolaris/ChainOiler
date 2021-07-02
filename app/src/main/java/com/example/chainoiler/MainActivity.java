package com.example.chainoiler;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.chainoiler.controllers.OilerController;
import com.example.chainoiler.controllers.utils.Actions;
import com.example.chainoiler.controllers.utils.Modes;
import com.example.chainoiler.controllers.utils.resultlisteners.OilerListener;

public class MainActivity extends AppCompatActivity implements OilerListener {

    private OilerController oilerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activatesModes(false);
        activateButtons(false);

        oilerController = new OilerController(this);
    }

    private void activateButtons(boolean b) {
        findViewById(R.id.sprayBt).setEnabled(b);
        findViewById(R.id.continuousBt).setEnabled(b);
        findViewById(R.id.drop1Bt).setEnabled(b);
    }

    public void onModeEdit(View view) {
        activatesModes(((SwitchCompat) view).isChecked());
    }

    public void onAction(View view) {
        Button b = ((Button) view);
        oilerController.setAction(getAction(b));
        if (getAction(b) != Actions.PumpOn)
            onUnlock(findViewById(R.id.lockBt));
    }

    public void onModeChange(View view) {
        //if(view)
    }

    private RadioButton getButton(Actions actions) {
        switch (actions) {
            case Spray:
                return (findViewById(R.id.sprayBt));
            case OneDrop:
                return (findViewById(R.id.drop1Bt));
            case PumpOn:
                return (findViewById(R.id.continuousBt));
            default:
                throw new RuntimeException("No button for mode " + actions);
        }
    }

    private Actions getAction(Button button) {
        if (button.getText().equals(getResources().getString(R.string.spray))) return Actions.Spray;
        else if (button.getText().equals(getResources().getString(R.string._1_drop)))
            return Actions.OneDrop;
        else if (button.getText().equals(getResources().getString(R.string.pump_on)))
            return Actions.PumpOn;
        else throw new RuntimeException("No mode for button " + button.getText());
    }

    public void onUnlock(View view) {
        Button lock = (Button) view;
        if (lock.getText().equals(getResources().getString(R.string.lock))) {
            lock.setText(R.string.unlock);
            findViewById(R.id.buttonsTL).setBackgroundResource(R.drawable.caution);
        } else {
            lock.setText(R.string.lock);
            findViewById(R.id.buttonsTL).setBackground(null);
        }
        activateButtons(lock.getText().equals(getResources().getString(R.string.unlock)));
    }

    private void activatesModes(boolean check) {
        RadioGroup modes = findViewById(R.id.modeRG);
        for (int i = 0; i < modes.getChildCount(); i++)
            modes.getChildAt(i).setEnabled(check);
    }

    @Override
    public void notify(Modes mode) {
    }

    @Override
    public void notifyTemperature(float t) {
        runOnUiThread(() -> ((TextView) findViewById(R.id.tempET)).setText(String.valueOf(t)));

    }

    @Override
    public void notifyHumidity(float h) {
        runOnUiThread(() -> ((TextView) findViewById(R.id.humidityET)).setText(String.valueOf(h)));
    }

    @Override
    public void notify(Actions action) {
        runOnUiThread(() -> {
            RadioGroup group = ((RadioGroup) findViewById(R.id.actionsGroup));
            group.clearCheck();
            onUnlock(findViewById(R.id.lockBt));
        });
    }
}