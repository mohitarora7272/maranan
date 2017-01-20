package com.maranan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.maranan.utils.GetterSetter;

import java.util.ArrayList;

public class NotificationDB extends SQLiteOpenHelper {

    public static SQLiteDatabase sqlitedb;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NotificationDB";
    private static String TABLE_NAME = "Notification";

    private static final String KEY_ID = "id";
    private static final String ALERT_ID = "alert_id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE = "image";
    private static final String PHONE = "phone";
    private static final String RADIO_ALERT = "radio_alert";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String DATE_TIME = "date_time";
    private static final String LOCATION = "location";
    private static final String IMAGE_RESOURCE = "image_resource";
    private static final String SEEK_BAR = "seek_bar";
    private static final String PROGRESS_BAR = "progress_bar";

    public NotificationDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public NotificationDB(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

		/* Query For Add Table */
        String SQL = "";

        SQL = SQL + "CREATE TABLE " + TABLE_NAME;
        SQL = SQL + "(";

        SQL = SQL + "	" + KEY_ID + " INTEGER PRIMARY KEY, ";
        SQL = SQL + "	" + ALERT_ID + " VARCHAR, ";
        SQL = SQL + "	" + TITLE + " VARCHAR, ";
        SQL = SQL + "	" + DESCRIPTION + " VARCHAR, ";
        SQL = SQL + "	" + IMAGE + " VARCHAR, ";
        SQL = SQL + "	" + PHONE + " VARCHAR, ";
        SQL = SQL + "	" + RADIO_ALERT + " VARCHAR, ";
        SQL = SQL + "	" + DATE + " VARCHAR, ";
        SQL = SQL + "	" + TIME + " VARCHAR, ";
        SQL = SQL + "	" + DATE_TIME + " VARCHAR, ";
        SQL = SQL + "	" + LOCATION + " VARCHAR, ";
        SQL = SQL + "	" + IMAGE_RESOURCE + " VARCHAR, ";
        SQL = SQL + "	" + SEEK_BAR + " VARCHAR, ";
        SQL = SQL + "	" + PROGRESS_BAR + " VARCHAR ";

        SQL = SQL + ")";

        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertRecords(SQLiteDatabase sqlite_dbase, GetterSetter getset) {
        SQLiteStatement insertStmt;
        sqlite_dbase = getWritableDatabase();
        String SQL = "";

        SQL = SQL + "INSERT INTO " + TABLE_NAME;
        SQL = SQL + "(";
        SQL = SQL + " 	" + ALERT_ID + ", ";
        SQL = SQL + " 	" + TITLE + ", ";
        SQL = SQL + " 	" + DESCRIPTION + ", ";
        SQL = SQL + " 	" + IMAGE + ", ";
        SQL = SQL + " 	" + PHONE + ", ";
        SQL = SQL + " 	" + RADIO_ALERT + ", ";
        SQL = SQL + " 	" + DATE + ", ";
        SQL = SQL + " 	" + TIME + ", ";
        SQL = SQL + " 	" + DATE_TIME + ", ";
        SQL = SQL + " 	" + LOCATION + ", ";
        SQL = SQL + " 	" + IMAGE_RESOURCE + ", ";
        SQL = SQL + " 	" + SEEK_BAR + ", ";
        SQL = SQL + " 	" + PROGRESS_BAR;
        SQL = SQL + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        sqlite_dbase.beginTransaction();

        insertStmt = sqlite_dbase.compileStatement(SQL);
        insertStmt.bindString(1, getset.getAlert_id());
        insertStmt.bindString(2, getset.getTitle());
        insertStmt.bindString(3, getset.getDescriptions());
        insertStmt.bindString(4, getset.getThumbnailHigh());
        insertStmt.bindString(5, getset.getPhone());
        insertStmt.bindString(6, getset.getRadio_alert());
        insertStmt.bindString(7, getset.getDate());
        insertStmt.bindString(8, getset.getTime());
        insertStmt.bindString(9, getset.getDate() + " " + getset.getTime());
        insertStmt.bindString(10, getset.getLocation());
        insertStmt.bindString(11, String.valueOf(getset.getImageResource()));
        insertStmt.bindString(12, getset.getSeekBar());
        insertStmt.bindString(13, getset.getProgressBar());
        insertStmt.execute();
        insertStmt.clearBindings();
        sqlite_dbase.setTransactionSuccessful();
        sqlite_dbase.endTransaction();

    }


    public ArrayList<GetterSetter> getAllRecords() {
        Cursor cursor = null;
        ArrayList<GetterSetter> notiList = new ArrayList<GetterSetter>();
        try {
            //String selectQuery = "SELECT  * FROM " + TABLE_NAME + DATE +" DESC";

            SQLiteDatabase db = this.getWritableDatabase();
            //Cursor cursor = db.rawQuery(selectQuery, null);

            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                            " ORDER BY " + DATE_TIME + " ASC"
                    , new String[]{});

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    GetterSetter getset = new GetterSetter();
                    getset.setAlert_id((cursor.getString(1)));
                    getset.setTitle(cursor.getString(2));
                    getset.setDescriptions(cursor.getString(3));
                    getset.setThumbnailHigh(cursor.getString(4));
                    getset.setPhone(cursor.getString(5));
                    getset.setRadio_alert(cursor.getString(6));
                    getset.setDate(cursor.getString(7));
                    getset.setTime(cursor.getString(8));
                    getset.setDateTimeText(cursor.getString(9));
                    getset.setLocation(cursor.getString(10));
                    getset.setImageResource(Integer.parseInt(cursor.getString(11)));
                    getset.setSeekBar(cursor.getString(12));
                    getset.setProgressBar(cursor.getString(13));
                    // Adding records to list
                    notiList.add(getset);
                } while (cursor.moveToNext());
            }

        } finally {
            cursor.close();
        }

        // return contact list
        return notiList;
    }


    public boolean isAlertIdExists(String id) {
        Cursor cursor = null;
        try{
            sqlitedb = this.getWritableDatabase();
            String SQL = "SELECT COUNT(" + ALERT_ID + ") FROM " + TABLE_NAME
                    + " WHERE " + ALERT_ID + " = '" + id + "'";
            cursor = sqlitedb.rawQuery(SQL, null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) > 0) {
                    return true;
                }
            }
        }finally {
            cursor.close();
        }

        return false;
    }


    public boolean updateRecords(SQLiteDatabase sqlite_dbase, GetterSetter getset) {
        sqlite_dbase = getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(ALERT_ID, getset.getAlert_id());
        args.put(TITLE, getset.getTitle());
        args.put(DESCRIPTION, getset.getDescriptions());
        args.put(IMAGE, getset.getThumbnailHigh());
        args.put(PHONE, getset.getPhone());
        args.put(RADIO_ALERT, getset.getRadio_alert());
        args.put(DATE, getset.getDate());
        args.put(TIME, getset.getTime());
        args.put(DATE_TIME, getset.getDate() + " " + getset.getTime());
        args.put(LOCATION, getset.getLocation());
        args.put(IMAGE_RESOURCE, getset.getImageResource());
        args.put(SEEK_BAR, getset.getSeekBar());
        args.put(PROGRESS_BAR, getset.getProgressBar());
        return sqlite_dbase.update(TABLE_NAME, args, ALERT_ID + "=" + getset.getAlert_id(), null) > 0;
    }

    public void deleteSingleRecord(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ALERT_ID + " = ?", new String[]{id});
        db.close();
    }

}
