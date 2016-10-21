package oyun.net.anagram.persistence;

import android.content.Context;

import oyun.net.anagram.model.Theme;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.quiz.Quiz;


public class AnagramDatabaseHelper {

    private AnagramDatabaseHelper(Context context) {
        
    }

    public static Category getCategoryWith(Context context, String categoryId) {
        return Category.DEFAULT;
    }

}
