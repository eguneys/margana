package oyun.net.anagram.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import oyun.net.anagram.model.quiz.Quiz;
import oyun.net.anagram.model.quiz.AnagramQuiz;

public class Category {
    
    public static final String TAG = "Category";

    private static final List<Anagram> defaultAnagrams =
        new ArrayList<Anagram>(Arrays.asList(Anagram.createTest("test1"),
                                             Anagram.createTest("test12"),
                                             Anagram.createTest("test123"),
                                             Anagram.createTest("eflatun"),
                                             Anagram.createTest("kabarma"),
                                             Anagram.createTest("ekonomi"),
                                             Anagram.createTest("anagram"),
                                             Anagram.createTest("kablocu")));

    private static final List<Quiz> defaultQuizzes =
        new ArrayList<Quiz>(Arrays.asList(new AnagramQuiz(defaultAnagrams, 120, 7, false)));

    public static final Category DEFAULT = new Category("defaultCategory",
                                                        "1",
                                                        Theme.anagram,
                                                        defaultQuizzes,
                                                        false);

    public static final Category RED = new Category("Red",
                                                    "2",
                                                    Theme.red,
                                                    defaultQuizzes,
                                                    false);

    public static final Category FOUR = new Category("four",
                                                     "4",
                                                     Theme.red,
                                                     defaultQuizzes,
                                                     false);

    public static final Category SEVEN = new Category("seven",
                                                      "7",
                                                      Theme.red,
                                                      defaultQuizzes,
                                                      false);


    private final String mName;
    private final String mId;
    private final Theme mTheme;
    private final List<Quiz> mQuizzes;
    private boolean mSolved;

    private int mWordLength;

    private int mNbUnsolved;
    private int mNbSolved;

    public Category(String name,
                    String id,
                    Theme theme,
                    List<Quiz> quizzes,
                    boolean solved) {
        this(name, id, theme, quizzes, solved, -1, 0, 0);
    }

    public Category(String name,
                    String id,
                    Theme theme,
                    List<Quiz> quizzes,
                    int wordLength,
                    int nbSolved,
                    int nbUnsolved) {
        this(name, id, theme, quizzes, false, wordLength, nbSolved, nbUnsolved);
    }

    public Category(String name,
                    String id,
                    Theme theme,
                    List<Quiz> quizzes,
                    boolean solved,
                    int wordLength,
                    int nbSolved,
                    int nbUnsolved) {
        mName = name;
        mId = id;
        mTheme = theme;
        mQuizzes = quizzes;
        mSolved = solved;
        mWordLength = wordLength;
        mNbSolved = nbSolved;
        mNbUnsolved = nbUnsolved;
    }

    public String getName() {
        return mName;
    }

    public String getId() {
        return mId;
    }

    public Theme getTheme() {
        return mTheme;
    }

    public List<Quiz> getQuizzes() {
        return mQuizzes;
    }

    public int getNbUnsolved() {
        return mNbUnsolved;
    }

    public int getNbSolved() {
        return mNbSolved;
    }
    public int getNbTotal() {
        return mNbUnsolved + mNbSolved;
    }

    public Quiz getRecentQuiz() {
        return mQuizzes.get(mQuizzes.size() - 1);
    }

    // public Quiz getFirstQuiz() {
    //     return getQuizzes().get(0);
    // }


    public void addNewQuiz() {
        AnagramQuiz quiz = new AnagramQuiz((AnagramQuiz)getRecentQuiz());
        addQuiz(quiz);
    }

    public void addQuiz(Quiz quiz) {
        mQuizzes.add(quiz);
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        this.mSolved = solved;
    }

    public int getFirstUnsolvedQuizPosition() {
        if (mQuizzes == null) {
            return -1;
        }

        for (int i = 0; i < mQuizzes.size(); i++) {
            if (!mQuizzes.get(i).isSolved()) {
                return i;
            }
        }
        return mQuizzes.size();
    }
}
