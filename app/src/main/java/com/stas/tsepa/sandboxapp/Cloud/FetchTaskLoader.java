package com.stas.tsepa.sandboxapp.cloud;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.stas.tsepa.sandboxapp.LectureItem;
import com.stas.tsepa.sandboxapp.repository.LectureItemRepository;

import java.io.IOException;
import java.util.List;

public class FetchTaskLoader extends AsyncTaskLoader implements LoaderManager.LoaderCallbacks {

    private static final String LOG_TAG = "MY " + FetchTaskLoader.class.getSimpleName();

    private LectureItemRepository mRepository;
    private Handler mHandler;

    public FetchTaskLoader(Context context, LectureItemRepository mRepository, Handler mHandler) {
        super(context);
        this.mRepository = mRepository;
        this.mHandler = mHandler;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d(LOG_TAG, "onStopLoading");
    }

    @Override
    public Object loadInBackground() {
        Log.d(LOG_TAG, "loadInBackground");

        int pageCount = 0;
        try {
            pageCount = RetrofitLectoryiFetcher.getPageCount();
        } catch (IOException e) {
            Message message = mHandler.obtainMessage();
            message.sendToTarget();
            return null;
        }
        Log.d(LOG_TAG, "page count " + Integer.toString(pageCount));

        int pageLoaded = mRepository.getCount() / RetrofitLectoryiFetcher.PER_PAGE;

        for (int page = pageLoaded + 1; page <= pageCount; page++) {
            if (isAbandoned()) {
                return null;
            }
            List<LectureItem> items;
            try {
                items = RetrofitLectoryiFetcher.fetch(page);
                if (page == 3)
                    throw new IOException();
            } catch (IOException e) {
                Message message = mHandler.obtainMessage();
                message.sendToTarget();
                return null;
            }
            mRepository.addAll(items);
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        return this;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Log.d(LOG_TAG, "onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
