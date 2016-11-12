package oyun.net.anagram.persistence;

import android.provider.BaseColumns;

public interface QuizTable {
    
    String NAME = "quiz";

    String COLUMN_ID = BaseColumns._ID;
    String FK_CATEGORY = "fk_category";
    String COLUMN_TYPE = "type";
    String COLUMN_TIME = "time";
    String COLUMN_LENGTH = "wLength";
    String COLUMN_SOLVED = "solved";

    String[] PROJECTION = new String[]{ COLUMN_ID,
                                        FK_CATEGORY,
                                        COLUMN_TYPE,
                                        COLUMN_SOLVED,
                                        COLUMN_TIME,
                                        COLUMN_LENGTH
    };

    String CREATE = "CREATE TABLE " + NAME + " ("
        + COLUMN_ID + " INTEGER PRIMARY KEY, "
        + FK_CATEGORY + " REFERENCES "
        + CategoryTable.NAME + "(" + CategoryTable.COLUMN_ID + "), "
        + COLUMN_TYPE + " TEXT NOT NULL, "
        + COLUMN_SOLVED + " TEXT, "
        + COLUMN_TIME + " INTEGER NOT NULL, "
        + COLUMN_LENGTH + " INTEGER NOT NULL "
        + ");";

}