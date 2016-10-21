package oyun.net.anagram.model.quiz;

public abstract class Quiz<A> {
    
    private static final String TAG = "Quiz";

    protected Quiz(String question, A answer, boolean solved) {
        mQuestion = question;
        mAnswer = answer;
        mSolved = solved;
    }
}
