package com.example.lenovo.retrofit_check;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ExoPlayer extends YouTubeBaseActivity {
    SimpleExoPlayer player;
    PlayerView playerView;

    int currentWindow=0;
    boolean playWhenReady=true;
    long playbackPosition=0;
    ArrayList<String> keys;
    ArrayList<String> files;
    int current;
    private  Intent intent;
    MediaSource mediaSource;
    YouTubePlayerView youTubePlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);

        youTubePlayerView =
                findViewById(R.id.player);
        playerView = findViewById(R.id.video_view);
        intent=getIntent();
        keys=intent.getStringArrayListExtra("keyList");
        files=intent.getStringArrayListExtra("filePaths");
        current=intent.getIntExtra("current", 0);

        if(keys!=null){
            playerView.setVisibility(View.GONE);
            youTubePlayerView.setVisibility(View.VISIBLE);
            youTubePlayerView.initialize(String.valueOf(R.string.API_KEY),
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {

                            // do any work here to cue video, play video, etc.
                            youTubePlayer.loadVideos(keys, current, 0);
                        }
                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });
            return;
        }
        initializePlayer();
        ImageButton next=findViewById(R.id.exo_next);
        ImageButton prev=findViewById(R.id.exo_prev);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current++;
                if(files!=null&&current==files.size()){
                    Toast.makeText(getApplicationContext(), "End of PlayList!", Toast.LENGTH_SHORT).show();
                    current--;
                    return;
                }
                Play(files.get(current));
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current--;
                if(current>=0){
                    Play(files.get(current));
                }
                else {
                    Toast.makeText(getApplicationContext(), "End of PlayList!", Toast.LENGTH_SHORT).show();
                    current++;
                }
            }
        });

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            // this will request for permission when permission is not true
        }else{
                Play(files.get(current));
                return;
        }
    }
    private MediaSource buildMediaSource(Uri uri1) {
        ExtractorMediaSource videoSource =
                new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                        createMediaSource(uri1);
        ExtractorMediaSource audioSource =
         new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(ExoPlayer.this,"Exoplayer-local")).
                createMediaSource(RawResourceDataSource.buildRawResourceUri(R.raw.black));
        if(keys==null){
            videoSource= new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(ExoPlayer.this, "ua")).
                    createMediaSource(uri1);
        }
        return new ConcatenatingMediaSource(videoSource, audioSource);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("result", current);
        setResult(RESULT_OK, intent);
        if(keys!=null) {
            finish();
            return;
        }
        player.release();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(keys!=null)
            return;
        player.prepare(mediaSource);
        player.seekTo(currentWindow, playbackPosition);
        if(player.getCurrentPosition()==0)
            player.setPlayWhenReady(true);
        else
            player.setPlayWhenReady(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(keys!=null)
            return;
        playbackPosition = player.getCurrentPosition()-5;
        currentWindow = player.getCurrentWindowIndex();
        player.setPlayWhenReady(false);
    }
//    public void play(String key){
//    }
    public  void Play(String file){
        File file_=new File(file);
        mediaSource = buildMediaSource(Uri.fromFile(file_));
        player.prepare(mediaSource, true, true);
        player.seekTo(0, 0);
        player.setPlayWhenReady(playWhenReady);
    }
    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                    if(player.getCurrentWindowIndex()!=0){
                        player.prepare(mediaSource, true, true);
                        player.setPlayWhenReady(false);
                        player.seekTo(0, 0);
                    }
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        playerView.setPlayer(player);

    }
}
