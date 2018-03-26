package simplegamer003.bakingapp.bakedish;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import me.anwarshahriar.calligrapher.Calligrapher;
import simplegamer003.bakingapp.R;

public class DishStepDetail extends AppCompatActivity {

    String[] videoUrl, stepDescription, thumbnailUrl;
    int position;
    String dishName;
    FrameLayout dishStepFragment;
    DishStepDetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_step_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        stepDescription = intent.getStringArrayExtra("step_description");
        videoUrl = intent.getStringArrayExtra("video_url");
        thumbnailUrl = intent.getStringArrayExtra("thumb_url");
        position = intent.getIntExtra("position", 0);
        dishName = intent.getStringExtra("dish_name");
        dishStepFragment = (FrameLayout) findViewById(R.id.dish_step_fragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        if (getSupportFragmentManager().findFragmentById(R.id.dish_step_fragment) != null){
            fragment = (DishStepDetailFragment) fragmentManager.findFragmentById(R.id.dish_step_fragment);
            fragmentTransaction.detach(fragment);
            fragmentTransaction.attach(fragment);
        }
        else {
            fragment = new DishStepDetailFragment();
            fragmentTransaction.add(R.id.dish_step_fragment, fragment);
            fragmentTransaction.commit();
        }

        getSupportActionBar().setTitle(dishName + " Steps");

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/blackjack.ttf", true);

        Bundle data = new Bundle();
        Fragment dishStepDetailFragment = new DishStepDetailFragment();
        data.putStringArray("video_urls", videoUrl);
        data.putStringArray("steps_desc", stepDescription);
        data.putStringArray("thumb_url", thumbnailUrl);
        data.putInt("position", position);
        dishStepDetailFragment.setArguments(data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
