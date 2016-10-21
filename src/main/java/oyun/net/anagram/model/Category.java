package oyun.net.anagram.model;

import java.util.List;
import java.util.ArrayList;

import oyun.net.anagram.model.quiz.Quiz;

public class Category {
    
    public static final String TAG = "Category";

    private static final List<Quiz> defaultQuizzes = new ArrayList<Quiz>();
    public static final Category DEFAULT = new Category("defaultCategory",
                                                                 "1",
                                                                 Theme.anagram,
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
}
