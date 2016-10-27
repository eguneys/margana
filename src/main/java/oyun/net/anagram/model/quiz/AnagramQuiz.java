package oyun.net.anagram.model.quiz;

import java.util.List;

import oyun.net.anagram.model.Anagram;

public class AnagramQuiz extends Quiz<List<Anagram>> {
    
    public AnagramQuiz(List<Anagram> answer, boolean solved) {
        super("", answer, solved);
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
}
