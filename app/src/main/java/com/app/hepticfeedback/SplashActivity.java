package com.app.hepticfeedback;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.app.hepticfeedback.databinding.ActivitySplashBinding;
import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends BaseActivity {

    public HapticEffectsContainer hapticsDTOS = null;
    public List<HapticEffect> effectList = new ArrayList<>();
    private Context context;
    private ActivitySplashBinding binding;

    private Uri selectedFileUri = null;

    @Override
    protected void appContext(Context context) {
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        pickGifFromGallery();

    }

    private void routeToMainActivity(long duration) {

        for (HapticEffect hapticEffect : effectList) {
            handler.postDelayed(() -> {

                playHapticEffect(hapticEffect);

            }, hapticEffect.timeStart);
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {

            handler.removeCallbacksAndMessages(null);
            if (vibrator != null) {
                vibrator.cancel();
            }


            Intent homeIntent = new Intent(context, HomeActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();

        }, duration);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_JSON_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    if (inputStream != null) {
                        String jsonContent = readTextFromUri(uri);

                        //parse data from json file
                        hapticsDTOS = HapticHelper.parseHapticEffects(jsonContent);
                        effectList = hapticsDTOS.getHapticEffects();

                        //pick Gif from storage
                        handleGifWithHaptic();

                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == PICK_GIF_FILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                if (data != null && data.getData() != null) {
                    selectedFileUri = data.getData();

                    pickJsonFromGallery();


                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "No GIF file selected!", Toast.LENGTH_SHORT).show();

                //allow user to select gif again if not loaded
                new Handler(Looper.getMainLooper())
                        .postDelayed(() -> {

                            pickGifFromGallery();

                        }, 300);

            }
        }

    }

    private void handleGifWithHaptic() {
        if (selectedFileUri != null) {
            executor.execute(() -> {
                long duration = getGifDurationFromUri(context, selectedFileUri);
                handler.post(() -> {

                    //load gif
                    Glide.with(this)
                            .load(selectedFileUri)
                            .into(binding.gifView);

                    //route user to home
                    routeToMainActivity(duration != -1 ? duration : 3000);

                });
            });
        }
    }


}