package com.app.hepticfeedback;

import java.util.List;

public class HapticEffect {

    public String effectId;
    public String description;
    public Object amplitude;
    public List<Integer> timing;
    public int timeStart;

    public HapticEffect(String effectId, String description, Object amplitude, List<Integer> timing, int timeStart) {
        this.effectId = effectId;
        this.description = description;
        this.amplitude = amplitude;
        this.timing = timing;
        this.timeStart = timeStart;
    }

    public String getEffectId() {
        return effectId;
    }

    public String getDescription() {
        return description;
    }

    public Object getAmplitude() {
        return amplitude;
    }

    public List<Integer> getTiming() {
        return timing;
    }

    public int getTimeStart() {
        return timeStart;
    }

    @Override
    public String toString() {
        return "HapticEffect{" +
                "effectId='" + effectId + '\'' +
                ", description='" + description + '\'' +
                ", amplitude=" + amplitude +
                ", timing=" + timing +
                ", timeStart=" + timeStart +
                '}';
    }
}
