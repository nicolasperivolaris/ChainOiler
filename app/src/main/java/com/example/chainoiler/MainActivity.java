package com.example.chainoiler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activatesModes(false);
        activatesButton(false);


    }

    private void activatesButton(boolean b) {
        ((findViewById(R.id.sprayBt))).setEnabled(b);
        ((findViewById(R.id.continuousBt))).setEnabled(b);
        ((findViewById(R.id.drop1Bt))).setEnabled(b);
    }

    public void onModeEdit(View view){
        boolean check = (((SwitchCompat)view).isChecked());
        activatesModes(check);
    }

    public void onUnlock(View view){
        Button lock = (Button)view;
        if(lock.getText().equals(getResources().getString(R.string.lock))){
            lock.setText(R.string.unlock);
            findViewById(R.id.buttonsTL).setBackgroundResource(R.drawable.caution);
        }
        else{
            lock.setText(R.string.lock);
            findViewById(R.id.buttonsTL).setBackground(null);
        }
        activatesButton(lock.getText().equals(getResources().getString(R.string.unlock)));
    }

    private void activatesModes(boolean check){
        RadioGroup modes = ((RadioGroup)findViewById(R.id.modeRG));
        for(int i = 0; i < modes.getChildCount(); i++)
            modes.getChildAt(i).setEnabled(check);
    }
}