package oyun.net.anagram.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import oyun.net.anagram.model.quiz.Quiz;
import oyun.net.anagram.model.quiz.AnagramQuiz;

public class Category {
    
    public static final String TAG = "Category";

    public static final int DEFAULT_WORD_LIMIT = 10;

    private final String mName;
    private final String mId;
    private final Theme mTheme;

    private int mTime;
    private int mWordLimit;
    private int mWordLength;

    private int mNbUnsolved;
    private int mNbSolved;

    private int mNbStars;

    // public Category(String name,
    //                 String id,
    //                 Theme theme) {
    //     this(name, id, theme, 120, DEFAULT_WORD_LIMIT, 0, 0, 0);
    // }

    public Category(String name,
                    String id,
                    Theme theme,
                    int time,
                    int wordLength,
                    int wordLimit,
                    int nbSolved,
                    int nbUnsolved,
                    int nbStars) {
        mName = name;
        mId = id;
        mTheme = theme;
        mTime = time;
        mWordLimit = wordLimit;
        mWordLength = wordLength;
        mNbSolved = nbSolved;
        mNbUnsolved = nbUnsolved;
        mNbStars = nbStars;
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

    public int getTime() {
        return mTime;
    }

    public int getWordLimit() {
        return mWordLimit;
    }

    public int getWordLength() {
        return mWordLength;
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

    public int getStars() {
        return mNbStars;
    }

    // private static final List<Anagram> defaultAnagrams =
    //     new ArrayList<Anagram>(Arrays.asList(Anagram.createTest("test1"),
    //                                          Anagram.createTest("test12"),
    //                                          Anagram.createTest("test123"),
    //                                          Anagram.createTest("eflatun"),
    //                                          Anagram.createTest("kabarma"),
    //                                          Anagram.createTest("ekonomi"),
    //                                          Anagram.createTest("anagram"),
    //                                          Anagram.createTest("kablocu")));

    // private static final List<Quiz> defaultQuizzes =
    //     new ArrayList<Quiz>(Arrays.asList(new AnagramQuiz(defaultAnagrams, 120, 7, false)));

    // public static final Category DEFAULT = new Category("defaultCategory",
    //                                                     "1",
    //                                                     Theme.anagram,
    //                                                     defaultQuizzes,
    //                                                     false);

    // public static final Category RED = new Category("Red",
    //                                                 "2",
    //                                                 Theme.red,
    //                                                 defaultQuizzes,
    //                                                 false);

    // public static final Category FOUR = new Category("four",
    //                                                  "4",
    //                                                  Theme.red,
    //                                                  defaultQuizzes,
    //                                                  false);

    // public static final Category SEVEN = new Category("seven",
    //                                                   "7",
    //                                                   Theme.red,
    //                                                   defaultQuizzes,
    //                                                   false);
}
