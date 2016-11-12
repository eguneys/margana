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
        new ArrayList<Quiz>(Arrays.asList(new AnagramQuiz(defaultAnagrams, 120, false)));

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


    public Category(String name,
                    String id,
                    Theme theme,
                    List<Quiz> quizzes) {
        this(name, id, theme, quizzes, false);
    }

    public Category(String name,
                    String id,
                    Theme theme,
                    List<Quiz> quizzes,
                    boolean solved) {
        mName = name;
        mId = id;
        mTheme = theme;
        mQuizzes = quizzes;
        mSolved = solved;
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
