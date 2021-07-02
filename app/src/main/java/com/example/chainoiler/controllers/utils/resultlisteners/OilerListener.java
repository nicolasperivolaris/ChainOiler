package com.example.chainoiler.controllers.utils.resultlisteners;

import com.example.chainoiler.controllers.utils.Actions;
import com.example.chainoiler.controllers.utils.Modes;

public interface OilerListener {
    void notify(Modes mode);

    void notifyTemperature(float t);

    void notifyHumidity(float h);

    void notify(Actions action);
}
