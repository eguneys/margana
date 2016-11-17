package oyun.net.anagram.model.quiz;

import java.util.List;
import java.util.ArrayList;

import oyun.net.anagram.model.Anagram;

public class AnagramQuiz extends Quiz<List<Anagram>> {
    
    private int mTime;
    private int mWordLength;

    private int mNbStars;

    public AnagramQuiz(AnagramQuiz quiz) {
        this(quiz.getTime(), quiz.getWordLength());
    }

    public AnagramQuiz(int time, int wordLength) {
        this(new ArrayList<Anagram>(), time, wordLength, false);
    }    

    /*
     * time required to solve the quiz in seconds
     */
    public AnagramQuiz(List<Anagram> answer, int time, int wordLength, boolean solved) {
        super("", answer, solved);
        mTime = time;
        mWordLength = wordLength;
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

    public List<Anagram> getSolvedAnagrams() {
        List<Anagram> result = new ArrayList<Anagram>();
        List<Anagram> anagrams = getAnagrams();

        for (Anagram anagram : anagrams) {
            if (anagram.isSolved()) {
                result.add(anagram);
            }
        }
                
        return result;
    }

    public int getScore() {
        int result = 0;
        List<Anagram> anagrams = getAnagrams();

        for (Anagram anagram : anagrams) {
            if (anagram.isSolved()) {
                result += anagram.size();
            }
        }
        return result;
    }

    public int getNbSolved() {
        return getSolvedAnagrams().size();
    }

    public int getNbTotal() {
        return getAnagrams().size();
    }

    @Override
    public boolean isSolved() {
        return getNbSolved() == getNbTotal();
    }

    public void addAnagrams(List<Anagram> anagrams) {
        getAnswer().addAll(anagrams);
    }

    public int getTime() {
        return mTime;
    }

    public int getWordLength() {
        return mWordLength;
    }

    public void addStars(int stars) {
        mNbStars += stars;
    }

    public int getStars() {
        return mNbStars;
    }

    public void reset() {
        mNbStars = 0;
        mTime = 0;
        getAnagrams().clear();
    }
}
