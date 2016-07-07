package com.stas.tsepa.sandboxapp.cloud;

import android.util.Log;

import com.stas.tsepa.sandboxapp.LectureItem;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitLectoryiFetcher {

    private static final String LOG_TAG = "MY " + RetrofitLectoryiFetcher.class.getSimpleName();

    private static int pageCount = 0;
    static final int PER_PAGE = 2;

    public static List<LectureItem> fetch(int page) throws IOException {

        Log.d(LOG_TAG, "fetch page " + Integer.toString(page));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lectoriy.mipt.ru/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LectoryiLecturesAPI lectoryiLecturesAPI = retrofit.create(LectoryiLecturesAPI.class);
        Response<List<LectureItem>> response;
        try {
            response = lectoryiLecturesAPI.loadItems(page, PER_PAGE).execute();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to fetch data");
            throw e;
        }
        pageCount = Integer.parseInt(response.headers().get("X-Pagination-Page-Count"));
        return response.body();
    }

    public static int getPageCount() throws IOException {
        if (pageCount != 0)
            return pageCount;
        fetch(1);
        return pageCount;
    }
}
