package oyun.net.anagram.model.quiz;

import java.util.List;

import oyun.net.anagram.model.Anagram;

public class AnagramQuiz extends Quiz<List<Anagram>> {
    
    private int mTime;

    /*
     * time required to solve the quiz in seconds
     */
    public AnagramQuiz(List<Anagram> answer, int time, boolean solved) {
        super("", answer, solved);
        mTime = time;
    }

    public Anagram get(int idx) {
        return getAnagrams().get(idx);
    }

    public int size() {
        return getAnagrams().size();
    }

    public List<Anagram> getAnagrams() {
        return getAnswer();
    }

    public long getTime() {
        return mTime;
    }
}
