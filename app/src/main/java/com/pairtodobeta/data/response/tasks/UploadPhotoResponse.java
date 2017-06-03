package com.pairtodobeta.data.response.tasks;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilyasavin on 3/11/17.
 */

public class UploadPhotoResponse {


    @SerializedName("image")
    public String imageUrl;

    @SerializedName("image_temporary_name")
    public String imageTempName;

}
