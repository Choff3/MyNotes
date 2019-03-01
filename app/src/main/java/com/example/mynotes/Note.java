package com.example.mynotes;

import java.util.Date;

public class Note {

    private int noteID;
    private Date date;
    private int importance;
    private String title;
    private String content;

    public Note(){
        this.date = new Date();
        this.noteID = -1;
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public Date getDate() {
        return date;
    }

    public int getImportance() {
        return importance;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
