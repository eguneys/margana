package oyun.net.anagram.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Anagram {
    
    public static final String TAG = "Anagram";

    private final String mId;
    private final String mAnswer;
    private final String mMeaning;
    private String mOrigQuestion;
    private final List<Character> mQuestionShuffle;

    private String mQuestion;

    private boolean mSolved;

    private int mTimeSpent;

    private String mQuizId;

    public Anagram(String id,
                   String question,
                   String answer,
                   String meaning) {
        mId = id;
        mOrigQuestion = question;
        mQuestion = question;
        mAnswer = answer;
        mMeaning = meaning;

        mQuestionShuffle = new ArrayList<>();
        for (char c : mOrigQuestion.toCharArray()) {
            mQuestionShuffle.add(c);
        }
    }

    public String get(int index) {
        return Character.toString(mQuestion.charAt(index));
    }

    public String getLetter(int index) {
        return get(index);
    }

    public int size() {
        return mQuestion.length();
    }

    public String getId() {
        return mId;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public String getMeaning() {
        return mMeaning;
    }

    public int getTimeSpent() {
        return mTimeSpent;
    }

    public void addTimeSpent(int time) {
        mTimeSpent = mTimeSpent + time;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setQuizId(String id) {
        mQuizId = id;
    }

    public String getQuizId() {
        return mQuizId;
    }

    public void shuffle() {
        java.util.Collections.shuffle(mQuestionShuffle);

        StringBuilder sb = new StringBuilder();
        for (char c : mQuestionShuffle)
            sb.append(c);

        mQuestion = sb.toString();
    }

    public static Anagram createTest(String anagram) {
        return new Anagram("", new StringBuilder(anagram).reverse().toString(), anagram, "");
    }
}
