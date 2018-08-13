package com.example.chris.baking.UI;

import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.DataTypes.RecipeStep;
import com.example.chris.baking.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;


public class DetailFragment extends AppCompatActivity implements ExoPlayer.EventListener{

    public static final String EXTRA_RECIPE = "extra_recipe";
    public static final String EXTRA_STEP = "extra_step";


    private Recipe mCurrentRecipe;
    private RecipeStep mCurrentRecipeStep;
    private int mCurrentStepNumber;

    private ImageView mClipArtImageView;

    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayer mExoPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCurrentRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
        mCurrentStepNumber = getIntent().getIntExtra(EXTRA_STEP, -5);
        mCurrentRecipeStep = mCurrentRecipe.getRecipieSteps().get(mCurrentStepNumber);

        mClipArtImageView = findViewById(R.id.detail_activity_image_view);

        setupMediaSession();

        setupExoplayer();

        updateActivityDescription();

        bindButtons();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setLandscapeView();
        } else {
            setPortraitView();
        }


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if ((newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)){

            setLandscapeView();



        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            setPortraitView();

        }

    }

    private void setPortraitView() {
        PlayerView playerView = findViewById(R.id.recipe_step_fragment_exoplayer_view);
        ViewGroup.LayoutParams params = playerView.getLayoutParams();

        //https://androidactivity.wordpress.com/2011/10/04/use-dip-sp-metrics-programmatically/
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());


        params.width = LinearLayout.LayoutParams.MATCH_PARENT;


        if(getActionBar() != null){
            getActionBar().show();
        }
    }

    private void setLandscapeView() {
        PlayerView playerView = findViewById(R.id.recipe_step_fragment_exoplayer_view);

        ViewGroup.LayoutParams params = playerView.getLayoutParams();

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;

        if(getActionBar() != null){
            getActionBar().hide();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaSession.setActive(false);
        releaseExoplayer();


    }

    void setupExoplayer() {

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            PlayerView playerView = findViewById(R.id.recipe_step_fragment_exoplayer_view);

            playerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play_movie));

            playerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            setVideo();

    }


    private void setupMediaSession() {
        mMediaSession = new MediaSessionCompat(this, DetailFragment.class.getSimpleName());

        //binds what actions to handle
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //prevents restarting app when not visible
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new BakingAppSessionCallback());

        mMediaSession.setActive(true);
    }

    void releaseExoplayer(){
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    void bindButtons() {
        Button previousButton = findViewById(R.id.button_previous);
        Button nextButton = findViewById(R.id.button_next);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentStepNumber < mCurrentRecipe.getRecipieSteps().size() -1){
                    updateView(mCurrentStepNumber + 1);


                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_at_last_step), Toast.LENGTH_SHORT).show();
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentStepNumber > 0){
                    updateView(mCurrentStepNumber - 1);
                }else {
                    Toast.makeText(getApplicationContext(),getString(R.string.error_at_first_step), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void updateView(int newStepNumber){
        Boolean isPlaying = mExoPlayer.getPlayWhenReady();

        mExoPlayer.stop();
        mCurrentStepNumber = newStepNumber;
        mCurrentRecipeStep = mCurrentRecipe.getRecipieSteps().get(mCurrentStepNumber);

        setVideo();

        updateActivityDescription();

        mExoPlayer.setPlayWhenReady(isPlaying);

    }

    void setVideo(){

        if (!mCurrentRecipeStep.getVideoURL().isEmpty()) {

            PlayerView playerView = findViewById(R.id.recipe_step_fragment_exoplayer_view);
            playerView.setVisibility(View.VISIBLE);

            mClipArtImageView.setVisibility(View.GONE);

            ImageView imageView = findViewById(R.id.detail_activity_image_view);
            imageView.setVisibility(View.GONE);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, "baking"), null);

            Uri uri = (Uri.parse(mCurrentRecipeStep.getVideoURL()));

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

            mExoPlayer.prepare(videoSource);

        } else {
            PlayerView playerView = findViewById(R.id.recipe_step_fragment_exoplayer_view);

            ViewGroup.LayoutParams params = playerView.getLayoutParams();

            params.height = playerView.getMeasuredHeight();
            params.width = playerView.getMeasuredWidth();

            playerView.setVisibility(View.GONE);

            mClipArtImageView.setLayoutParams(params);
            mClipArtImageView.setVisibility(View.VISIBLE);


            Picasso.with(getApplicationContext()).load(R.drawable.baking_clipart).into(mClipArtImageView);
        }




    }

    void updateActivityDescription(){
        TextView textView = findViewById(R.id.detail_activity_description_tv);

        textView.setText(mCurrentRecipeStep.getDescription());
    }

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
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
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

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    private class BakingAppSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

}


