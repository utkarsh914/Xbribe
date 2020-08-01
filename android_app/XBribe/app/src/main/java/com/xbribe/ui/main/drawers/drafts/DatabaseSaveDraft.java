package com.xbribe.ui.main.drawers.drafts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSaveDraft extends SQLiteOpenHelper
{
    public static final String DATABASE_NMAE="Draftdata.db";
    public static final String TABLE_NAME="Draft_table";
    public static final String  COL_1="ID";
    public static final String  COL_2="MINISTRY";
    public static final String COL_3="ADDRESS";
    public static final String COL_4="PINCODE";
    public static final String COL_5="CITY";
    public static final String COL_6="DEPARTMENT";
    public static final String  COL_7="ORGANISATIONNAME";
    public static final String  COL_8="DESCRIPTION";
    public static final String COL_9="EMAIL";
    public static final String COL_10="LATITUDE";
    public static final String COL_11="LONGITUDE";
    public static final String COL_12="OFFICIALNAME";
    public static final String COL_13="MINISTRYID";

    public DatabaseSaveDraft(Context context)
    {
        super(context, DATABASE_NMAE,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,MINISTRY TEXT,ADDRESS TEXT,PINCODE TEXT,CITY TEXT,DEPARTMENT TEXT,ORGANISATIONNAME TEXT ,DESCRIPTION TEXT,EMAIL TEXT,LATITUDE TEXT,LONGITUDE TEXT,OFFICIALNAME TEXT,MINISTRYID TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String ministry,String address,String pincode,String city,String department,String organisationname,String description,String email,String latitude,String longitude,String officialName, String ministryID)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
       // contentValues.put(COL_1,ID);
        contentValues.put(COL_2,ministry);
        contentValues.put(COL_3,address);
        contentValues.put(COL_4,pincode);
        contentValues.put(COL_5,city);
        contentValues.put(COL_6,department);
        contentValues.put(COL_7,organisationname);
        contentValues.put(COL_8,description);
        contentValues.put(COL_9,email);
        contentValues.put(COL_10,latitude);
        contentValues.put(COL_11,longitude);
        contentValues.put(COL_12,officialName);
        contentValues.put(COL_13,ministryID);

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
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME +" WHERE ID = ?",new String[]{String.valueOf(position)}, null);//Position has been increased to correspond the id
        return cursor;

    }
    public Integer deleteData(String id)
    {
        SQLiteDatabase db =this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[] {id});
    }


}
