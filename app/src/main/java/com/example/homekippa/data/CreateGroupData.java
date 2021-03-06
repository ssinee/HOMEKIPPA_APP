package com.example.homekippa.data;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class CreateGroupData {

    @SerializedName("userId")
    private String userId;

    @SerializedName("groupName")
    private String groupName;

    @SerializedName("groupAddress")
    private  String groupAddress;

    @SerializedName("groupIntroduction")
    private String groupIntroduction;

    @SerializedName("area")
    private String area;

    public  CreateGroupData(String userId, String groupName, String groupAddress, String groupIntroduction, String area){
        this.userId = userId;
        this.groupName = groupName;
        this.groupAddress = groupAddress;
        this.groupIntroduction = groupIntroduction;
        this.area = area;
    }

}
