package com.app.hepticfeedback;

import java.util.List;

public class HapticEffectsContainer {

    private List<HapticEffect> hapticEffects;

    public HapticEffectsContainer(List<HapticEffect> hapticEffects) {
        this.hapticEffects = hapticEffects;
    }

    public List<HapticEffect> getHapticEffects() {
        return hapticEffects;
    }

    @Override
    public String toString() {
        return "HapticEffectsContainer{" +
                "hapticEffects=" + hapticEffects +
                '}';
    }
}
