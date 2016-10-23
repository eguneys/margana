package oyun.net.anagram.model.quiz;

public class AnagramQuiz extends Quiz<String> {
    
    public AnagramQuiz(String question, String answer, boolean solved) {
        super(question, answer, solved);
    }

    public String get(int idx) {
        return Character
            .toString(getQuestion().charAt(idx));
    }

    public int size() {
        return getQuestion().length();
    }
}
