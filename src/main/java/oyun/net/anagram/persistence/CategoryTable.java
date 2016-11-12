package oyun.net.anagram.persistence;

import android.provider.BaseColumns;

public interface CategoryTable {
    
    String NAME = "category";

    String COLUMN_ID = BaseColumns._ID;
    String COLUMN_NAME = "name";
    String COLUMN_THEME = "theme";

    String[] PROJECTION = new String[]{ COLUMN_ID, COLUMN_NAME, COLUMN_THEME };

    String CREATE = "CREATE TABLE " + NAME + " ("
        + COLUMN_ID + " TEXT PRIMARY KEY, "
        + COLUMN_NAME + " TEXT NOT NULL, "
        + COLUMN_THEME + " TEXT NOT NULL"
        + ");";
}
