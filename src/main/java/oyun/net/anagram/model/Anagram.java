package oyun.net.anagram.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Anagram {
    
    public static final String TAG = "Anagram";

    private final String mId;
    private final String mAnswer;
    private String mOrigQuestion;
    private final List<Character> mQuestionShuffle;

    private String mQuestion;

    public Anagram(String id,
                   String question,
                   String answer) {
        mId = id;
        mOrigQuestion = question;
        mQuestion = question;
        mAnswer = answer;

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

    public void shuffle() {
        java.util.Collections.shuffle(mQuestionShuffle);

        StringBuilder sb = new StringBuilder();
        for (char c : mQuestionShuffle)
            sb.append(c);

        mQuestion = sb.toString();
    }

    public static Anagram createTest(String anagram) {
        return new Anagram("", new StringBuilder(anagram).reverse().toString(), anagram);
    }
}
