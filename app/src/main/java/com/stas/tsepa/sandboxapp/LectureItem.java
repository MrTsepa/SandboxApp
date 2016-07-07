package com.stas.tsepa.sandboxapp;

import java.util.ArrayList;
import java.util.List;

public class LectureItem {
    private String title;
    private Integer duration;
    private List<CourseItem> courses;

    public LectureItem(String title, Integer duration, CourseItem course) {
        this.courses = new ArrayList<>();
        this.courses.add(course);
        this.duration = duration;
        this.title = title;
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
}
