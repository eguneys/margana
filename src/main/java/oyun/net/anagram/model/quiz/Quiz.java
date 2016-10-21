package oyun.net.anagram.model.quiz;

public abstract class Quiz<A> {
    
    private static final String TAG = "Quiz";

    private final String mQuestion;
    private final A mAnswer;
    private boolean mSolved ;

    protected Quiz(String question, A answer, boolean solved) {
        mQuestion = question;
        mAnswer = answer;
        mSolved = solved;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public A getAnswer() {
        return mAnswer;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public int getId() {
        return getQuestion().hashCode();
    }
}
