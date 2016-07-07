package com.stas.tsepa.sandboxapp.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.stas.tsepa.sandboxapp.cloud.FetchTaskLoader;
import com.stas.tsepa.sandboxapp.LectureItem;
import com.stas.tsepa.sandboxapp.R;
import com.stas.tsepa.sandboxapp.repository.LectureItemRepository;
import com.stas.tsepa.sandboxapp.repository.LectureItemSQLiteDB;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements LectureItemSQLiteDB.Callback, LoaderManager.LoaderCallbacks<List<LectureItem>> {

    private static final String LOG_TAG = "MY " + MainActivity.class.getSimpleName();

    private static final int ID_REPOSITORY_LOADER = 1;
    private static final int ID_FETCH_LOADER = 2;

    private LectureItemAdapter mAdapter;
    private LectureItemRepository mRepository;

    private Handler mFetchingErrorHandler = new Handler(Looper.getMainLooper()) {

        private int RETRYING_TIME_SECS = 5;

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(MainActivity.this.getBaseContext(), "Fetching error. Retrying in "
                    + Integer.toString(RETRYING_TIME_SECS) + " seconds", Toast.LENGTH_LONG).show();
            MainActivity.this.getSupportLoaderManager().destroyLoader(ID_FETCH_LOADER);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    MainActivity.this.getSupportLoaderManager().initLoader(ID_FETCH_LOADER, null,
                            new FetchTaskLoader(MainActivity.this, MainActivity.this.mRepository,
                                    mFetchingErrorHandler));
                }
            }, RETRYING_TIME_SECS * 1000);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lectures_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LectureItemAdapter();
        recyclerView.setAdapter(mAdapter);

        mRepository = new LectureItemSQLiteDB(this, this);

        getSupportLoaderManager().initLoader(ID_REPOSITORY_LOADER, null, this);
        getSupportLoaderManager().initLoader(ID_FETCH_LOADER, null,
                new FetchTaskLoader(this, mRepository, mFetchingErrorHandler));

        if (savedInstanceState != null)
            recyclerView.setVerticalScrollbarPosition(savedInstanceState
                            .getInt("LIST_SCROLL_POSITION"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh :
                mRepository.clear();
                getSupportLoaderManager().getLoader(ID_FETCH_LOADER).abandon();
                getSupportLoaderManager().restartLoader(ID_FETCH_LOADER, null,
                        new FetchTaskLoader(this, mRepository, mFetchingErrorHandler));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("LIST_SCROLL_POSITION",
            findViewById(R.id.lectures_recycler_view)
                .getVerticalScrollbarPosition());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDataAppended(List<LectureItem> lectureItems) {
//        Log.d(LOG_TAG, "onDataAppended");
        getSupportLoaderManager().getLoader(ID_REPOSITORY_LOADER).onContentChanged();
    }

    @Override
    public void onDataDeleted() {
//        Log.d(LOG_TAG, "onDataDeleted");
        getSupportLoaderManager().getLoader(ID_REPOSITORY_LOADER).onContentChanged();
    }

    @Override
    public Loader<List<LectureItem>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        return new RepositoryLoader(this, mRepository);
    }

    @Override
    public void onLoadFinished(Loader<List<LectureItem>> loader, List<LectureItem> data) {
//        Log.d(LOG_TAG, "onLoadFinished");
        mAdapter.clear();
        mAdapter.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<LectureItem>> loader) {

    }

    private static class RepositoryLoader extends AsyncTaskLoader<List<LectureItem>> {

        private static final String LOG_TAG = "MY " + RepositoryLoader.class.getSimpleName();

        private LectureItemRepository mRepository;

        public RepositoryLoader(Context context, LectureItemRepository mRepository) {
            super(context);
            this.mRepository = mRepository;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Override
        public List<LectureItem> loadInBackground() {
//            Log.d(LOG_TAG, "loadInBackground");
            return mRepository.getAll();
        }
    }
}
