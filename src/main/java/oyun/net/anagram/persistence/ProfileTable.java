package oyun.net.anagram.persistence;

import android.provider.BaseColumns;

public interface ProfileTable {
    
    String NAME = "profile";

    String COLUMN_ID = BaseColumns._ID;
    String COLUMN_SCORE = "score";
    String COLUMN_NB_STAR = "nbStar";
    String COLUMN_NB_SOLVED = "nbSolved";

    String[] PROJECTION = new String[]{ COLUMN_ID,
                                        COLUMN_SCORE,
                                        COLUMN_NB_STAR,
                                        COLUMN_NB_SOLVED,
    };

    String CREATE = "CREATE TABLE " + NAME + " ("
        + COLUMN_ID + " STRING PRIMARY KEY, "
        + COLUMN_SCORE + " INTEGER NOT NULL, "
        + COLUMN_NB_STAR + " INTEGER NOT NULL, "
        + COLUMN_NB_SOLVED + " INTEGER NOT NULL"
        + ");";
}
