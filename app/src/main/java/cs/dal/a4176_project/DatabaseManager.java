package cs.dal.a4176_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tedzhao on 2017/10/30.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "project4176g2";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_SPORT_RECORDS = "sports";
    private static final String TABLE_DIET_RECORDS = "diets";
    private static final String TABLE_PROFILE = "profile";
    private static final String TABLE_CALORIE_RECORDS = "calories";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_PLANNED = "planned";
    private static final String COLUMN_COMPLETED = "complete";
    private static final String COLUMN_C2P = "c2p";
    private static final String COLUMN_CALORIE_CONSUMED = "calorie_consumed";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CALORIE_INTAKE = "calorie_intake";
    private static final String COLUMN_INFO = "info";
    private static final String TABLE_CODE_INFO = "code_info";
    private static final String COLUMN_SUGGESTION = "suggestion";
    //private static final String COLUMN_ICON = "icon";
    //private static final String COLUMN_NAME = "name";
    //private static final String COLUMN_AGE = "age";
    //private static final String COLUMN_GENDER = "gender";
    //private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_WEIGHT = "weight";
    //private static final String COLUMN_MEDICAL = "medical";
    private static final String COLUMN_NET_CALORIE_CONSUMED = "net_calorie_consumed"; /* loss - gain */

    // constructor
    DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createSportRecordsTable(sqLiteDatabase);
        createDietRecordsTable(sqLiteDatabase);
        createProfileTable(sqLiteDatabase);
        createCalorieRecordsTable(sqLiteDatabase);
        createCodeInfoTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_SPORT_RECORDS + ";";
        sqLiteDatabase.execSQL(sql);
        String sql2 = "DROP TABLE IF EXISTS " + TABLE_DIET_RECORDS + ";";
        sqLiteDatabase.execSQL(sql2);
        String sql3 = "DROP TABLE IF EXISTS profile";
        sqLiteDatabase.execSQL(sql3);
        String sql4 = "DROP TABLE IF EXISTS " + TABLE_CALORIE_RECORDS + ";";
        sqLiteDatabase.execSQL(sql4);
        //String sql5 = "DROP TABLE IF EXISTS " + TABLE_CODE_INFO + ";";
        //sqLiteDatabase.execSQL(sql5);
        onCreate(sqLiteDatabase);
    }


    /**
     * create sport records table
     * */
    public void createSportRecordsTable(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_SPORT_RECORDS + " (\n" +
                "    " + COLUMN_ID + " INTEGER NOT NULL CONSTRAINT sport_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + COLUMN_TYPE + " varchar(200) NOT NULL,\n" +
                "    " + COLUMN_TITLE + " varchar(200) NOT NULL,\n" +
                "    " + COLUMN_DATE + " date NOT NULL,\n" +
                "    " + COLUMN_TIME + " time NULL,\n" +
                "    " + COLUMN_PLANNED + " float NOT NULL, \n" +
                "    " + COLUMN_COMPLETED + " float NOT NULL, \n" +
                "    " + COLUMN_C2P + " float NULL, \n" +
                "    " + COLUMN_CALORIE_CONSUMED + " float NOT NULL, \n" +
                "    " + COLUMN_WEIGHT + " float NOT NULL\n" +
                ");";
        sqLiteDatabase.execSQL(sql);
    }

    /**
     * create diet records table
     * */
    public void createDietRecordsTable(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_DIET_RECORDS + " (\n" +
                "    " + COLUMN_ID + " INTEGER NOT NULL CONSTRAINT diet_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + COLUMN_TYPE + " varchar(200) NOT NULL,\n" +
                "    " + COLUMN_TITLE + " varchar(200) NOT NULL,\n" +
                "    " + COLUMN_DATE + " date NOT NULL,\n" +
                "    " + COLUMN_AMOUNT + " float NOT NULL, \n" +
                "    " + COLUMN_CALORIE_INTAKE + " float NOT NULL\n" +
                ");";
        sqLiteDatabase.execSQL(sql);
    }

    /**
     * create setting table
     * */
    public void createProfileTable(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE profile " +
                "(id integer primary key, level integer, name text, age text, gender text, height text, weight text, medical text);";
        sqLiteDatabase.execSQL(sql);
    }

    /**
     * create calorie Records table
     * */
    public void createCalorieRecordsTable(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_CALORIE_RECORDS + " (\n" +
                "    " + COLUMN_ID + " INTEGER NOT NULL CONSTRAINT calorie_record_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + COLUMN_DATE + " date NOT NULL,\n" +
                "    " + COLUMN_NET_CALORIE_CONSUMED + " INTEGER NOT NULL\n" +
                ");";
        sqLiteDatabase.execSQL(sql);
    }


    /**
     * create code info table
     * */
    public void createCodeInfoTable(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_CODE_INFO + " (\n" +
                "    " + COLUMN_ID + " INTEGER NOT NULL CONSTRAINT code_info_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + COLUMN_CODE + " varchar(200) NOT NULL,\n" +
                "    " + COLUMN_INFO + " varchar(200) NOT NULL,\n" +
                "    " + COLUMN_SUGGESTION + " varchar(200) NOT NULL\n" +
                ");";
        sqLiteDatabase.execSQL(sql);
    }

    /**
     * insert code info table
     * */
    public boolean addCodeInfoTable(String code, String info, String suggestion) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CODE, code);
        contentValues.put(COLUMN_INFO, info);
        contentValues.put(COLUMN_SUGGESTION, suggestion);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(TABLE_CODE_INFO, null, contentValues) != -1;
    }

    /**
     * insert into sport records table
     * */

    public boolean addSportRecords(String type, String title, String date, String time, float planned, float completed, float c2p, float calorie_consumed, float weight) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TYPE, type);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_PLANNED, planned);
        contentValues.put(COLUMN_COMPLETED, completed);
        contentValues.put(COLUMN_C2P, c2p);
        contentValues.put(COLUMN_CALORIE_CONSUMED, calorie_consumed);
        contentValues.put(COLUMN_WEIGHT, weight);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(TABLE_SPORT_RECORDS, null, contentValues) != -1;
    }

    /**
     * rawQuery returns a Cursor object having all the data fetched from database
    * */
    public Cursor getAllSportRecords() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_SPORT_RECORDS, null);
    }

    public Cursor getAllSportRecordsOrderByDate() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM sports ORDER BY date DESC LIMIT 7", null);
    }

    boolean deleteSportRecord(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_SPORT_RECORDS, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    boolean updateSportRecord(String type, String title, String date, String time, float planned, float completed, float c2p, float calorie_consumed, float weight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TYPE, type);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_PLANNED, planned);
        contentValues.put(COLUMN_COMPLETED, completed);
        contentValues.put(COLUMN_C2P, c2p);
        contentValues.put(COLUMN_CALORIE_CONSUMED, calorie_consumed);
        contentValues.put(COLUMN_WEIGHT, weight);
        return db.update(TABLE_SPORT_RECORDS, contentValues, COLUMN_DATE + "=?", new String[]{String.valueOf(date)}) == 1;
    }



    /**
     * insert into diet records table
     * */

    public boolean addDietRecords(String type, String title, String date, float amount, float calorie_intake) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TYPE, type);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_AMOUNT, amount);
        contentValues.put(COLUMN_CALORIE_INTAKE, calorie_intake);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(TABLE_DIET_RECORDS, null, contentValues) != -1;
    }

    /**
     * rawQuery returns a Cursor object having all the data fetched from database
     * */
    public Cursor getAllDietRecords() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_DIET_RECORDS, null);
    }

    boolean deleteDietRecord(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_DIET_RECORDS, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    boolean updateDietRecord(int id, String type, String title, String date, float amount, float calorie_intake) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TYPE, type);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_AMOUNT, amount);
        contentValues.put(COLUMN_CALORIE_INTAKE, calorie_intake);
        return db.update(TABLE_DIET_RECORDS, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }




    /**
     * insert into Profile table
     * */
    public boolean addProfile(String name, String age, String gender, String height, String weight, String medical) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("level", 0);
        contentValues.put("name", name);
        contentValues.put("age", age);
        contentValues.put("gender", gender);
        contentValues.put("height", height);
        contentValues.put("weight", weight);
        contentValues.put("medical", medical);
        //contentValues.put("icon", icon);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("profile", null, contentValues) != -1;
    }

    public boolean updateProfile(int id, String name, String age, String gender, String height, String weight, String medical) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("age", age);
        contentValues.put("gender", gender);
        contentValues.put("height", height);
        contentValues.put("weight", weight);
        contentValues.put("medical", medical);
        //contentValues.put("icon", icon);
        SQLiteDatabase db = getWritableDatabase();
        return db.update(TABLE_PROFILE, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    public void updateLevel(int id, int l) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("level", l);
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_PROFILE, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        Log.e("shoude be updated", "updateLevel: ++");
    }


    /**
     * rawQuery returns a Cursor object having all the data fetched from database
     * */
    public Cursor getAllProfile() {

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Log.e("2", "getAllProfile: finished ");
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_PROFILE, null);
    }

    boolean deleteProfile(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_PROFILE, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }







    /**
     * insert into calorie records table
     * */
    public boolean addCalorieRecords(String date, float net_calorie_consumed) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_NET_CALORIE_CONSUMED, net_calorie_consumed);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(TABLE_CALORIE_RECORDS, null, contentValues) != -1;
    }

    /**
     * rawQuery returns a Cursor object having all the data fetched from database
     * */
    public Cursor getAllCalorieRecords() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_CALORIE_RECORDS, null);
    }

    boolean deleteCalorieRecord(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_CALORIE_RECORDS, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }



    /**
     * rawQuery returns a Cursor object having all the data fetched from database
     * */
    public Cursor getAllCodeInfo() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_CODE_INFO, null);
    }

}
