package com.maranan.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;

import com.maranan.utils.GetterSetter;

import java.io.File;
import java.util.ArrayList;

public class MarananDB extends SQLiteOpenHelper {

    public static SQLiteDatabase sqlitedb;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Maranan";
    private static final String FILE_DIR = "DataBase";
    private static String TABLE_NAME = "DedicationDirector";

    private static final String KEY_ID = "id";
    private static final String DED_ID = "ded_id";
    private static final String NATURE = "nature";
    private static final String SEX = "sex";
    private static final String THERE_IS = "thereIs";
    private static final String NAME = "name";
    private static final String BLESSING = "blessing";
    private static final String EMAIL = "email";
    private static final String PUBLISH = "publish";
    private static final String STATUS = "status";
    private static final String TIME = "time";
    private static final String DATE = "date";
    private static final String SIGNINWITH = "signInWith";
    private static final String NAME_OPTIONAL = "nameOptional";
    private static final String ADMINCHECKED = "admin_checked";
    private static final String F_STATUS = "f_status";
    private static final String L_STATUS = "l_status";
    private static final String M_STATUS = "m_status";
    private static GetterSetter getset;

    // Called Constructor
    public MarananDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        /*
         * super(context, Environment.getExternalStorageDirectory() +
		 * File.separator + FILE_DIR + File.separator + DATABASE_NAME, null,
		 * DATABASE_VERSION);
		 */
    }

    // Called Constructor
    public MarananDB(Context context, String name, CursorFactory factory,
                     int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
		/*
		 * super(context, Environment.getExternalStorageDirectory() +
		 * File.separator + FILE_DIR + File.separator + DATABASE_NAME, null,
		 * DATABASE_VERSION);
		 */
    }

    // onCreate for create database
    @Override
    public void onCreate(SQLiteDatabase db) {
		/* Query For Add Table */
        String SQL = "";

        SQL = SQL + "CREATE TABLE " + TABLE_NAME;
        SQL = SQL + "(";

        SQL = SQL + "	" + KEY_ID + " INTEGER PRIMARY KEY, ";
        SQL = SQL + "	" + DED_ID + " VARCHAR, ";
        SQL = SQL + "	" + NATURE + " VARCHAR, ";
        SQL = SQL + "	" + SEX + " VARCHAR, ";
        SQL = SQL + "	" + THERE_IS + " VARCHAR, ";
        SQL = SQL + "	" + NAME + " VARCHAR, ";
        SQL = SQL + "	" + BLESSING + " VARCHAR, ";
        SQL = SQL + "	" + EMAIL + " VARCHAR, ";
        SQL = SQL + "	" + PUBLISH + " VARCHAR, ";
        SQL = SQL + "	" + STATUS + " VARCHAR, ";
        SQL = SQL + "	" + TIME + " VARCHAR, ";
        SQL = SQL + "	" + DATE + " VARCHAR, ";
        SQL = SQL + "	" + SIGNINWITH + " VARCHAR, ";
        SQL = SQL + "	" + NAME_OPTIONAL + " VARCHAR, ";
        SQL = SQL + "	" + ADMINCHECKED + " VARCHAR, ";
        SQL = SQL + "	" + F_STATUS + " VARCHAR, ";
        SQL = SQL + "	" + L_STATUS + " VARCHAR, ";
        SQL = SQL + "	" + M_STATUS + " VARCHAR ";

        SQL = SQL + ")";

        db.execSQL(SQL);

    }

    // onUpgrade for upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Open DataBase
    public void open() {
        sqlitedb = getWritableDatabase();
    }

    // Get DataBase From SD Card
    public void getDataBaseFromSdCard() {
        File dbfile = new File(Environment.getExternalStorageDirectory()
                + File.separator + FILE_DIR + File.separator + DATABASE_NAME);
        @SuppressWarnings("unused")
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
    }

    // First Name already exists or not
    public boolean isFirstNameExist(String firstName) {
        Cursor cursor = null;
        try {
            sqlitedb = this.getWritableDatabase();
            String SQL = "SELECT COUNT(" + NAME + ") FROM " + TABLE_NAME
                    + " WHERE " + NAME + " = '" + firstName + "'";
            cursor = sqlitedb.rawQuery(SQL, null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) > 0) {
                    return true;
                }
            }

        } finally {
            cursor.close();
        }
        return false;
    }

    // Name Optional already exists or not
    public boolean isNameOptExist(String nameOpt) {
        Cursor cursor = null;
        try {
            sqlitedb = this.getWritableDatabase();
            String SQL = "SELECT COUNT(" + NAME_OPTIONAL + ") FROM " + TABLE_NAME
                    + " WHERE " + NAME_OPTIONAL + " = '" + nameOpt + "'";
            cursor = sqlitedb.rawQuery(SQL, null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) > 0) {
                    return true;
                }
            }
        } finally {
            cursor.close();
        }
        return false;
    }

    // First Name already exists or not
    public boolean isThereIsExist(String thereIs) {
        Cursor cursor = null;
        try {
            sqlitedb = this.getWritableDatabase();
            String SQL = "SELECT COUNT(" + THERE_IS + ") FROM " + TABLE_NAME
                    + " WHERE " + THERE_IS + " = '" + thereIs + "'";
            cursor = sqlitedb.rawQuery(SQL, null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) > 0) {
                    return true;
                }
            }
        } finally {
            cursor.close();
        }

        return false;
    }

    // Admin Name already exists or not
    public boolean isAdminExist() {
        Cursor cursor = null;
        try {
            sqlitedb = this.getWritableDatabase();
            String SQL = "SELECT COUNT(" + ADMINCHECKED + ") FROM " + TABLE_NAME
                    + " WHERE " + ADMINCHECKED + " = '" + "admin" + "'";
            cursor = sqlitedb.rawQuery(SQL, null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) > 0) {
                    return true;
                }
            }
        } finally {
            cursor.close();
        }

        return false;
    }

    // Name Column already exists or not
    public boolean existsColumnInTable() {
        Cursor mCursor = null;
        try {
            // query 1 row
            mCursor = sqlitedb.rawQuery("SELECT * FROM " + TABLE_NAME
                    + " LIMIT 0", null);

            // getColumnIndex gives us the index (0 to ...) of the column -
            // otherwise we get a -1
            if (mCursor.getColumnIndex(NAME) != -1)
                return true;
            else
                return false;

        } finally {
            mCursor.close();
        }
    }

    // Insert records
    public void insertRecords(SQLiteDatabase sqlite_dbase, GetterSetter getset) {
        SQLiteStatement insertStmt;
        sqlite_dbase = getWritableDatabase();
        String SQL = "";

        SQL = SQL + "INSERT INTO " + TABLE_NAME;
        SQL = SQL + "(";
        SQL = SQL + " 	" + DED_ID + ", ";
        SQL = SQL + " 	" + NATURE + ", ";
        SQL = SQL + " 	" + SEX + ", ";
        SQL = SQL + " 	" + THERE_IS + ", ";
        SQL = SQL + " 	" + NAME + ", ";
        SQL = SQL + " 	" + BLESSING + ", ";
        SQL = SQL + " 	" + EMAIL + ", ";
        SQL = SQL + " 	" + PUBLISH + ", ";
        SQL = SQL + " 	" + STATUS + ", ";
        SQL = SQL + " 	" + TIME + ", ";
        SQL = SQL + " 	" + DATE + ", ";
        SQL = SQL + " 	" + SIGNINWITH + ", ";
        SQL = SQL + " 	" + NAME_OPTIONAL + ", ";
        SQL = SQL + " 	" + ADMINCHECKED + ", ";
        SQL = SQL + " 	" + F_STATUS + ", ";
        SQL = SQL + " 	" + L_STATUS + ", ";
        SQL = SQL + " 	" + M_STATUS;
        SQL = SQL + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        sqlite_dbase.beginTransaction();

        insertStmt = sqlite_dbase.compileStatement(SQL);
        insertStmt.bindString(1, getset.getId());
        insertStmt.bindString(2, getset.getNature());
        insertStmt.bindString(3, getset.getSex());
        insertStmt.bindString(4, getset.getThere_Is());
        insertStmt.bindString(5, getset.getName());
        insertStmt.bindString(6, getset.getBlessing());
        insertStmt.bindString(7, getset.getEmail());
        insertStmt.bindString(8, getset.getPublish());
        insertStmt.bindString(9, "");
        insertStmt.bindString(10, getset.getTime());
        insertStmt.bindString(11, getset.getDate());
        insertStmt.bindString(12, getset.getSigninwith());
        insertStmt.bindString(13, getset.getName_Optional());
        insertStmt.bindString(14, getset.getAdmin());
        insertStmt.bindString(15, getset.getF_status());
        insertStmt.bindString(16, getset.getL_status());
        insertStmt.bindString(17, getset.getM_status());
        insertStmt.execute();
        insertStmt.clearBindings();
        sqlite_dbase.setTransactionSuccessful();
        sqlite_dbase.endTransaction();

    }

    // Update records
    public void updateRecords(String name) {
        Cursor cursor = null;
        try {
            sqlitedb = getWritableDatabase();
            String sql = "UPDATE " + TABLE_NAME + " SET " + NAME + "='" + name
                    + "'" + " WHERE " + NAME + "=" + getset.getName();
            cursor = sqlitedb.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) > 0) {

                }
            }
            sqlitedb.close();
        } finally {
            cursor.close();
        }

    }

    // getName from table using name
    public GetterSetter getName(String name) {
        GetterSetter getset;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String SQL = "";

            SQL = SQL + " SELECT * ";
            SQL = SQL + " FROM " + TABLE_NAME;
            SQL = SQL + " WHERE " + NAME + " = " + name;

            cursor = db.rawQuery(SQL, new String[]{});
            getset = new GetterSetter();

            if (cursor.moveToFirst()) {
                getset.setName(cursor.getString(1));
                getset.setSex(cursor.getString(2));
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

            db.close();
            if (cursor.getCount() == 0) {
                return null;

            } else {
                return getset;
            }
        } finally {
            cursor.close();
        }
    }

    // Getting All Records
    public ArrayList<GetterSetter> getAllRecords() {
        Cursor cursor = null;
        ArrayList<GetterSetter> contactList = new ArrayList<GetterSetter>();
        try {
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_NAME;

            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    GetterSetter getset = new GetterSetter();
                    getset.setNature((cursor.getString(1)));
                    getset.setName(cursor.getString(2));
                    getset.setSex(cursor.getString(3));
                    getset.setThere_Is(cursor.getString(4));
                    getset.setName_Optional(cursor.getString(5));
                    getset.setBlessing(cursor.getString(6));
                    getset.setTime(cursor.getString(10));
                    // Adding records to list
                    contactList.add(getset);
                } while (cursor.moveToNext());
            }

        } finally {
            cursor.close();
        }

        // return contact list
        return contactList;
    }

    // delete records
    public void deleteAllRecord() {
        sqlitedb = this.getWritableDatabase();
        sqlitedb.delete(TABLE_NAME, null, null);
        sqlitedb.close();
    }

    // Deleting single contact
    public void deleteSingleRecord(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, DED_ID + " = ?", new String[]{id});
        db.close();
    }

}
