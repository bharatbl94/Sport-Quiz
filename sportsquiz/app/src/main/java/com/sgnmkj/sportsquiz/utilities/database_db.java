package com.sgnmkj.sportsquiz.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SGNMKJ on 26-Feb-16.
 */
public class database_db {
    entry_data endb;
    static final String file_name="Record.db";
    final int version_number=1;
    private SQLiteDatabase sq;
    public database_db(Context c)
    {
        endb=new entry_data(c, file_name, null, version_number);
    }
    public database_db open()
    {
        sq=endb.getWritableDatabase();
        return this;
    }
    public Cursor getdata(String table)
    {
        return sq.query(table, null, null, null, null, null, null);
    }
    public Cursor getquery_num(String table,String order_num) {
        return sq.rawQuery("Select * from "+table+" WHERE Ordernum="+order_num,null);
    }
    public Cursor getquery_status(String table,int status_code) {
        //     Log.e("position","at getdata");
        return sq.rawQuery("Select * from "+table+" WHERE Status="+status_code,null);
    }
    public int delete_record(String table,String order_num) {
        //     Log.e("position","at getdata");
        return sq.delete(table,"Ordernum = ? ",new String[]{order_num});
    }

    public long insert_data(String table,ContentValues value)
    {
  //      Log.e("position","at insert_data");
        return sq.insert(table, null, value);
    }

    public void close()
    {
        sq.close();
    }
}

class entry_data extends SQLiteOpenHelper
{

    public entry_data(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase sqdb) {
        sqdb.execSQL("CREATE TABLE IF NOT EXISTS 'Quiz_Data'(question text NOT NULL,correct_ans text not null,incorrect1 text not null,incorrect2 text not null,incorrect3 text not null);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          onCreate(db);
    }

}