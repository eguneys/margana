package oyun.net.anagram.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Anagram {
    
    public static final String TAG = "Anagram";

    private final String mId;
    private final String mAnswer;
    private final String mQuestion;

    public Anagram(String id,
                   String question,
                   String answer) {
        mId = id;
        mQuestion = question;
        mAnswer = answer;
    }

    public String get(int index) {
        return Character.toString(mQuestion.charAt(index));
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

    public static Anagram createTest(String anagram) {
        return new Anagram("", new StringBuilder(anagram).reverse().toString(), anagram);
    }
}
