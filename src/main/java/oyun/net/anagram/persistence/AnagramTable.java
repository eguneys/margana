package oyun.net.anagram.persistence;

import android.provider.BaseColumns;

public interface AnagramTable {
    
    String NAME = "anagram";

    String COLUMN_ID = BaseColumns._ID;
    String FK_QUIZ = "fk_quiz";
    String COLUMN_QUESTION = "question";
    String COLUMN_ANSWER = "answer";
    String COLUMN_MEANING = "meaning";

    String[] PROJECTION = new String[]{ COLUMN_ID,
                                        FK_QUIZ,
                                        COLUMN_QUESTION,
                                        COLUMN_ANSWER,
                                        COLUMN_MEANING
    };

    String CREATE = "CREATE TABLE " + NAME + " ("
        + COLUMN_ID + " STRING PRIMARY KEY, "
        + FK_QUIZ + " REFERENCES "
        + QuizTable.NAME + "(" + QuizTable.COLUMN_ID + "), "
        + COLUMN_QUESTION + " TEXT NOT NULL, "
        + COLUMN_ANSWER + " TEXT NOT NULL, "
        + COLUMN_MEANING + " TEXT NOT NULL"
        + ");";
}
