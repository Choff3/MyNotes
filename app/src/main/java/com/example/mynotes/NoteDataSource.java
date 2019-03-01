package com.example.mynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class NoteDataSource {

    private SQLiteDatabase database;
    private NoteDBHelper dbHelper;

    public NoteDataSource(Context context) {
        dbHelper = new NoteDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertNote(Note n) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("title", n.getTitle());
            initialValues.put("content", n.getContent());
            initialValues.put("date", String.valueOf(n.getDate().getSeconds()));
            initialValues.put("importance", String.valueOf(n.getImportance()));

            didSucceed = database.insert("note", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateNote(Note n) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) n.getNoteID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("title", n.getTitle());
            updateValues.put("content", n.getContent());
            updateValues.put("date", String.valueOf(n.getDate().getSeconds()));
            updateValues.put("importance", String.valueOf(n.getImportance()));

            didSucceed = database.update("note", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastNoteId() {
        int lastId = -1;
        try {
            String query = "Select MAX(_id) from note";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<String> getNoteTitle() {
        ArrayList<String> noteTitles = new ArrayList<String>();
        try {
            String query = "Select title from note";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                noteTitles.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            noteTitles = new ArrayList<String>();
        }
        return noteTitles;
    }

    public ArrayList<Note> getNotes(String sortField) {
        ArrayList<Note> notes = new ArrayList<Note>();
        try {
            String query = "SELECT  * FROM note ORDER BY " + sortField;

            Cursor cursor = database.rawQuery(query, null);

            Note newNote;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newNote = new Note();                                          //1
                newNote.setNoteID(cursor.getInt(0));
                newNote.setTitle(cursor.getString(1));
                newNote.setContent(cursor.getString(2));
                newNote.setImportance(Integer.valueOf(cursor.getString(3)));
                Date date = new Date();                         //2
                date.setSeconds(Integer.valueOf(cursor.getString(4)));
                newNote.setDate(date);

                notes.add(newNote);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            notes = new ArrayList<Note>();
        }
        return notes;
    }

    public boolean deleteNote(int noteId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("note", "_id=" + noteId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }

    public Note getSpecificNote(int noteId) {
        Note note = new Note();
        String query = "SELECT  * FROM note WHERE _id =" + noteId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            note.setNoteID(cursor.getInt(0));
            note.setTitle(cursor.getString(1));
            note.setContent(cursor.getString(2));
            note.setImportance(Integer.valueOf(cursor.getString(3)));
            Date date = new Date();                         //2
            date.setSeconds(Integer.valueOf(cursor.getString(4)));
            note.setDate(date);

            cursor.close();
        }
        return note;
    }

}