package oyun.net.anagram.persistence;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Anagram;
import oyun.net.anagram.model.Theme;
import oyun.net.anagram.model.Category;
import oyun.net.anagram.model.JsonAttributes;
import oyun.net.anagram.model.quiz.Quiz;
import oyun.net.anagram.model.quiz.AnagramQuiz;

public class AnagramDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "AnagramDatabaseHelper";
    private static final String DB_NAME = "anagram";
    private static final String DB_SUFFIX = ".db";
    private static final int DB_VERSION = 1;

    private static AnagramDatabaseHelper mInstance;
    private static List<Category> mCategories;

    private final Resources mResources;

    private AnagramDatabaseHelper(Context context) {
        super(context, DB_NAME + DB_SUFFIX, null, DB_VERSION);

        mResources = context.getResources();
    }

    private static AnagramDatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AnagramDatabaseHelper(context.getApplicationContext());
        }

        return mInstance;
    }

    public static List<Category> getCategories(Context context, boolean fromDb) {
        if (mCategories == null || fromDb) {
            mCategories = loadCategories(context);
        }
        return mCategories;
    }

    public static Category getCategoryWith(Context context, String categoryId) {
        SQLiteDatabase readableDatabase = getReadableDatabase(context);
        Cursor data = readableDatabase
            .query(CategoryTable.NAME, CategoryTable.PROJECTION,
                   CategoryTable.COLUMN_ID + "=?", new String[]{categoryId},
                   null, null, null);
        data.moveToFirst();
        return getCategory(data, readableDatabase);
    }


    private static List<Category> loadCategories(Context context) {
        Cursor data = AnagramDatabaseHelper.getCategoryCursor(context);
        List<Category> tmpCategories = new ArrayList<>(data.getCount());

        final SQLiteDatabase readableDatabase = AnagramDatabaseHelper.getReadableDatabase(context);
        while(data.moveToNext()) {
            final Category category = getCategory(data, readableDatabase);
            tmpCategories.add(category);
        }

        return tmpCategories;
    }

    private static Cursor getCategoryCursor(Context context) {
        SQLiteDatabase readableDatabase = getReadableDatabase(context);
        Cursor data = readableDatabase
            .query(CategoryTable.NAME, CategoryTable.PROJECTION, null, null, null, null, null);
        return data;
    }

    private static Category getCategory(Cursor cursor, SQLiteDatabase readableDatabase) {
        final String id = cursor.getString(0);
        final String name = cursor.getString(1);
        final String themeName = cursor.getString(2);
        final List<Quiz> quizzes = getQuizzes(id, readableDatabase);

        final Theme theme = Theme.valueOf(themeName);

        return new Category(name, id, theme, quizzes);
    }

    private static List<Quiz> getQuizzes(final String categoryId, SQLiteDatabase database) {
        final List<Quiz> quizzes = new ArrayList<>();
        final Cursor cursor = database.query(QuizTable.NAME, QuizTable.PROJECTION,
                                             QuizTable.FK_CATEGORY + " LIKE ?", new String[]{categoryId},
                                             null, null, null);
        while(cursor.moveToNext()) {
            quizzes.add(createQuizDueToType(cursor, database));
        }
        cursor.close();

        return quizzes;
    }

    private static Quiz createQuizDueToType(Cursor cursor, SQLiteDatabase readableDatabase) {
        final String type = cursor.getString(2);
        final boolean solved = getBooleanFromDatabase(cursor.getString(3));
        final int time = cursor.getInt(4);

        switch (type) {
        case JsonAttributes.QuizType.ANAGRAM_QUIZ: {
            return createAnagramQuiz(readableDatabase, time);
        }
        default: {
            throw new IllegalArgumentException("Quiz type " + type + " is not supported");
        }
        }
    }

    private static Quiz createAnagramQuiz(SQLiteDatabase readableDatabase, int time) {
        final List<Anagram> anagrams = getRandomUnsolvedAnagrams(readableDatabase);

        return new AnagramQuiz(anagrams, time, false);
    }


    // http://stackoverflow.com/questions/1253561/sqlite-order-by-rand
    private static List<Anagram> getRandomUnsolvedAnagrams(SQLiteDatabase readableDatabase) {
        final List<Anagram> anagrams = new ArrayList<>();

        String orderBy = "";
        String limit = "10";
        String queryString = "SELECT * FROM " + AnagramTable.NAME + " WHERE "
            + AnagramTable.COLUMN_ID + " IN "
            + "(SELECT " + AnagramTable.COLUMN_ID + " FROM "
            + AnagramTable.NAME + " ORDER BY RANDOM() LIMIT ?" +  ")"
            + "AND " + AnagramTable.FK_QUIZ + " IS NULL";
        // queryString = "SELECT * FROM anagram LIMIT ?";
        final Cursor cursor = readableDatabase
            .rawQuery(queryString,
                      new String[] { limit });

        while (cursor.moveToNext()) {
            final Anagram anagram = getAnagram(cursor);
            anagrams.add(anagram);
        }

        return anagrams;
    }

    private static Anagram getAnagram(Cursor cursor) {
        final String id = cursor.getString(0);
        final String question = cursor.getString(2);
        final String answer = cursor.getString(3);
        final String meaning = cursor.getString(4);

        return new Anagram(id, question, answer);
    }

    private static boolean getBooleanFromDatabase(String isSolved) {
        // json stores booleans as true/false, whereas SQLite stores them as 0 1
        return isSolved != null && isSolved.length() == 1 && Integer.valueOf(isSolved) == 1;
    }

    private static SQLiteDatabase getReadableDatabase(Context context) {
        return getInstance(context).getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //
        db.execSQL(CategoryTable.CREATE);
        db.execSQL(QuizTable.CREATE);
        db.execSQL(AnagramTable.CREATE);
        preFillDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 
    }

    private void preFillDatabase(SQLiteDatabase db) {
        try {
            db.beginTransaction();

            try {
                fillCategoriesAndQuizzes(db);
                fillAnagrams(db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            
        } catch (IOException | JSONException e) {
            Log.e(TAG, "preFillDatabase", e);
        }
    }

    private String readAnagramsFromResources() throws IOException {
        InputStream rawAnagrams = mResources.openRawResource(R.raw.anagrams);
        return readJson(rawAnagrams);
    }

    private String readCategoriesFromResources() throws IOException {
        InputStream rawCategories = mResources.openRawResource(R.raw.categories);
        return readJson(rawCategories);
    }

    private String readJson(InputStream jsonStream) throws IOException {
        StringBuilder resJson = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(jsonStream));
        String line;
        while ((line = reader.readLine()) != null) {
            resJson.append(line);
        }
        return resJson.toString();        
    }

    private void fillAnagrams(SQLiteDatabase db) throws JSONException, IOException {
        ContentValues values = new ContentValues(); // reuse
        JSONArray jsonArray = new JSONArray(readAnagramsFromResources());
        JSONObject anagram;
        for (int i = 0; i < jsonArray.length(); i++) {
            anagram = jsonArray.getJSONObject(i);
            fillAnagram(db, values, anagram);
        }
    }

    private void fillCategoriesAndQuizzes(SQLiteDatabase db) throws JSONException, IOException {
        ContentValues values = new ContentValues(); // reuse
        JSONArray jsonArray = new JSONArray(readCategoriesFromResources());
        JSONObject category;
        for (int i = 0; i < jsonArray.length(); i++) {
            category = jsonArray.getJSONObject(i);
            final String categoryId = category.getString(JsonAttributes.ID);
            fillCategory(db, values, category, categoryId);
            final JSONArray quizzes = category.getJSONArray(JsonAttributes.QUIZZES);
            fillQuizzesForCategory(db, values, quizzes, categoryId);
        }
    }

    private void fillAnagram(SQLiteDatabase db,
                              ContentValues values,
                              JSONObject anagram) throws JSONException {

        String word = anagram.getString(JsonAttributes.WORD);
        String question = word;
        String answer = word;
        String anagramId = word;

        values.clear();
        values.put(AnagramTable.COLUMN_ID, anagramId);
        values.put(AnagramTable.COLUMN_QUESTION, question);
        values.put(AnagramTable.COLUMN_ANSWER, answer);
        values.put(AnagramTable.COLUMN_MEANING, anagram.getString(JsonAttributes.MEANING));
        db.insert(AnagramTable.NAME, null, values);
    }

    private void fillCategory(SQLiteDatabase db, ContentValues values, JSONObject category,
                              String categoryId) throws JSONException {
        values.clear();
        values.put(CategoryTable.COLUMN_ID, categoryId);
        values.put(CategoryTable.COLUMN_NAME, category.getString(JsonAttributes.NAME));
        values.put(CategoryTable.COLUMN_THEME, category.getString(JsonAttributes.THEME));
        db.insert(CategoryTable.NAME, null, values);
    }

    private void fillQuizzesForCategory(SQLiteDatabase db, ContentValues values, JSONArray quizzes,
                                        String categoryId) throws JSONException {
        JSONObject quiz;
        for (int i = 0; i < quizzes.length(); i++) {
            quiz = quizzes.getJSONObject(i);
            values.clear();
            values.put(QuizTable.FK_CATEGORY, categoryId);
            values.put(QuizTable.COLUMN_TYPE, quiz.getString(JsonAttributes.TYPE));
            values.put(QuizTable.COLUMN_TIME, quiz.getString(JsonAttributes.TIME));
            db.insert(QuizTable.NAME, null, values);
        }
    }
}
