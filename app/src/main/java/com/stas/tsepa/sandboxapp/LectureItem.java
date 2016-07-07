package com.stas.tsepa.sandboxapp;

import java.util.ArrayList;
import java.util.List;

public class LectureItem {
    private String guid;
    private String title;
    private Integer duration;
    private List<CourseItem> courses;
    private String order;

    public LectureItem(String guid, String title, Integer duration, CourseItem course, String order) {
        this.guid = guid;
        this.courses = new ArrayList<>();
        this.courses.add(course);
        this.duration = duration;
        this.title = title;
        this.order = order;
    }

    public LectureItem(String guid, String title, Integer duration) {
        this.guid = guid;
        this.title = title;
        this.duration = duration;
        this.courses = new ArrayList<>();
    }

    public String getGuid() {
        return guid;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    public CourseItem getCourse() {
        if (courses.size() == 0)
            return null;
        return courses.get(0);
    }

    public String getOrder() {
        return order;
    }
}
