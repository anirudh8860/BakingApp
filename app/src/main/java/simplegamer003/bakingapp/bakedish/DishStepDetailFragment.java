package simplegamer003.bakingapp.bakedish;


import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import simplegamer003.bakingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DishStepDetailFragment extends Fragment {

    private TextView stepDesc;
    private String[] videoUrl, stepDescription;
    private int position, finalposition;
    private DishStepDetail dishStepDetail;
    private Button prev, next;
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView exoPlayerView;
    private long playbackPos;
    private int windowIndex;
    private boolean playWhenReady;
    private ComponentListener componentListener;

    public DishStepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View stepDescView = inflater.inflate(R.layout.fragment_dish_step_detail, container, false);
        /*
        Bundle bundle = this.getArguments();
        if (bundle != null){
            videoUrl = bundle.getStringArray("video_urls");
            stepDescription = bundle.getStringArray("steps_desc");
            position = bundle.getInt("position");
        }
        */
        dishStepDetail= (DishStepDetail) getActivity();
        stepDesc = stepDescView.findViewById(R.id.step_desc);
        prev = stepDescView.findViewById(R.id.prev);
        next = stepDescView.findViewById(R.id.next);
        exoPlayerView = stepDescView.findViewById(R.id.step_video_player);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                position = dishStepDetail.position;
                videoUrl = dishStepDetail.videoUrl;
                stepDescription = dishStepDetail.stepDescription;
                finalposition = stepDescription.length - 1;
                checkForFirstPosition(position);
                checkForFinalPosition(position);
                stepDesc.setText(stepDescription[position]);
                componentListener = new ComponentListener();
                showOrHideVideo(videoUrl[position]);
            }
        }, 100);

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

        return stepDescView;
    }

    private void initializePlayer(Uri uri) {
        if (exoPlayer == null){
            assignResToPlayer(uri);
        }
        else{
            releasePlayer();
            assignResToPlayer(uri);
        }
    }

    private void assignResToPlayer(Uri uri) {
        Context context = getActivity().getApplicationContext();
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
                .createMediaSource(uri
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
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){

        }
    }

    private void getPrev(){
        if (position != 0)
            position --;

        checkForFirstPosition(position);
        checkForFinalPosition(position);

        stepDesc.setText(stepDescription[position]);
        showOrHideVideo(videoUrl[position]);
    }

    private void getNext() {
        if (position != finalposition)
            position++;

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


    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showOrHideVideo(videoUrl[position]);
            }
        }, 100);
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (exoPlayer == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showOrHideVideo(videoUrl[position]);
                }
            }, 100);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
