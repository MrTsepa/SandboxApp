package com.stas.tsepa.sandboxapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LectoryiLecturesAPI {
    @GET("lecture?token=E3yK8syiWdH6du")
    Call<List<LectureItem>> loadItems();

}
