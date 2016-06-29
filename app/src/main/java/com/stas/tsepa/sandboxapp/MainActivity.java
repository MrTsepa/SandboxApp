package com.stas.tsepa.sandboxapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private LectureItemAdapter lecturesAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lecturesAdapter = new LectureItemAdapter(new ArrayList<LectureItem>());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lectures_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(lecturesAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateLecturesList();
    }

    private void updateLecturesList() {
        new FetchLecturesPageTask().execute();
    }

    private class FetchLecturesPageTask extends AsyncTask<Void, Void, List<LectureItem> > {

        private final String LOG_TAG = FetchLecturesPageTask.class.getSimpleName();

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
            lecturesAdapter.addAll(lectureItems);
        }
    }

    private class LectureItemAdapter extends RecyclerView.Adapter<LectureItemAdapter.ViewHolder> {

        private List<LectureItem> objects;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView titleTextView;
            public TextView durationTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.titleTextView = (TextView) itemView
                        .findViewById(R.id.title_text_view);
                this.durationTextView = (TextView) itemView
                        .findViewById(R.id.duration_text_view);
            }
        }

        public LectureItemAdapter(List<LectureItem> objects) {
            this.objects = objects;
            notifyDataSetChanged();
        }

        public void add(LectureItem item) {
            objects.add(item);
            notifyDataSetChanged();
        }

        public void addAll(List<LectureItem> items) {
            objects.addAll(items);
            notifyDataSetChanged();
        }

        public void clear() {
            objects.clear();
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lectures_list_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            LectureItem lectureItem = objects.get(position);
            if (lectureItem != null) {
                if (holder.titleTextView != null) {
                    holder.titleTextView.setText(lectureItem.getTitle());
                }
                if (holder.durationTextView != null) {
                    holder.durationTextView.setText(convertDurationToString(lectureItem.getDuration()));
                }
            }
        }

        @Override
        public int getItemCount() {
            return objects.size();
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
