package com.stas.tsepa.sandboxapp.repository;

import com.stas.tsepa.sandboxapp.LectureItem;

import java.util.List;

public interface LectureItemRepository {
    void close();
    void addAll(List<LectureItem> items);
    List<LectureItem> getAll();
    int getCount();
    void clear();

    interface Callback {
        void onDataAppended(List<LectureItem> lectureItems);
        void onDataDeleted();
    }
}
