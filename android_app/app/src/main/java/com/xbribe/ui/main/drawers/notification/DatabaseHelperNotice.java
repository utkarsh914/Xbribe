package com.xbribe.ui.main.drawers.notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperNotice extends SQLiteOpenHelper
{
    public static  final String DATABASE_NMAE="NoticeDetails.db";
    public static final String TABLE_NAME="Notification";
    public static final String  COL_1="ID";
    public static final String COL_2="EMAILADDRESS";
    public static final String COL_3="MESSAGE";
    public static final String COL_4="TITLE";

   public DatabaseHelperNotice(Context context)
   {
       super(context, DATABASE_NMAE,null, 1);
   }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,EMAILADDRESS TEXT,MESSAGE TEXT,TITLE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
     onCreate(db);
    }
    public boolean insertData(String emailaddress,String message,String title)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //contentValues.put(COL_1,ID);
        contentValues.put(COL_2,emailaddress);
        contentValues.put(COL_3,message);
        contentValues.put(COL_4,title);

        long success=db.insert(TABLE_NAME,null,contentValues);
        if(success==-1)
        {
            return  false;
        }
        else
        {
            return  true;
        }

    }
    public Cursor getAllDetails()
    {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from "+TABLE_NAME,null);
        return cursor;
    }
    public Cursor getRowDetails(int position)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME +" WHERE ID = ?",new String[]{String.valueOf(position+1)}, null);
        return cursor;
    }
}
