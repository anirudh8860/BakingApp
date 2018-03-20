package simplegamer003.bakingapp.moshihelper;

import java.io.Serializable;

/**
 * Created by anirudhsohil on 12/03/18.
 */

public class Steps implements Serializable{
    int id;
    String shortDescription;
    String description;
    String videoURL;
    String thumbnailURL;

    public int getIdStep() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    @Override
    public String toString() {
        return getIdStep() + " " + getShortDescription() + " " + getVideoURL() + " " +getDescription() + " " +getThumbnailURL();
    }
}
