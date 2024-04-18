package com.app.hepticfeedback;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.app.hepticfeedback.databinding.ActivityHomeBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements Player.Listener {

    private final int PICK_VIDEO_REQUEST = 1;
    public HapticEffectsContainer hapticsDTOS = null;
    public List<HapticEffect> effectList = new ArrayList<>();
    private Context context;
    private ActivityHomeBinding binding;
    private ExoPlayer player;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Uri videoUri = null;

    @Override
    protected void appContext(Context context) {
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        initExoPlayer();

        pickVideoFromGallery();

    }

    private void initExoPlayer() {
        player = new ExoPlayer.Builder(this).build();
        binding.mediaPlayer.setPlayer(player);
        player.addListener(this);
    }


    private void pickVideoFromGallery() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please pick a video!");
        builder.setPositiveButton("OK", (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_VIDEO_REQUEST);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void playVideo(Uri videoUri) {
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        player.release();
        handler.removeCallbacksAndMessages(null);
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.removeCallbacksAndMessages(null);
        if (vibrator != null) {
            vibrator.cancel();
        }
    }


    @Override
    public void onPlaybackStateChanged(int playbackState) {
        if (playbackState == Player.STATE_READY && !player.isPlayingAd()) {

            for (HapticEffect hapticEffect : effectList) {
                handler.postDelayed(() -> {

                    playHapticEffect(hapticEffect);

                }, hapticEffect.timeStart);
            }

        } else if (playbackState == Player.STATE_ENDED && !player.isPlaying()) {

            Intent splashIntent = new Intent(this, SplashActivity.class);
            startActivity(splashIntent);
            finish();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();

            pickJsonFromGallery();

        } else if (requestCode == PICK_JSON_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    if (inputStream != null) {
                        String jsonContent = readTextFromUri(uri);

                        //parse data from json file
                        hapticsDTOS = HapticHelper.parseHapticEffects(jsonContent);
                        effectList = hapticsDTOS.getHapticEffects();

                        //play video
                        playVideo(videoUri);

                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}