package com.stas.tsepa.sandboxapp.cloud.courseview;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CourseLectureCountFetcher {
    public static int fetch(String guid) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lectoriy.mipt.ru/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LectoryiCourseViewAPI lectoryiCourseViewAPI = retrofit.create(LectoryiCourseViewAPI.class);
        Response<CourseViewAPIItem> response;
        try {
            response = lectoryiCourseViewAPI.loadItems(guid).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return response.body().lectures.size();
    }
}
