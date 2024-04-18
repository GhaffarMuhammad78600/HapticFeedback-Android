package com.app.hepticfeedback;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.droidsonroids.gif.GifDrawable;

abstract class BaseActivity extends AppCompatActivity {

    Vibrator vibrator;
    int PICK_JSON_FILE_REQUEST_CODE = 2;
    int PICK_GIF_FILE_REQUEST_CODE = 3;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    protected abstract void appContext(Context context);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appContext(this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


    }

    public String readTextFromUri(Uri uri) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

    public boolean isPrimitiveFeedback(String haptic) {
        boolean isPrimitiveFeedback = false;
        switch (haptic) {
            case "PRIMITIVE_CLICK":
            case "PRIMITIVE_THUD":
            case "PRIMITIVE_SPIN":
            case "PRIMITIVE_QUICK_RISE":
            case "PRIMITIVE_SLOW_RISE":
            case "PRIMITIVE_QUICK_FALL":
            case "PRIMITIVE_TICK":
            case "PRIMITIVE_LOW_TICK":
                isPrimitiveFeedback = true;
                break;
        }

        return isPrimitiveFeedback;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public int getPrimitiveFeedback(String haptic) {
        int hapticId = 0;
        switch (haptic) {
            case "PRIMITIVE_CLICK":
                hapticId = VibrationEffect.Composition.PRIMITIVE_CLICK;
                break;
            case "PRIMITIVE_THUD":
                hapticId = VibrationEffect.Composition.PRIMITIVE_THUD;
                break;
            case "PRIMITIVE_SPIN":
                hapticId = VibrationEffect.Composition.PRIMITIVE_SPIN;
                break;
            case "PRIMITIVE_QUICK_RISE":
                hapticId = VibrationEffect.Composition.PRIMITIVE_QUICK_RISE;
                break;
            case "PRIMITIVE_SLOW_RISE":
                hapticId = VibrationEffect.Composition.PRIMITIVE_SLOW_RISE;
                break;
            case "PRIMITIVE_QUICK_FALL":
                hapticId = VibrationEffect.Composition.PRIMITIVE_QUICK_FALL;
                break;
            case "PRIMITIVE_TICK":
                hapticId = VibrationEffect.Composition.PRIMITIVE_TICK;
                break;
            case "PRIMITIVE_LOW_TICK":
                hapticId = VibrationEffect.Composition.PRIMITIVE_LOW_TICK;
                break;

        }

        return hapticId;
    }

    public boolean isPreDefinedFeedback(String haptic) {
        boolean isPreDefinedFeedback = false;
        switch (haptic) {
            case "EFFECT_CLICK":
            case "EFFECT_DOUBLE_CLICK":
            case "EFFECT_TICK":
            case "EFFECT_THUD":
            case "EFFECT_POP":
            case "EFFECT_HEAVY_CLICK":
            case "EFFECT_TEXTURE_TICK":
            case "EFFECT_STRENGTH_LIGHT":
            case "EFFECT_STRENGTH_MEDIUM":
            case "EFFECT_STRENGTH_STRONG":
                isPreDefinedFeedback = true;
                break;
        }

        return isPreDefinedFeedback;
    }

    public int getPreDefinedFeedback(String haptic) {
        int hapticId = 0;
        switch (haptic) {
            case "EFFECT_CLICK":
                hapticId = VibrationEffect.EFFECT_CLICK;
                break;
            case "EFFECT_DOUBLE_CLICK":
                hapticId = VibrationEffect.EFFECT_DOUBLE_CLICK;
                break;
            case "EFFECT_TICK":
                hapticId = VibrationEffect.EFFECT_TICK;
                break;
//            case "EFFECT_THUD":
//                hapticId = VibrationEffect.EFFECT_THUD;
//                break;
//            case "EFFECT_POP":
//                hapticId = VibrationEffect.EFFECT_POP;
//                break;
            case "EFFECT_HEAVY_CLICK":
                hapticId = VibrationEffect.EFFECT_HEAVY_CLICK;
                break;
//            case "EFFECT_TEXTURE_TICK":
//                hapticId = VibrationEffect.EFFECT_TEXTURE_TICK;
//                break;
//            case "EFFECT_STRENGTH_LIGHT":
//                hapticId = VibrationEffect.EFFECT_STRENGTH_LIGHT;
//                break;
//            case "EFFECT_STRENGTH_MEDIUM":
//                hapticId = VibrationEffect.EFFECT_STRENGTH_MEDIUM;
//                break;
//            case "EFFECT_STRENGTH_STRONG":
//                hapticId = VibrationEffect.EFFECT_STRENGTH_STRONG;
//                break;
        }

        return hapticId;
    }


    void pickJsonFromGallery() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please pick a Haptic file!");
        builder.setPositiveButton("OK", (dialog, which) -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/json");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            Intent chooser = Intent.createChooser(intent, "Choose a JSON file");
            startActivityForResult(chooser, PICK_JSON_FILE_REQUEST_CODE);

        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }


    void pickGifFromGallery() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please pick a Gif file!");
        builder.setPositiveButton("OK", (dialog, which) -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            Intent chooser = Intent.createChooser(intent, "Choose a GIF file");
            startActivityForResult(chooser, PICK_GIF_FILE_REQUEST_CODE);

        });

        AlertDialog dialog = builder.create();
        dialog.dismiss();
        dialog.show();
    }


    public void playHapticEffect(HapticEffect hapticEffect) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect effect = null;

                if ("WAVEFORM".equals(hapticEffect.effectId) && hapticEffect.timing != null) {

                    long[] timingArray = hapticEffect.timing.stream().mapToLong(i -> i).toArray();

                    int[] amplitudeArray;
                    if (hapticEffect.amplitude instanceof List) {

                        List<Integer> amplitudeList = (List<Integer>) hapticEffect.amplitude;
                        amplitudeArray = amplitudeList.stream().mapToInt(i -> i).toArray();

                    } else if (hapticEffect.amplitude instanceof Integer) {

                        int singleAmplitude = (Integer) hapticEffect.amplitude;
                        amplitudeArray = new int[timingArray.length];
                        Arrays.fill(amplitudeArray, singleAmplitude);

                    } else {

                        amplitudeArray = new int[timingArray.length];
                        Arrays.fill(amplitudeArray, VibrationEffect.DEFAULT_AMPLITUDE);

                    }

                    effect = VibrationEffect.createWaveform(timingArray, amplitudeArray, -1);

                } else if (isPrimitiveFeedback(hapticEffect.effectId)) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                        double amplitudeDouble = (double) hapticEffect.getAmplitude();
                        float amplitudeFloat = (float) amplitudeDouble;

                        effect = VibrationEffect.startComposition()
                                .addPrimitive(getPrimitiveFeedback(hapticEffect.effectId), amplitudeFloat)
                                .compose();

                    } else {
                        effect = VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE);
                    }


                } else if (isPreDefinedFeedback(hapticEffect.effectId)) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        effect = VibrationEffect.createPredefined(getPreDefinedFeedback(hapticEffect.effectId));
                    } else {
                        effect = VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE);
                    }

                } else {
                    effect = VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE);
                }

                if (effect != null) {
                    vibrator.vibrate(effect);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void triggerAdvancedHapticFeedback() {
        long[] vibrationPattern = {0, 100, 50, 300}; // Start without a delay, vibrate for 100 milliseconds, pause for 50 milliseconds, vibrate for 300 milliseconds.

        // The '-1' here means to perform the vibration pattern once.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, -1));
        } else {
            vibrator.vibrate(vibrationPattern, -1);
        }
    }

    public void triggerHapticFeedback() {
        // Check if the device has a vibrator.
        if (vibrator.hasVibrator()) {
            // Vibrate for a specific duration.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // New API for Android Oreo (8.0) and above.
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // Deprecated API for below Android Oreo (8.0).
                vibrator.vibrate(100);
            }
        } else {
            showToast("Device doesn't support vibrator");
        }
    }

    void playHapticFeedback(long[] pattern, VibrationEffect effect, int repeat) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(effect);
            } else {
                vibrator.vibrate(pattern, repeat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }


    public long getGifDurationFromUri(Context context, Uri gifUri) {

        GifDrawable gifDrawable = null;
        try {
            gifDrawable = new GifDrawable(context.getContentResolver(), gifUri);

            int numberOfFrames = gifDrawable.getNumberOfFrames();
            long duration = 0;

            for (int i = 0; i < numberOfFrames; i++) {
                duration += gifDrawable.getFrameDuration(i);
            }

            return duration;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (gifDrawable != null) {
                gifDrawable.recycle();
            }
        }

    }


}
