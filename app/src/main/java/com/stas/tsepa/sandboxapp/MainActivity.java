package com.stas.tsepa.sandboxapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        updateLecturesList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateLecturesList();
    }

    private void updateLecturesList() {
        new FetchLecturesList().execute();
    }

    private class FetchLecturesList extends AsyncTask<Void, Void, List<LectureItem> > {

        private final String LOG_TAG = FetchLecturesList.class.getSimpleName();

        @Override
        protected List<LectureItem> doInBackground(Void... voids) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://lectoriy.mipt.ru/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LectoryiLecturesAPI lectoryiLecturesAPI = retrofit.create(LectoryiLecturesAPI.class);
            Response<List<LectureItem>> response;
            try {
                response = lectoryiLecturesAPI.loadItems().execute();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Unable to fetch data");
                return new ArrayList<>();
            }
            return response.body();
        }

        @Override
        protected void onPostExecute(List<LectureItem> lectureItems) {
            super.onPostExecute(lectureItems);
            lecturesAdapter.clear();
            for (LectureItem lectureItem : lectureItems) {
                lecturesAdapter.add(lectureItem);
            }
        }
    }

    private class LectureItemAdapter extends ArrayAdapter<LectureItem> {

        private ArrayList<LectureItem> objects;

        public LectureItemAdapter(Context context, int resource, ArrayList<LectureItem> objects) {
            super(context, resource, objects);
            this.objects = objects;
        }

        @Override
        public void add(LectureItem object) {
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

            TextView titleTextView = (TextView) view.findViewById(R.id.title_text_view);
            TextView durationTextView = (TextView) view.findViewById(R.id.duration_text_view);

            LectureItem lectureItem = objects.get(position);
            if (lectureItem != null) {
                if (titleTextView != null) {
                    titleTextView.setText(lectureItem.getTitle());
                }
                if (durationTextView != null) {
                    durationTextView.setText(convertDurationToString(lectureItem.getDuration()));
                }
            }
            return view;
        }

        private String convertDurationToString(Integer duration) {
            Integer dur_secs = duration/1000;
            Integer hours = dur_secs / 3600;
            Integer mins = (dur_secs%3600) / 60;
            Integer secs = (dur_secs%60);
            return hours.toString() + ':' + mins.toString() + ':' + secs.toString();
        }
    }
}
