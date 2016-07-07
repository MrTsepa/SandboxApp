package com.stas.tsepa.sandboxapp.repository;

import com.stas.tsepa.sandboxapp.cloud.courseview.CourseLectureCountFetcher;

public class CloudCourseLectureCountGetter implements CourseLectureCountGetter {
    @Override
    public int get(String guid) {
        return CourseLectureCountFetcher.fetch(guid);
    }
}
