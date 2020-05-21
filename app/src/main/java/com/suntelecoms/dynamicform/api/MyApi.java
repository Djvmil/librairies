package com.suntelecoms.dynamicform.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MyApi {
    @Multipart
    @POST("add-annonce")
    Call<ResponseAddAnnonce> getAdd_Annonce(@Header("Authorization") String authToken,
                                            @Part MultipartBody.Part file,
                                            @Part("name") RequestBody name
                                            //@Part("tArabic") String arabicTitle,
                                            //@Part("tEnglish") String englishTitle,
                                            //@Part("tRussian") String russianTitle
    );
}
