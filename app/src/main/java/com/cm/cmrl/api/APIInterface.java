package com.cm.cmrl.api;

import com.cm.cmrl.model.ImageUploadResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
/**
 * Created by Iddinesh.
 */
public interface APIInterface {

    @Multipart
    @POST("image_upload.php")
    Call<ImageUploadResponse> getImageStroeResponse(@Part MultipartBody.Part file);


}
