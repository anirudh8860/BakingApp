package simplegamer003.bakingapp.bakedish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.anwarshahriar.calligrapher.Calligrapher;
import simplegamer003.bakingapp.R;
import simplegamer003.bakingapp.moshihelper.Steps;

/**
 * Created by anirudhsohil on 16/03/18.
 */

public class DishStepsViewAdapter extends RecyclerView.Adapter<DishStepsViewAdapter.StepsViewHolder>{

    private String[] shortDescription;
    private Steps[] steps;
    private LayoutInflater inflater;
    private Context context;
    private String name;

    public DishStepsViewAdapter(Context context, String[] shortDescription, Steps[] steps, String name){
        this.context = context;
        this.shortDescription = shortDescription;
        this.steps = steps;
        this.name = name;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.card_list_steps_item, parent, false);
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(itemView, "fonts/blackjack.ttf");
        return new StepsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, final int position) {
        holder.shortDescriptionTextView.setText(shortDescription[position]);
        holder.shortDescriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.getResources().getBoolean(R.bool.isTablet)){
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    DishStepDetailFragment fragment = new DishStepDetailFragment();
                    fragment.setArguments(args);
                    FragmentManager fragmentManager = ((DishIngredientAndSteps) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.dish_ingredient_detail_fragment, fragment);
                    fragmentTransaction.commit();
                }
                else {
                    openDishStep(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shortDescription.length;
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder{

        TextView shortDescriptionTextView;

        public StepsViewHolder(View itemView) {
            super(itemView);
            shortDescriptionTextView = (TextView) itemView.findViewById(R.id.steps_item);
        }
    }

    private void openDishStep(int position){
        String[] videoUrl = new String[steps.length];
        String[] stepDescription = new String[steps.length];
        String[] thumbnailUrl = new String[steps.length];

        for (int i = 0; i < steps.length; i++){
            videoUrl[i] = steps[i].getVideoURL();
            stepDescription[i] = steps[i].getDescription();
            thumbnailUrl[i] = steps[i].getThumbnailURL();
            if (videoUrl[i].equals(null) || videoUrl[i].equals(""))
                videoUrl[i] = "0";
            if (thumbnailUrl[i].equals(null) || thumbnailUrl[i].equals(""))
                thumbnailUrl[i] = "0";
        }

        Intent dishStepIntent = new Intent(context, DishStepDetail.class);
        dishStepIntent.putExtra("dish_name", name);
        dishStepIntent.putExtra("position", position);
        dishStepIntent.putExtra("video_url", videoUrl);
        dishStepIntent.putExtra("thumb_url", thumbnailUrl);
        dishStepIntent.putExtra("step_description", stepDescription);
        context.startActivity(dishStepIntent);
    }
}
