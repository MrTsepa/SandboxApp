package com.stas.tsepa.sandboxapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LectoryiLecturesAPI {
    @GET("lecture?token=E3yK8syiWdH6du")
    Call<List<LectureItem>> loadItems(@Query("page") int page);

}
