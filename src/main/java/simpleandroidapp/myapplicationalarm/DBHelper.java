package simpleandroidapp.myapplicationalarm;

import android.content.Context;
/**
 * Created by Jelord Rey Gulle on 10/9/2015. (Pra sa storage)
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBHelper extends SQLiteOpenHelper
{
    private static DBHelper mInstance = null;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "jelord.db";

    public static final String TABLE_NAME = "alarms";
    public static final String COLUMN_ALARM_NAME = "name";
    public static final String COLUMN_ALARM_HOUR = "hour";
    public static final String COLUMN_ALARM_MINUTE = "minute";
    public static final String COLUMN_DAYS = "days";
    public static final String COLUMN_CORRECT_ANSWERS = "correct_answers";
    public static final String COLUMN_ENABLED = "enabled";
    public static final String COLUMN_TONE = "tone";


    public static final String SQL_CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ALARM_NAME + " TEXT," +
            COLUMN_ALARM_HOUR + " INTEGER," +
            COLUMN_ALARM_MINUTE + " INTEGER," +
            COLUMN_DAYS + " TEXT," +
            COLUMN_CORRECT_ANSWERS + " INTEGER, " +
            COLUMN_ENABLED + " BOOLEAN, " +
            COLUMN_TONE + " TEXT" + ")";

    public static final String SQL_DELETE_ALARMS_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public static DBHelper getInstance(Context mContext)
    {
        if(mInstance == null)
            mInstance = new DBHelper(mContext.getApplicationContext());
        return mInstance;
    }


    private DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Context mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ALARMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ALARMS_TABLE); //not concerned with users keeping alarms on upgrade at this time
        onCreate(db);
    }


    private Alarm getValuesForAlarm(Cursor c)
    {
        Alarm alarm = new Alarm();
        alarm.id = c.getLong(c.getColumnIndex(BaseColumns._ID));
        alarm.name = c.getString(c.getColumnIndex(COLUMN_ALARM_NAME));
        alarm.hour = c.getInt(c.getColumnIndex(COLUMN_ALARM_HOUR));
        alarm.minute = c.getInt(c.getColumnIndex(COLUMN_ALARM_MINUTE));
        alarm.tone = c.getString(c.getColumnIndex(COLUMN_TONE)) != "" ? Uri.parse(c.getString(c.getColumnIndex(COLUMN_TONE))) : null;


        String days = c.getString(c.getColumnIndex(COLUMN_DAYS));
        for(int i = 0; i < days.length(); i++)
        {
            if (days.charAt(i) == 'T')
                alarm.setDay(i, true);
            else
                alarm.setDay(i, false);
        }

        alarm.numCorrectNeeded = c.getInt(c.getColumnIndex(COLUMN_CORRECT_ANSWERS));
        if (c.getInt(c.getColumnIndex(COLUMN_ENABLED)) == 0)
            alarm.enabled = false;
        else
            alarm.enabled = true;

        return alarm;
    }


    private ContentValues getValuesForDB(Alarm alarm)
    {
        ContentValues values = new ContentValues();

        values.put(COLUMN_ALARM_NAME, alarm.name);
        values.put(COLUMN_ALARM_HOUR, alarm.hour);
        values.put(COLUMN_ALARM_MINUTE, alarm.minute);
        values.put(COLUMN_CORRECT_ANSWERS, alarm.numCorrectNeeded);
        values.put(COLUMN_ENABLED, alarm.enabled);
        values.put(COLUMN_TONE, alarm.tone != null ? alarm.tone.toString() : "");

        String days = "";
        for (int i = 0; i < 7; i++)
        {
            if(alarm.getDay(i))
                days += 'T';
            else
                days += 'F';
        }

        values.put(COLUMN_DAYS, days);
        return values;
    }


    public long createAlarm(Alarm alarm)
    {
        ContentValues values = getValuesForDB(alarm);
        return getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    public long updateAlarm(Alarm alarm)
    {
        ContentValues values = getValuesForDB(alarm);
        return getWritableDatabase().update(TABLE_NAME, values, BaseColumns._ID + " = ?",
                new String[]{String.valueOf(alarm.id)});
    }


    public Alarm getAlarm(long id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_Select = "SELECT * FROM " + TABLE_NAME + " WHERE " + BaseColumns._ID + " = " + id;

        Cursor c = db.rawQuery(SQL_Select, null);
        if(c.moveToNext())
            return getValuesForAlarm(c);
        return null;
    }

    public List<Alarm> getAllAlarms()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_Select_All = "SELECT * FROM " + TABLE_NAME;

        Cursor c = db.rawQuery(SQL_Select_All, null);

        List<Alarm> alarmList = new ArrayList<Alarm>();
        while(c.moveToNext())
        {
            alarmList.add(getValuesForAlarm(c));
        }
        return alarmList;



    }

    public int deleteAlarm(long id)
    {
        return getWritableDatabase().delete(TABLE_NAME, BaseColumns._ID + " = ? ",
                new String[]{String.valueOf(id)});
    }
}

