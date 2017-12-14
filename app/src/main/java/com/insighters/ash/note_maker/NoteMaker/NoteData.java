package com.insighters.ash.note_maker.NoteMaker;

/**
 * Created by ash on 14/12/17.
 */

public class NoteData {
    private String title;
    private String data;
    private String color;
    private String date;


    public NoteData() {
    }

    public NoteData(String title, String data, String color, String date) {
        this.title = title;
        this.data = data;
        this.color = color;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
