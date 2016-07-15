package com.stas.tsepa.sandboxapp.cloud;

import com.stas.tsepa.sandboxapp.LectureItem;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ObservableLecturesAPI {
    @GET("lecture?token=E3yK8syiWdH6du&expand=courses")
    Observable<List<LectureItem>> loadItems(@Query("page") long page, @Query("per-page") int perPage);
}
