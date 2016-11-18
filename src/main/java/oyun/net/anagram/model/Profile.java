package oyun.net.anagram.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Profile {
    
    public static final String TAG = "Profile";

    private int mNbStar;
    private int mScore;

    public Profile(int stars, int score) {
        mNbStar = stars;
        mScore = score;
    }

    public void addScore(int score) {
        mScore += score;
    }

    public void addStar(int count) {
        mNbStar += count;
    }

    public int getScore() {
        return mScore;
    }

    public int getStars() {
        return mNbStar;
    }

}
