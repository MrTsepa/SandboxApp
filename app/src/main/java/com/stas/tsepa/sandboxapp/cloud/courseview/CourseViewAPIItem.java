package com.stas.tsepa.sandboxapp.cloud.courseview;

import com.stas.tsepa.sandboxapp.CourseItem;
import com.stas.tsepa.sandboxapp.LectureItem;

import java.util.List;

public class CourseViewAPIItem extends CourseItem {
    List<LectureItem> lectures;

    public CourseViewAPIItem(String guid) {
        super(guid);
    }
}
