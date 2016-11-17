package oyun.net.anagram.persistence;

import android.provider.BaseColumns;

public interface ProfileTable {
    
    String NAME = "profile";

    String COLUMN_ID = BaseColumns._ID;
    String COLUMN_SCORE = "score";
    String COLUMN_NB_STAR = "nbStar";

    String[] PROJECTION = new String[]{ COLUMN_ID,
                                        COLUMN_SCORE,
                                        COLUMN_NB_STAR
    };

    String CREATE = "CREATE TABLE " + NAME + " ("
        + COLUMN_ID + " STRING PRIMARY KEY, "
        + COLUMN_SCORE + " INTEGER NOT NULL, "
        + COLUMN_NB_STAR + " INTEGER NOT NULL"
        + ");";
}
