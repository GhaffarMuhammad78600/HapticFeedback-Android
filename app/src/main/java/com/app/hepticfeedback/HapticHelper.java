package com.app.hepticfeedback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HapticHelper {


    public static HapticEffectsContainer parseHapticEffects(String jsonString) throws JSONException {
        List<HapticEffect> hapticEffects = new ArrayList<>();

        JSONObject rootObject = new JSONObject(jsonString);
        JSONArray effectsArray = rootObject.getJSONArray("hapticEffects");

        for (int i = 0; i < effectsArray.length(); i++) {
            JSONObject effectObject = effectsArray.getJSONObject(i);
            String effectId = effectObject.optString("effect_id", effectObject.optString("primitive_id"));
            String description = effectObject.getString("description");
            int timeStart = effectObject.getInt("time_start");

            Object amplitude;
            if (effectObject.optJSONArray("amplitude") != null) {
                List<Integer> amplitudeList = new ArrayList<>();
                JSONArray amplitudeArray = effectObject.getJSONArray("amplitude");
                for (int j = 0; j < amplitudeArray.length(); j++) {
                    amplitudeList.add(amplitudeArray.getInt(j));
                }
                amplitude = amplitudeList;
            } else {
                amplitude = effectObject.optDouble("amplitude", 1.0);
            }

            List<Integer> timing = new ArrayList<>();
            if (effectObject.optJSONArray("timing") != null) {
                JSONArray timingArray = effectObject.getJSONArray("timing");
                for (int j = 0; j < timingArray.length(); j++) {
                    timing.add(timingArray.getInt(j));
                }
            }

            HapticEffect hapticEffect = new HapticEffect(effectId, description, amplitude, timing, timeStart);
            hapticEffects.add(hapticEffect);
        }

        return new HapticEffectsContainer(hapticEffects);
    }
}
