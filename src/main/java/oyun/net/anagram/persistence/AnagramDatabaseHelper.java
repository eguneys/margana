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
import oyun.net.anagram.model.Profile;
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
    private static Profile mProfile;

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

    public static void initDb(Context context) {
        SQLiteDatabase readableDatabase = getReadableDatabase(context);
    }

    public static Profile getProfile(Context context, boolean fromDb) {
        // load categories first?
        getCategories(context, false);

        if (mProfile == null || fromDb) {
            mProfile = loadProfile(context);
        }
        return mProfile;
    }

    public static List<Category> getCategories(Context context, boolean fromDb) {
        if (mCategories == null || fromDb) {
            mCategories = loadCategories(context);
        }
        return mCategories;
    }

    public static Category getCategoryById(Context context, String categoryId) {
        SQLiteDatabase readableDatabase = getReadableDatabase(context);
        Cursor data = readableDatabase
            .query(CategoryTable.NAME, CategoryTable.PROJECTION,
                   CategoryTable.COLUMN_ID + "=?", new String[]{categoryId},
                   null, null, null);
        data.moveToFirst();
        return getCategory(data, readableDatabase);
    }

    private static Profile loadProfile(Context context) {
        Cursor data = AnagramDatabaseHelper.getProfileCursor(context);
        final SQLiteDatabase readableDatabase = AnagramDatabaseHelper.getReadableDatabase(context);
        return getProfile(data, readableDatabase);
    }

    private static Cursor getProfileCursor(Context context) {
        SQLiteDatabase readableDatabase = getReadableDatabase(context);
        Cursor data = readableDatabase
            .query(ProfileTable.NAME, ProfileTable.PROJECTION, null, null, null, null, null);
        return data;
    }

    private static Profile getProfile(Cursor cursor, SQLiteDatabase readableDatabase) {
        int stars = getTotalStars(readableDatabase);        
        return new Profile(stars, 0);
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

    // private static Category getCategoryWithRandomAnagrams(Cursor cursor, SQLiteDatabase readableDatabase) {
    //     Category cat = getCategory(cursor, readableDatabase);
    //     AnagramQuiz quiz = ((AnagramQuiz)cat.getFirstQuiz());
    //     quiz.addAnagrams(getRandomUnsolvedAnagrams(readableDatabase, quiz.getLength(), 10));

    //     return cat;
    // }

    private static Category getCategory(Cursor cursor, SQLiteDatabase readableDatabase) {
        final String id = cursor.getString(0);
        final String name = cursor.getString(1);
        final String themeName = cursor.getString(2);
        final int time = cursor.getInt(3);
        final int wordLength = cursor.getInt(4);
        final int wordLimit = cursor.getInt(5);
        // final List<Quiz> quizzes = getQuizzes(id, readableDatabase);
        // final Quiz quiz = (id, readableDatabase);
        final int nbUnsolved = getUnsolvedAnagramsCountByLength(readableDatabase, wordLength);
        final int nbSolved = getSolvedAnagramsCountByLength(readableDatabase, wordLength);
        final int nbStars = getStarsForCategory(id, readableDatabase);

        final Theme theme = Theme.valueOf(themeName);
        Log.e("YYY getCat", id + " " + nbStars);

        return new Category(name, id, theme, time, wordLength, wordLimit, nbSolved, nbUnsolved, nbStars);
    }

    private static int getTotalStars(SQLiteDatabase database) {
        int stars = 0;
        for (Category category : mCategories) {
            stars += getStarsForCategory(category.getId(), database);
        }
        return stars;
    }

    private static int getStarsForCategory(final String categoryId, SQLiteDatabase database) {
        final String queryString = "SELECT SUM(" + QuizTable.COLUMN_NB_STAR + ") FROM " + QuizTable.NAME
            + " WHERE " + QuizTable.FK_CATEGORY + " = ?";

        final Cursor cursor = database.rawQuery(queryString, new String[] { categoryId });

        cursor.moveToFirst();
        int stars = cursor.getInt(0);
        return stars;
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
        final int wordLength = cursor.getInt(5);

        switch (type) {
        case JsonAttributes.QuizType.ANAGRAM_QUIZ: {
            return createAnagramQuiz(readableDatabase, time, wordLength);
        }
        default: {
            throw new IllegalArgumentException("Quiz type " + type + " is not supported");
        }
        }
    }

    private static Quiz createAnagramQuiz(SQLiteDatabase readableDatabase, int time, int wordLength) {
        return new AnagramQuiz(time, wordLength);
    }


    // http://stackoverflow.com/questions/1253561/sqlite-order-by-rand
    public static List<Anagram> getRandomUnsolvedAnagrams(Context context, int anagramLength, int limit) {
        SQLiteDatabase readableDatabase = getReadableDatabase(context);
        final List<Anagram> anagrams = new ArrayList<>();

        String orderBy = "";
        String queryString = "SELECT * FROM " + AnagramTable.NAME + " WHERE "
            + AnagramTable.COLUMN_ID + " IN "
            + "(SELECT " + AnagramTable.COLUMN_ID + " FROM " + AnagramTable.NAME
            + filterByAnagramLength(anagramLength)
            + " ORDER BY RANDOM() LIMIT ?" +  ")"
            + "AND " + AnagramTable.FK_QUIZ + " IS NULL";
        final Cursor cursor = readableDatabase
            .rawQuery(queryString,
                      new String[] {
                          limit + ""
                      });

        while (cursor.moveToNext()) {
            final Anagram anagram = getAnagram(cursor);
            anagrams.add(anagram);
        }

        return anagrams;
    }

    private static int getSolvedAnagramsCountByLength(SQLiteDatabase readableDatabase, int anagramLength) {
        final String queryString = "SELECT COUNT(*) FROM " + AnagramTable.NAME
            + filterByAnagramLength(anagramLength)
            + " AND " + selectAnagramSolved();

        return readFromQueryInt(readableDatabase, queryString);
    }

    private static int getUnsolvedAnagramsCountByLength(SQLiteDatabase readableDatabase, int anagramLength) {
        final String queryString = "SELECT COUNT(*) FROM " + AnagramTable.NAME
            + filterByAnagramLength(anagramLength)
            + " AND " + selectAnagramUnsolved();

        return readFromQueryInt(readableDatabase, queryString);
    }

    private static int readFromQueryInt(SQLiteDatabase readableDatabase, String queryString) {
        final Cursor cursor = readableDatabase
            .rawQuery(queryString, new String[] {});
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    private static String selectAnagramSolved() {
        return AnagramTable.FK_QUIZ + " IS NOT NULL";
    }

    private static String selectAnagramUnsolved() {
        return AnagramTable.FK_QUIZ + " IS NULL";
    }

    private static String filterByAnagramLength(int anagramLength) {
        if (anagramLength == -1) {
            return " WHERE LENGTH(" + AnagramTable.COLUMN_QUESTION + ") > 3 AND "
                + "LENGTH(" + AnagramTable.COLUMN_QUESTION + ") < 10";
        }
        return " WHERE LENGTH(" + AnagramTable.COLUMN_QUESTION + ") = " + anagramLength;
    }

    private static Anagram getAnagram(Cursor cursor) {
        final String id = cursor.getString(0);
        final String question = cursor.getString(2);
        final String answer = cursor.getString(3);
        final String meaning = cursor.getString(4);

        return new Anagram(id, question, answer, meaning);
    }

    private static boolean getBooleanFromDatabase(String isSolved) {
        // json stores booleans as true/false, whereas SQLite stores them as 0 1
        return isSolved != null && isSolved.length() == 1 && Integer.valueOf(isSolved) == 1;
    }

    private static SQLiteDatabase getReadableDatabase(Context context) {
        return getInstance(context).getReadableDatabase();
    }

    private static SQLiteDatabase getWritableDatabase(Context context) {
        return getInstance(context).getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //
        db.execSQL(CategoryTable.CREATE);
        db.execSQL(QuizTable.CREATE);
        db.execSQL(AnagramTable.CREATE);
        db.execSQL(ProfileTable.CREATE);
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
                fillCategories(db);
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

    private void fillCategories(SQLiteDatabase db) throws JSONException, IOException {
        ContentValues values = new ContentValues(); // reuse
        JSONArray jsonArray = new JSONArray(readCategoriesFromResources());
        JSONObject category;
        for (int i = 0; i < jsonArray.length(); i++) {
            category = jsonArray.getJSONObject(i);
            final String categoryId = category.getString(JsonAttributes.ID);
            fillCategory(db, values, category, categoryId);
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
        values.put(CategoryTable.COLUMN_TIME, category.getString(JsonAttributes.TIME));
        putOptionalInt(values, CategoryTable.COLUMN_WORD_LIMIT, category.getString(JsonAttributes.WORD_LIMIT));
        putOptionalInt(values, CategoryTable.COLUMN_WORD_LENGTH, category.getString(JsonAttributes.WORD_LENGTH));

        db.insert(CategoryTable.NAME, null, values);
    }

    private void putOptionalInt(ContentValues values, String column, String value) {
        if (value != null| value != "-1") {
            values.put(column, value);
        }
    }

    // UPDATE OPERATIONS

    public static void syncCategoryLocal(Category category) {
        if (mCategories != null && mCategories.contains(category)) {
            final int location = mCategories.indexOf(category);
            final Category local = mCategories.get(location);
            local.sync(category);
        }
    }

    public static void syncProfileLocalAddStar(int star) {
        mProfile.addStar(star);
    }

    public static String insertQuiz(Context context, AnagramQuiz quiz, String categoryId) {
        insertQuizLocal(quiz, categoryId);
        return insertQuizDb(context, quiz, categoryId);
    }

    private static void insertQuizLocal(AnagramQuiz quiz, String categoryId) {
    }

    private static String insertQuizDb(Context context, AnagramQuiz quiz, String categoryId) {
        SQLiteDatabase writableDatabase = getWritableDatabase(context);

        ContentValues values = new ContentValues();
        values.put(QuizTable.FK_CATEGORY, categoryId);
        values.put(QuizTable.COLUMN_TYPE, "anagram-quiz");
        values.put(QuizTable.COLUMN_TIME, quiz.getTime());
        values.put(QuizTable.COLUMN_WORD_LENGTH, quiz.getWordLength());
        values.put(QuizTable.COLUMN_NB_STAR, quiz.getStars());
        

        long id = writableDatabase
            .insert(QuizTable.NAME, null, values);

        return id + "";
    }

    public static void insertAnagrams(Context context, List<Anagram> anagrams) {
        SQLiteDatabase db = getWritableDatabase(context);
        db.beginTransaction();
        try {
            insertAnagrams(db, anagrams);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static void insertAnagrams(SQLiteDatabase database, List<Anagram> anagrams) {
        ContentValues values = new ContentValues();
        for (Anagram anagram : anagrams) {
            values.clear();
            values.put(AnagramTable.FK_QUIZ, anagram.getQuizId());
            database.update(AnagramTable.NAME,
                            values,
                            AnagramTable.COLUMN_ID + " =?",
                            new String[]{ anagram.getId() });
        }
    }
}
