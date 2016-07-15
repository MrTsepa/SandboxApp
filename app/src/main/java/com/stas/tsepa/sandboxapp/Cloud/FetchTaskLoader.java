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
import com.stas.tsepa.sandboxapp.repository.Repository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FetchTaskLoader extends AsyncTaskLoader implements LoaderManager.LoaderCallbacks {

    private static final String LOG_TAG = "MY " + FetchTaskLoader.class.getSimpleName();

    private Repository<LectureItem, String> mRepository;
    private Handler mHandler;

    public FetchTaskLoader(Context context, Repository<LectureItem, String> mRepository, Handler mHandler) {
        super(context);
        this.mRepository = mRepository;
        this.mHandler = mHandler;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

/*    @Override
    public Object loadInBackground() {
        Log.d(LOG_TAG, "loadInBackground");

        int pageCount;
        try {
            pageCount = RetrofitLectoryiFetcher.getPageCount();
        } catch (IOException e) {
            Message message = mHandler.obtainMessage();
            message.sendToTarget();
            return null;
        }
        Log.d(LOG_TAG, "page count " + Integer.toString(pageCount));

        int pageLoaded = mRepository.getCount() / RetrofitLectoryiFetcher.PER_PAGE;

        Log.d(LOG_TAG, "page loaded " + Integer.toString(pageLoaded));

        for (int page = pageLoaded + 1; page <= pageCount; page++) {
            if (isAbandoned()) {
                return null;
            }
            List<LectureItem> items;
            try {
                items = RetrofitLectoryiFetcher.fetch(page);
            } catch (IOException e) {
                Message message = mHandler.obtainMessage();
                message.sendToTarget();
                return null;
            }
            mRepository.addAll(items);
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
*/

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

    @Override
    public Object loadInBackground() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lectoriy.mipt.ru/api/v1/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ObservableLecturesAPI lecturesAPI = retrofit.create(ObservableLecturesAPI.class);

        int pageCount = 10;

        Observable.interval(5, TimeUnit.SECONDS)
                .skip(1)
                .take(pageCount)
                .takeUntil(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return isAbandoned();
                    }
                })
                .flatMap(new Func1<Long, Observable<List<LectureItem>>>() {
                    @Override
                    public Observable<List<LectureItem>> call(Long aLong) {

                        return lecturesAPI.loadItems(aLong, 2);
                    }
                })
                .subscribe(new Action1<List<LectureItem>>() {
                    @Override
                    public void call(List<LectureItem> lectureItems) {
                        mRepository.addAll(lectureItems);
                    }
        });
        return null;
    }
}
