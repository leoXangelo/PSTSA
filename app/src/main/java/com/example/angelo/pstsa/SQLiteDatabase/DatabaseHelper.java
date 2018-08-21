package com.example.angelo.pstsa.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "pstsa";
    //Create table cache history
    private static final String COOKIES = "CREATE TABLE tb_cookies " +
            "(" +
            "cookiesID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "cookiesName TEXT" +
            ")";
    private static final String IPADDRESS = "CREATE TABLE tb_ipaddress " +
            "(" +
            "ipID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "ipAddress TEXT" +
            ")";
    private static final String ACCOUNT = "CREATE TABLE tb_account " +
            "(" +
            "accountID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "accountUsername TEXT, " +
            "accountPassword TEXT" +
            ")";
    private static final String RANKING_MALE = "CREATE TABLE tb_ranking_male " +
            "(" +
            "rankingID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "number INTEGER," +
            "name TEXT," +
            "gender TEXT," +
            "total INTEGER," +
            "rank INTEGER" +
            ")";

    private static final String RANKING_FEMALE = "CREATE TABLE tb_ranking_female " +
            "(" +
            "rankingID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "number INTEGER," +
            "name TEXT," +
            "gender TEXT," +
            "total INTEGER," +
            "rank INTEGER" +
            ")";

    private static final String AVERAGING_FEMALE = "CREATE TABLE tb_averaging_female " +
            "(" +
            "rankingID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "number INTEGER," +
            "name TEXT," +
            "gender TEXT," +
            "total INTEGER," +
            "rank INTEGER" +
            ")";
    private static final String AVERAGING_MALE = "CREATE TABLE tb_averaging_male " +
            "(" +
            "rankingID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "number INTEGER," +
            "name TEXT," +
            "gender TEXT," +
            "total INTEGER," +
            "rank INTEGER" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COOKIES);
        db.execSQL(IPADDRESS);
        db.execSQL(ACCOUNT);
        db.execSQL(RANKING_MALE);
        db.execSQL(RANKING_FEMALE);
        db.execSQL(AVERAGING_MALE);
        db.execSQL(AVERAGING_FEMALE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "tb_cookies");
        db.execSQL("DROP TABLE IF EXISTS " + "tb_ipaddress");
        db.execSQL("DROP TABLE IF EXISTS " + "tb_account");
        db.execSQL("DROP TABLE IF EXISTS " + "tb_ranking_male");
        db.execSQL("DROP TABLE IF EXISTS " + "tb_ranking_female");
        db.execSQL("DROP TABLE IF EXISTS " + "tb_averaging_male");
        db.execSQL("DROP TABLE IF EXISTS " + "tb_averaging_female");
    }

    public boolean SaveAccount(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("accountUsername", username);
        cv.put("accountPassword", password);
        long result = db.insert("tb_account", null, cv);
        db.close();
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean SaveMaleRank(int number, String name, String gender, int total, int rank){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("number", number);
        cv.put("name", name);
        cv.put("gender", gender);
        cv.put("total", total);
        cv.put("rank", rank);
        long result = db.insert("tb_ranking_male", null, cv);
        db.close();
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean SaveFemaleRank(int number, String name, String gender, int total, int rank){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("number", number);
        cv.put("name", name);
        cv.put("gender", gender);
        cv.put("total", total);
        cv.put("rank", rank);
        long result = db.insert("tb_ranking_female", null, cv);
        db.close();
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean SaveFemaleAverage(int number, String name, String gender, int total, int rank){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("number", number);
        cv.put("name", name);
        cv.put("gender", gender);
        cv.put("total", total);
        cv.put("rank", rank);
        long result = db.insert("tb_averaging_female", null, cv);
        db.close();
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean SaveMaleAverage(int number, String name, String gender, int total, int rank){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("number", number);
        cv.put("name", name);
        cv.put("gender", gender);
        cv.put("total", total);
        cv.put("rank", rank);
        long result = db.insert("tb_averaging_male", null, cv);
        db.close();
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Integer deleteMaleRank(String gender){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete("tb_ranking_male", "gender=?", new String[]{gender});
        return i;
    }

    public Integer deleteFemaleRank(String gender){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete("tb_ranking_female", "gender=?", new String[]{gender});
        return i;
    }

    public Integer deleteMaleAverage(String gender){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete("tb_averaging_male", "gender=?", new String[]{gender});
        return i;
    }

    public Integer deleteFemaleAverage(String gender){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete("tb_averaging_female", "gender=?", new String[]{gender});
        return i;
    }

    public boolean UpdateMaleRank(String number, int rank){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("rank", rank);
        int result = db.update("tb_ranking_male", cv, "number =?", new String[]{number});
        if (result > 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean UpdateFemaleRank(String number, int rank){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("rank", rank);
        int result = db.update("tb_ranking_female", cv, "number =?", new String[]{number});
        if (result > 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean UpdateMaleAverage(String number, int rank){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("rank", rank);
        int result = db.update("tb_averaging_male", cv, "number =?", new String[]{number});
        if (result > 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean UpdateFemaleAverage(String number, int rank){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("rank", rank);
        int result = db.update("tb_averaging_female", cv, "number =?", new String[]{number});
        if (result > 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean UpdateAccount(String ID, String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("accountUsername", username);
        cv.put("accountPassword", password);
        int result = db.update("tb_account", cv, "accountID =?", new String[]{ID});
        if (result > 0){
            return true;
        }else {
            return false;
        }
    }

    public Cursor GetAccount(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tb_account", null);
        return res;
    }

    public boolean SaveCookies(String cookies){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("cookiesName", cookies);
        long result = db.insert("tb_cookies", null, cv);
        db.close();
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean SaveIP(String IP){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ipAddress", IP);
        long result = db.insert("tb_ipaddress", null, cv);
        db.close();
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean UpdateIP(String ID, String IP){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ipAddress", IP);
        int result = db.update("tb_ipaddress", cv, "ipID =?", new String[]{ID});
        if (result > 0){
            return true;
        }else {
            return false;
        }
    }

    public Cursor GetIP(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tb_ipaddress", null);
        return res;
    }

    public Cursor GetCookies(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tb_cookies", null);
        return res;
    }

    public Cursor GetMaleRank(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tb_ranking_male ORDER BY total ASC", null);
        return res;
    }

    public Cursor GetFemaleRank(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tb_ranking_female ORDER BY total ASC", null);
        return res;
    }

    public Cursor GetMaleAverage(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tb_averaging_male ORDER BY total DESC", null);
        return res;
    }

    public Cursor GetFemaleAverage(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM tb_averaging_female ORDER BY total DESC", null);
        return res;
    }
}
