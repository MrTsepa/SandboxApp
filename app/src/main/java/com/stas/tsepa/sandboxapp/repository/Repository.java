package com.stas.tsepa.sandboxapp.repository;

import java.util.List;

public interface Repository<Item, Id> {
    void addAll(List<Item> items);
    List<Item> getAll();
    Item get(Id id);
    int getCount();
    void clear();

    interface Callback <Item> {
        void onDataAppended(List<Item> items);
        void onDataDeleted();
    }
}
