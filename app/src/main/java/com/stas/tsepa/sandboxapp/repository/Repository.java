package com.stas.tsepa.sandboxapp.repository;

import java.util.List;

public interface Repository<Item> {
    void close();
    void addAll(List<Item> items);
    List<Item> getAll();
    int getCount();
    void clear();

    interface Callback <Item> {
        void onDataAppended(List<Item> items);
        void onDataDeleted();
    }
}
