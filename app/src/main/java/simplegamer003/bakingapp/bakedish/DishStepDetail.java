package simplegamer003.bakingapp.bakedish;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import simplegamer003.bakingapp.R;

public class DishStepDetail extends AppCompatActivity {

    String[] videoUrl, stepDescription;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_step_detail);

        Intent intent = getIntent();
        stepDescription = intent.getStringArrayExtra("step_description");
        videoUrl = intent.getStringArrayExtra("video_url");
        position = intent.getIntExtra("position", 0);
        Log.d("Pos", String.valueOf(position));
        for (int i = 0; i < stepDescription.length; i++){
            Log.d("Video Dish " + i, videoUrl[i]);
            Log.d("Step Description Dish " + i, stepDescription[i]);
        }
        Bundle data = new Bundle();
        Fragment dishStepDetailFragment = new DishStepDetailFragment();
        data.putStringArray("video_urls", videoUrl);
        data.putStringArray("steps_desc", stepDescription);
        data.putInt("position", position);
        dishStepDetailFragment.setArguments(data);
    }
}
