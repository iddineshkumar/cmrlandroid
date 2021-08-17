package com.cm.cmrl.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

;
import com.cm.cmrl.arraylist.MonitorUpsList;
import com.cm.cmrl.arraylist.UpsMonitorUpdateList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iddinesh.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_UPS_MONITOR_LIST= "ups_monitor_list";
    private static final String TABLE_UPS_TIME_LIST= "ups_maintenance_list";
    private static final String TABLE_UPS_MONITOR_UPDATE= "ups_monitor_update";

    // UPS monitor Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_UPS_NAME = "ups_name";
    private static final String KEY_UPS_ID_= "ups_id";

    // Ups monitor time list
    private static final String KEY_UPS_TIME_ID_= "ups_time_id";
    private static final String KEY_UPS_TIME= "ups_time_name";
    private static final String KEY_UPS_TIME_API_ID= "ups_time_api_id";

    // Ups monitor update list
    private static final String KEY_READING_DC_VOLTAGE_= "ups_reading_dc_voltage";
    private static final String KEY_RYB ="ups_ryb";
    private static final String KEY_KVA = "ups_kva";
    private static final String KEY_AMPS = "ups_amps";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // create monitor table
    private static final String CREATE_UPS_MONITOR_TABLE = "CREATE TABLE " + TABLE_UPS_MONITOR_LIST + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_UPS_NAME + " TEXT,"
            + KEY_UPS_ID_ + " TEXT" + ")";

    // create monitor time list table
    private static final String CREATE_UPS_TIME_TABLE = "CREATE TABLE " + TABLE_UPS_TIME_LIST + "("
            + KEY_UPS_TIME_ID_ + " INTEGER PRIMARY KEY," + KEY_UPS_TIME + " TEXT,"
            + KEY_UPS_TIME_API_ID + " TEXT" + ")";

    // create monitor update table
    private static final String CREATE_UPS_UPDATE_TABLE = "CREATE TABLE " + TABLE_UPS_MONITOR_UPDATE + "("
            + KEY_UPS_TIME_ID_ + " INTEGER PRIMARY KEY,"
            + KEY_READING_DC_VOLTAGE_ + " TEXT,"
            + KEY_RYB + " TEXT,"
            + KEY_KVA + " TEXT,"
            + KEY_AMPS + " TEXT" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_UPS_MONITOR_TABLE);
        db.execSQL(CREATE_UPS_TIME_TABLE);
        db.execSQL(CREATE_UPS_UPDATE_TABLE);
    }

    // create maintenance table

    // Creating Tables


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPS_MONITOR_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPS_TIME_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPS_MONITOR_UPDATE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

       // Adding new monitor
    public void addContact(MonitorUpsList contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UPS_NAME, contact.getName()); // Contact Name
        values.put(KEY_UPS_ID_, contact.getId()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_UPS_MONITOR_LIST, null, values);
        db.close(); // Closing database connection
    }

    // Adding new monitor_time_list
    public void addTimeList(MonitorUpsList contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UPS_TIME, contact.getName()); // Contact Name
        values.put(KEY_UPS_TIME_API_ID, contact.getId()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_UPS_TIME_LIST, null, values);
        db.close(); // Closing database connection
    }
    // Adding new monitor_time_list
    public void addMonitorUpdateList(UpsMonitorUpdateList contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Inserting Row
        db.insert(TABLE_UPS_MONITOR_UPDATE, null, values);
        db.close(); // Closing database connection
    }

       // Getting single contact
    MonitorUpsList getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_UPS_MONITOR_LIST, new String[] { KEY_ID,
                        KEY_UPS_NAME, KEY_UPS_ID_ }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MonitorUpsList contact = new MonitorUpsList(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }
    // Getting single ups time list
    MonitorUpsList getUpsTimeList(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_UPS_TIME_LIST, new String[] { KEY_UPS_ID_,
                        KEY_UPS_TIME, KEY_UPS_TIME_API_ID }, KEY_UPS_ID_ + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MonitorUpsList contact = new MonitorUpsList(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

    // Getting All Contacts
    public List<MonitorUpsList> getAllContacts() {
        List<MonitorUpsList> contactList = new ArrayList<MonitorUpsList>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_UPS_MONITOR_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MonitorUpsList contact = new MonitorUpsList();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setId(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Getting All Ups time list

    public List<MonitorUpsList> getAllUpsTimeList() {
        List<MonitorUpsList> contactList = new ArrayList<MonitorUpsList>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_UPS_TIME_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MonitorUpsList contact = new MonitorUpsList();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setId(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }
    // Updating single contact
    public int updateUpsTimeList(MonitorUpsList contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UPS_TIME, contact.getName());
        values.put(KEY_UPS_TIME_API_ID, contact.getId());

        // updating row
        return db.update(TABLE_UPS_TIME_LIST, values, KEY_UPS_ID_ + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Updating single contact
    public int updateContact(MonitorUpsList contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UPS_NAME, contact.getName());
        values.put(KEY_UPS_ID_, contact.getId());

        // updating row
        return db.update(TABLE_UPS_MONITOR_LIST, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteContact(MonitorUpsList contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_UPS_MONITOR_LIST, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

    // Deleting single ups time list
    public void deleteUpsTimeListt(MonitorUpsList contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_UPS_TIME_LIST, KEY_UPS_ID_ + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_UPS_MONITOR_LIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
