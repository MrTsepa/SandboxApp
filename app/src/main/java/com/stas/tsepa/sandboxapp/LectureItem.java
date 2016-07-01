package com.stas.tsepa.sandboxapp;

import java.io.Serializable;

public class LectureItem implements Serializable{
    private String title;
    private Integer duration;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
