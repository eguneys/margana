package oyun.net.anagram.persistence;

import android.provider.BaseColumns;

public interface CategoryTable {
    
    String NAME = "category";

    String COLUMN_ID = BaseColumns._ID;
    String COLUMN_NAME = "name";
    String COLUMN_THEME = "theme";
    String COLUMN_TIME = "time";
    String COLUMN_WORD_LENGTH = "wordLength";
    String COLUMN_WORD_LIMIT = "wordLimit";

    String[] PROJECTION = new String[]{ COLUMN_ID, COLUMN_NAME, COLUMN_THEME, COLUMN_TIME, COLUMN_WORD_LENGTH, COLUMN_WORD_LIMIT };

    String CREATE = "CREATE TABLE " + NAME + " ("
        + COLUMN_ID + " TEXT PRIMARY KEY, "
        + COLUMN_NAME + " TEXT NOT NULL, "
        + COLUMN_THEME + " TEXT NOT NULL, "
        + COLUMN_TIME + " INTEGER NOT NULL, "
        + COLUMN_WORD_LENGTH + " INTEGER, "
        + COLUMN_WORD_LIMIT + " INTEGER"
        + ");";
}
