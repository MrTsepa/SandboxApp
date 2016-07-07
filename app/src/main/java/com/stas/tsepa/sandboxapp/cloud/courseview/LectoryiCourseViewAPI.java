package com.stas.tsepa.sandboxapp.cloud.courseview;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LectoryiCourseViewAPI {
    @GET("course/{guid}?token=E3yK8syiWdH6du")
    Call<CourseViewAPIItem> loadItems(@Path("guid") String guid);
}
