package oyun.net.anagram.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Profile {
    
    public static final String TAG = "Profile";

    private int mNbStar;
    private int mScore;
    private int mNbSolvedWords;

    public Profile(int stars, int score, int solved) {
        mNbStar = stars;
        mScore = score;
        mNbSolvedWords = solved;
    }

    public void addScore(int score) {
        mScore += score;
    }

    public void addStar(int count) {
        mNbStar += count;
    }

    public void addSolvedWords(int words) {
        mNbSolvedWords += words;
    }

    public int getSolvedWords() {
        return mNbSolvedWords;
    }

    public int getScore() {
        return mScore;
    }

    public int getStars() {
        return mNbStar;
    }

}
