package com.johannes2002895.notes.db;


import static com.johannes2002895.notes.db.DatabaseContract.NoteColumns.TABLE_NAME;
import  com.johannes2002895.notes.db.DatabaseContract.NoteColumns;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//akan mengextend kelas SQLiteHelper
public class DatabaseHelper extends SQLiteOpenHelper {
    //proses DDL disini

    private static final String DATABASE_NAME = "notes";
    private static final int DATABASE_VERSION  = 1;

    private static final String SQL_CREATE_TABLE_NOT = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_NAME,
            NoteColumns._ID,
            NoteColumns.TITLE,
            NoteColumns.DESCRIPTION,

            NoteColumns.DATE
    );
    //panggil construtor kelas
    DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_NOT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
