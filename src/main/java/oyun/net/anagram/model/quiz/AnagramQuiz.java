package oyun.net.anagram.model.quiz;

import java.util.List;
import java.util.ArrayList;

import oyun.net.anagram.model.Anagram;

public class AnagramQuiz extends Quiz<List<Anagram>> {
    
    private int mTime;
    private int mNbWords;

    public AnagramQuiz(int time, int nbWords, boolean solved) {
        this(new ArrayList<Anagram>(), time, nbWords, solved);
    }    

    /*
     * time required to solve the quiz in seconds
     */
    public AnagramQuiz(List<Anagram> answer, int time, int nbWords, boolean solved) {
        super("", answer, solved);
        mTime = time;
        mNbWords = nbWords;
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

    public List<Anagram> getAnagramsWithTimeSpent() {
        List<Anagram> result = new ArrayList<Anagram>();
        List<Anagram> anagrams = getAnagrams();

        for (Anagram anagram : anagrams) {
            if (anagram.getTimeSpent() > 0) {
                result.add(anagram);
            }
        }
                
        return result;
    }

    public void addAnagrams(List<Anagram> anagrams) {
        getAnswer().addAll(anagrams);
    }

    public long getTime() {
        return mTime;
    }

    public int getLength() {
        return mNbWords;
    }
}
