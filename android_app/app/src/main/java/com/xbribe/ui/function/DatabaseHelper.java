package com.xbribe.ui.function;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.prefs.PreferenceChangeEvent;

import static android.os.Build.ID;

public class DatabaseHelper  extends SQLiteOpenHelper
{
    public static  final String DATABASE_NMAE="CaseDetails.db";
    public static final String TABLE_NAME="Case_table";
    public static final String  COL_1="ID";
    public static final String  COL_2="TOKEN";
    public static final String COL_3="ADDRESS";
    public static final String COL_4="DESCRIPTION";
    public static final String COL_5="MINISTRY";
    public static  final String COL_6="DEPARTMENT";
    public static final String  COL_7="ORGANISATIONNAME";
    public static final String  COL_8="IMAGECOUNT";
    public static final String  COL_9="AUDIOCOUNT";
    public static final String  COL_10="VIDEOCOUNT";
    public static final String COL_11="CASEPROCESS";
    public static final String COL_12="CASEID";
    public static final String COL_13="USERID";
    public static final String COL_14="EMAIL";
    public static final String COL_15="OFFICIALNAME";
    public static final String COL_16="DATE";


   public DatabaseHelper(Context context)
   {
        super(context, DATABASE_NMAE,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,TOKEN TEXT,ADDRESS TEXT,DESCRIPTION TEXT,MINISTRY TEXT,DEPARTMENT TEXT,ORGANISATIONNAME TEXT ,IMAGECOUNT INTEGER,AUDIOCOUNT INTEGER,VIDEOCOUNT INTEGER,CASEPROCESS TEXT,CASEID TEXT,USERID TEXT,EMAIL TEXT,OFFICIALNAME TEXT,DATE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
     onCreate(db);
    }
    public boolean insertData(String token,String address,String description,String ministry,String department,String organisationname,Integer imagecount,Integer  audiocount,Integer videocount,String caseprocess,String caseid,String userid,String email,String officialName,String date)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //contentValues.put(COL_1,ID);
        contentValues.put(COL_2,token);
        contentValues.put(COL_3,address);
        contentValues.put(COL_4,description);
        contentValues.put(COL_5,ministry);
        contentValues.put(COL_6,department);
        contentValues.put(COL_7,organisationname);
        contentValues.put(COL_8,imagecount);
        contentValues.put(COL_9,audiocount);
        contentValues.put(COL_10,videocount);
        contentValues.put(COL_11,caseprocess);
        contentValues.put(COL_12,caseid);
        contentValues.put(COL_13,userid);
        contentValues.put(COL_14,email);
        contentValues.put(COL_15,officialName);
        contentValues.put(COL_16,date);
        long success=db.insert(TABLE_NAME,null,contentValues);
        if(success==-1)
            return  false;
        else
            return  true;

    }
    public Cursor getAllDetails()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from "+TABLE_NAME,null);
        return cursor;
    }
    public Cursor getrowdetails(int position)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME +" WHERE ID = ?",new String[]{String.valueOf(position+1)}, null);
        return cursor;

    }



}
