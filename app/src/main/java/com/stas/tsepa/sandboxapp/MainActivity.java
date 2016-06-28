package com.stas.tsepa.sandboxapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LectureItemAdapter lecturesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lecturesAdapter = new LectureItemAdapter(this,
                R.layout.lectures_list_item,
                new ArrayList<LectureItem>());
        ListView listView = (ListView) findViewById(R.id.lectures_list_view);
        listView.setAdapter(lecturesAdapter);
    }

    private class LectureItemAdapter extends ArrayAdapter<LectureItem> {

        private ArrayList<LectureItem> objects;

        public LectureItemAdapter(Context context, int resource, ArrayList<LectureItem> objects) {
            super(context, resource, objects);
            this.objects = objects;
        }

        @Override
        public void add(LectureItem object) {
            super.add(object);
            objects.add(object);
        }

        @Override
        public void clear() {
            super.clear();
            objects.clear();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = getLayoutInflater().inflate(R.layout.lectures_list_item, null);
            }
            else {
                view = convertView;
            }

            TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);
            TextView durationTextView = (TextView) view.findViewById(R.id.duration_text_view);

            LectureItem lectureItem = objects.get(position);
            if (lectureItem != null) {
                if (nameTextView != null) {
                    //TODO
                }
                if (durationTextView != null) {
                    //TODO
                }
            }

            return view;
        }
    }
}
