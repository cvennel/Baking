package com.example.chris.baking.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.DataTypes.RecipeStep;
import com.example.chris.baking.R;
import com.example.chris.baking.Utils.ExoPlayerFullScreenController;
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
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;


public class DetailFragment extends Fragment implements ExoPlayer.EventListener {


    public static final String EXTRA_RECIPE = "extra_recipe";
    public static final String EXTRA_STEP = "extra_step";


    private Recipe mCurrentRecipe;
    private RecipeStep mCurrentRecipeStep;
    private int mCurrentStepNumber;

    private Boolean mIsTwoPane;
    private ImageView mClipArtImageView;

    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;


    ExoPlayerFullScreenController mExoPlayerFullScreenController;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mExoPlayerView;

    private View mRootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mCurrentRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
            mCurrentStepNumber = savedInstanceState.getInt(EXTRA_STEP);
            mCurrentRecipeStep = mCurrentRecipe.getSteps().get(mCurrentStepNumber);
        }

        mRootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mClipArtImageView = mRootView.findViewById(R.id.detail_activity_image_view);

        setupMediaSession();

        setupExoplayer();

        updateActivityDescription();

        mExoPlayerFullScreenController = new ExoPlayerFullScreenController(getActivity(), (SimpleExoPlayerView) mRootView.findViewById(R.id.recipe_step_fragment_exoplayer_view), mRootView);
        bindButtons();

        return mRootView;
    }


    public void setFragmentRecipeInfo(Recipe currentRecipe, int currentStepNumber, Boolean isTwoPane) {
        mCurrentRecipe = currentRecipe;
        mCurrentStepNumber = currentStepNumber;
        mCurrentRecipeStep = mCurrentRecipe.getSteps().get(mCurrentStepNumber);
        mIsTwoPane = isTwoPane;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_RECIPE, mCurrentRecipe);
        outState.putInt(EXTRA_STEP, mCurrentStepNumber);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        adjustImageViewSize();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMediaSession.setActive(false);
        releaseExoplayer();
    }


    void setupExoplayer() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        mExoPlayerView = mRootView.findViewById(R.id.recipe_step_fragment_exoplayer_view);

        mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play_movie));

        mExoPlayerView.setPlayer(mExoPlayer);

        mExoPlayer.addListener(this);

        mExoPlayerView.setControllerShowTimeoutMs(0);
        mExoPlayerView.setControllerHideOnTouch(false);


        setVideo();

    }


    private void setupMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), DetailActivity.class.getSimpleName());

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

    void releaseExoplayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    void bindButtons() {
        Button previousButton = mRootView.findViewById(R.id.button_previous);
        Button nextButton = mRootView.findViewById(R.id.button_next);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentStepNumber < mCurrentRecipe.getSteps().size() - 1) {
                    updateView(mCurrentStepNumber + 1);


                } else {
                    Toast.makeText(getContext(), getString(R.string.error_at_last_step), Toast.LENGTH_SHORT).show();
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentStepNumber > 0) {
                    updateView(mCurrentStepNumber - 1);
                } else {
                    Toast.makeText(getContext(), getString(R.string.error_at_first_step), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void updateView(int newStepNumber) {
        Boolean isPlaying = mExoPlayer.getPlayWhenReady();

        mExoPlayer.stop();
        mCurrentStepNumber = newStepNumber;
        mCurrentRecipeStep = mCurrentRecipe.getSteps().get(mCurrentStepNumber);

        setVideo();

        updateActivityDescription();

        mExoPlayer.setPlayWhenReady(isPlaying);

    }

    void setVideo() {

        if (!mCurrentRecipeStep.getVideoURL().isEmpty()) {

            PlayerView playerView = mRootView.findViewById(R.id.recipe_step_fragment_exoplayer_view);
            playerView.setVisibility(View.VISIBLE);

            mClipArtImageView.setVisibility(View.GONE);

            ImageView imageView = mRootView.findViewById(R.id.detail_activity_image_view);
            imageView.setVisibility(View.GONE);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "baking"), null);

            Uri uri = (Uri.parse(mCurrentRecipeStep.getVideoURL()));

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

            mExoPlayer.prepare(videoSource);

        } else {
            PlayerView playerView = mRootView.findViewById(R.id.recipe_step_fragment_exoplayer_view);


            playerView.setVisibility(View.GONE);

            mClipArtImageView.setVisibility(View.VISIBLE);


            Picasso.with(getContext()).load(R.drawable.baking_clipart).into(mClipArtImageView);

            adjustImageViewSize();

        }


    }

    private void adjustImageViewSize() {
        DisplayMetrics metrics = new DisplayMetrics();

        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mClipArtImageView.setMaxHeight(metrics.heightPixels/2);
    }

    void updateActivityDescription() {
        TextView textView = mRootView.findViewById(R.id.detail_activity_description_tv);

        textView.setText(mCurrentRecipeStep.getDescription());
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
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
