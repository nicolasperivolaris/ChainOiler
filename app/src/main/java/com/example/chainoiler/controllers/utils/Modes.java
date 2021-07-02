package com.example.chainoiler.controllers.utils;

public enum Modes {
    Auto,
    Dry,
    Wet,
    Hot,
    Cold;
    private float min, max;

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }
}
