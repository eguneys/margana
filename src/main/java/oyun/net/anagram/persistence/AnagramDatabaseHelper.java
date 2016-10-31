package oyun.net.anagram.persistence;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;

import oyun.net.anagram.model.Theme;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.quiz.Quiz;


public class AnagramDatabaseHelper {

    private AnagramDatabaseHelper(Context context) {
        
    }

    public static List<Category> getCategories(Context context, boolean fromDb) {
        return new ArrayList<Category>(Arrays.asList(Category.SEVEN,
                                                     Category.FOUR));
    }

    public static Category getCategoryWith(Context context, String categoryId) {
        return Category.RED;
    }

}
