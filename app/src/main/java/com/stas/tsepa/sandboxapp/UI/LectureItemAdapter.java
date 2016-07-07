package com.stas.tsepa.sandboxapp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stas.tsepa.sandboxapp.LectureItem;
import com.stas.tsepa.sandboxapp.R;

import java.util.ArrayList;
import java.util.List;

class LectureItemAdapter
        extends RecyclerView.Adapter<LectureItemAdapter.ViewHolder> {

    private List<LectureItem> objects;

    private OnItemClickListener onItemClickListener = null;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleTextView;
        public TextView durationTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.titleTextView = (TextView) itemView
                    .findViewById(R.id.title_text_view);
            this.durationTextView = (TextView) itemView
                    .findViewById(R.id.duration_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public LectureItemAdapter(List<LectureItem> objects) {
        if (objects == null)
            this.objects = new ArrayList<>();
        else
            this.objects = objects;
        notifyDataSetChanged();
    }

    public LectureItemAdapter() {
        this.objects = new ArrayList<>();
    }

    public void addAll(List<LectureItem> items) {
        if (items != null) {
            objects.addAll(items);
        }
    }

    public void clear() {
        objects.clear();
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

    public LectureItem getItemAt(int position) {
        if (position >= objects.size() || position < 0) {
            return null;
        }
        else {
            return objects.get(position);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private String convertDurationToString(Integer duration) {
        Integer dur_secs = duration / 1000;
        Integer hours = dur_secs / 3600;
        Integer mins = (dur_secs % 3600) / 60;
        Integer secs = (dur_secs % 60);
        return hours.toString() + ':' + mins.toString() + ':' + secs.toString();
    }
}
