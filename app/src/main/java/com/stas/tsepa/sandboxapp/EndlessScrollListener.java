package com.stas.tsepa.sandboxapp;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private final String LOG_TAG = "MY " + EndlessScrollListener.class.getSimpleName();

    private int currentPage = 0;
    private int totalItemCount = 0;
    private boolean loading = true;

    LinearLayoutManager linearLayoutManager;

    public EndlessScrollListener(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            this.linearLayoutManager = (LinearLayoutManager) layoutManager;
        }
        else {
            //TODO
        }
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {

        int lastVisibleItemPosition;
        int totalItemCount = linearLayoutManager.getItemCount();
        lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

        if (totalItemCount < this.totalItemCount) {
            this.currentPage = 0;
            this.totalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        if (loading && (totalItemCount > this.totalItemCount)) {
            loading = false;
            this.totalItemCount = totalItemCount;
        }

        if (!loading && lastVisibleItemPosition + 1 >= totalItemCount) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
//        Log.d(LOG_TAG, "onScrolled, lastVisibleItem " + Integer.toString(lastVisibleItemPosition)
//                + ", totalItemCount " + Integer.toString(this.totalItemCount));
    }

    public abstract void onLoadMore(int page);
}
