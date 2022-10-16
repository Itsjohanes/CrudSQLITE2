package com.johannes2002895.notes.helper;

import android.database.Cursor;

import com.johannes2002895.notes.db.DatabaseContract;
import com.johannes2002895.notes.entity.MyNotes;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<MyNotes> mapCursorToArrayList(Cursor cursor){

        ArrayList<MyNotes> noteList = new ArrayList<>();

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE));
            noteList.add(new MyNotes(id,title,description,date));
        }
        return noteList;
    }
}

