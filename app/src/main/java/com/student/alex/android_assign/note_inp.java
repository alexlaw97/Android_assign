package com.student.alex.android_assign;

public class note_inp {
    String title, content ;

    public note_inp(){

    }

    public note_inp(String title, String content){
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public String toString() {
        return this.title + ": " + this.content;
    }

}
