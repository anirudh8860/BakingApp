package simplegamer003.bakingapp.bakedish;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import me.anwarshahriar.calligrapher.Calligrapher;
import simplegamer003.bakingapp.R;
import simplegamer003.bakingapp.moshihelper.Dish;
import simplegamer003.bakingapp.moshihelper.Ingredients;

/**
 * A simple {@link Fragment} subclass.
 */
public class DishStepDetailFragment extends Fragment {

    private TextView stepDesc;
    private String[] videoUrl, stepDescription;
    private int position, finalposition, windowIndex, resumeWindow;
    private DishStepDetail dishStepDetail;
    private DishIngredientAndSteps ingredientAndSteps;
    private Button prev, next, retryButton;
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView exoPlayerView;
    private long playbackPos, resumePosition;
    private boolean playWhenReady, isTablet;
    private ComponentListener componentListener;
    private View stepDescView;
    private RelativeLayout prevNextFragment;
    private static final String positionKey = "position";
    private static final String STATE_RESUME_WINDOW = "resumeWindow";
    private static final String STATE_RESUME_POSITION = "resumePosition";
    private static final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private boolean exoPlayerFullscreen = false;
    private Dialog fullScreenDialog;
    private ImageButton fullScreenBtn;
    private FrameLayout frameLayout;
    private LayoutInflater inflater;
    private int orientation;
    private Context context;

    public DishStepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        frameLayout = new FrameLayout(getActivity());
        stepDescView = inflater.inflate(R.layout.fragment_dish_step_detail, container, false);
        context = getActivity().getApplicationContext();
        isTablet = context.getResources().getBoolean(R.bool.isTablet);

        stepDesc = stepDescView.findViewById(R.id.step_desc);
        prev = stepDescView.findViewById(R.id.prev);
        next = stepDescView.findViewById(R.id.next);
        exoPlayerView = stepDescView.findViewById(R.id.step_video_player);
        prevNextFragment = stepDescView.findViewById(R.id.prev_next_layout);
        orientation = getActivity().getResources().getConfiguration().orientation;
        retryButton = stepDescView.findViewById(R.id.check_conn_btn_frag);

        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(stepDescView, "fonts/blackjack.ttf");

        setView(savedInstanceState);

        prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPrev();
                }
            });

        next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getNext();
                }
            });

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setView(savedInstanceState);
            }
        });

        return stepDescView;
    }

    private void setView(Bundle savedInstanceState){
        if (isNetworkAvailable()){
            setVisibilityOfConnected();
            showStepAndVideo(savedInstanceState);
        }
        else {
            setVisibilityOfNotConnected();
            stepDesc.setText(context.getString(R.string.not_connected_text));
        }

        if (isTablet){
            prev.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        }
        else {
            prev.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
        }
    }

    private void setVisibilityOfNotConnected() {
        retryButton.setVisibility(View.VISIBLE);
        exoPlayerView.setVisibility(View.GONE);
    }

    private void setVisibilityOfConnected(){
        retryButton.setVisibility(View.GONE);
        exoPlayerView.setVisibility(View.VISIBLE);
    }

    private void showStepAndVideo(final Bundle savedInstanceState){
        if (isTablet){
            ingredientAndSteps = (DishIngredientAndSteps) getActivity();
            prev.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
            Bundle bundle = getArguments();
            position = bundle.getInt("position");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (savedInstanceState != null) {
                        position = savedInstanceState.getInt(positionKey);
                        resumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
                        resumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
                        exoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
                    }
                    videoUrl = ingredientAndSteps.videoUrl;
                    stepDescription = ingredientAndSteps.stepDescription;
                    finalposition = stepDescription.length - 1;
                    checkForFirstPosition(position);
                    checkForFinalPosition(position);
                    stepDesc.setText(stepDescription[position]);
                    componentListener = new ComponentListener();
                    showOrHideVideo(videoUrl[position]);
                    initFullScreenDialog();
                    initFullScreenButton();
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                        openFullScreenDialog();
                }
            }, 100);
        }
        else {
            dishStepDetail = (DishStepDetail) getActivity();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (savedInstanceState != null) {
                        position = savedInstanceState.getInt(positionKey);
                        resumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
                        resumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
                        exoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
                    } else {
                        position = dishStepDetail.position;
                    }
                    videoUrl = dishStepDetail.videoUrl;
                    stepDescription = dishStepDetail.stepDescription;
                    finalposition = stepDescription.length - 1;
                    checkForFirstPosition(position);
                    checkForFinalPosition(position);
                    stepDesc.setText(stepDescription[position]);
                    componentListener = new ComponentListener();
                    showOrHideVideo(videoUrl[position]);
                    initFullScreenDialog();
                    initFullScreenButton();
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                        openFullScreenDialog();
                }
            }, 100);
        }
    }

    private void initializePlayer(Uri uri) {
        if (exoPlayer == null) {
            assignResToPlayer(uri);
        } else {
            releasePlayer();
            assignResToPlayer(uri);
        }
    }

    private void initFullScreenDialog() {

        fullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (exoPlayerFullscreen) {
                    closeFullScreenDialog();
                }
                super.onBackPressed();
            }
        };
    }


    private void openFullScreenDialog() {

        ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
        fullScreenDialog.addContentView(exoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        fullScreenBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.fullscreen_exit_white));
        exoPlayerFullscreen = true;
        fullScreenDialog.show();
    }


    private void closeFullScreenDialog() {
        ViewGroup container = (ViewGroup) exoPlayerView.getParent();
        container.removeView(exoPlayerView);
        LinearLayout layout = (LinearLayout) stepDescView.findViewById(R.id.player_linear_layout);
        layout.removeAllViews();

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        exoPlayerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height));
        layout.addView(exoPlayerView);

        stepDesc.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(stepDesc);

        exoPlayerFullscreen = false;
        fullScreenDialog.dismiss();
        fullScreenBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.fullscreen_white));
    }


    private void initFullScreenButton() {

        PlaybackControlView controlView = exoPlayerView.findViewById(R.id.exo_controller);
        fullScreenBtn = controlView.findViewById(R.id.exo_fullscreen_button);
        fullScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!exoPlayerFullscreen)
                    openFullScreenDialog();
                else {
                    closeFullScreenDialog();
                }
            }
        });
    }

    private void assignResToPlayer(Uri uri) {
        Context context = getContext();
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        RenderersFactory renderersFactory = new DefaultRenderersFactory(context);
        exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory
                , trackSelector
                , loadControl);
        exoPlayerView.setPlayer(exoPlayer);
        DefaultHttpDataSourceFactory dataSource = new DefaultHttpDataSourceFactory(
                Util.getUserAgent(context, getString(R.string.app_name)));
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSource)
                .createMediaSource(
                        uri
                        , null
                        , null);
        exoPlayer.addListener(componentListener);
        exoPlayer.addVideoDebugListener(componentListener);
        exoPlayer.addAudioDebugListener(componentListener);
        exoPlayer.prepare(mediaSource, false, false);
        exoPlayer.setPlayWhenReady(playWhenReady);
        exoPlayer.seekTo(windowIndex, playbackPos);

    }

    private void releasePlayer(){
        if (exoPlayer != null){
            playbackPos = exoPlayer.getCurrentPosition();
            windowIndex = exoPlayer.getCurrentWindowIndex();
            playWhenReady = exoPlayer.getPlayWhenReady();
            exoPlayer.removeListener(componentListener);
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(positionKey, position);
    }

    private void getPrev(){
        if (position != 0)
            position -= 1;

        checkForFirstPosition(position);
        checkForFinalPosition(position);

        stepDesc.setText(stepDescription[position]);
        showOrHideVideo(videoUrl[position]);
    }

    private void getNext() {
        if (position != finalposition)
            position += 1;

        checkForFirstPosition(position);
        checkForFinalPosition(position);

        stepDesc.setText(stepDescription[position]);
        showOrHideVideo(videoUrl[position]);
    }

    private void checkForFirstPosition(int position){
        if (position == 0)
            prev.setEnabled(false);
        else
            prev.setEnabled(true);
    }

    private void checkForFinalPosition(int position){
        if (position == finalposition)
            next.setEnabled(false);
        else
            next.setEnabled(true);
    }

    private void showOrHideVideo(String videoUrlStr){
        boolean noVideo = checkForNoVideo(videoUrlStr);
        if (noVideo)
            exoPlayerView.setVisibility(View.GONE);
        else {
            exoPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(videoUrlStr));
        }
    }

    private boolean checkForNoVideo(String videoUrlStr) {
        if (videoUrlStr.equals("0"))
            return true;
        else
            return false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (isNetworkAvailable()) {
            if (isTablet){
                prev.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initFullScreenButton();
                    initFullScreenDialog();
                    showOrHideVideo(videoUrl[position]);

                    if (exoPlayerFullscreen) {
                        ((ViewGroup) exoPlayerView.getParent()).removeView(exoPlayerView);
                        fullScreenDialog.addContentView(exoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        fullScreenBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.fullscreen_exit_white));
                        fullScreenDialog.show();
                    }
                }
            }, 100);
        }
        else {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayerView != null && exoPlayerView.getPlayer() != null) {
            resumeWindow = exoPlayerView.getPlayer().getCurrentWindowIndex();
            resumePosition = Math.max(0, exoPlayerView.getPlayer().getContentPosition());
            releasePlayer();
        }

        if (fullScreenDialog != null)
            fullScreenDialog.dismiss();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (exoPlayer == null) {
            if (isNetworkAvailable()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showOrHideVideo(videoUrl[position]);
                    }
                }, 100);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
