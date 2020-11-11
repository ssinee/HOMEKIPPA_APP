package com.example.homekippa.ui.group;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.homekippa.R;


public class SingleItemPost implements Parcelable {

    private int id;
    private int group_id;
    private String user_id;
    private String title;
    private String content;
    private String image;
    private String date;
    private int like_num;
    private int comment_num;
    private String scope;
//    private String groupPostLocation;
//    private ArrayList<SingleItemPostImage> groupPostImage;

    public SingleItemPost(int id, int group_id, String user_id, String title, String content, String image, String date, int like_num, int comment_num, String scope) {
        this.id = id;
        this.group_id = group_id;
        this.user_id = user_id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.date = date;
        this.like_num = like_num;
        this.comment_num = comment_num;
        this.scope = scope;
    }

    public int getPostId() {
        return id;
    }

    public int getGroupId() {
        return group_id;
    }

    public String getUserId() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return getDate();
    }

    public int getLikeNum() {
        return like_num;
    }

    public int getCommentNum() {
        return comment_num;
    }

    public String getScope() {
        return scope;
    }

    public void setLikeNum(int like_num) {
        this.like_num = like_num;
    }

    public void setCommentNum(int comment_num) {
        this.comment_num = comment_num;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    protected SingleItemPost(Parcel in) {
        id = in.readInt();
        group_id = in.readInt();
        user_id = in.readString();
        title = in.readString();
        content = in.readString();
        image = in.readString();
        date = in.readString();
        like_num = in.readInt();
        comment_num = in.readInt();
        scope = in.readString();
    }

    public static final Creator<SingleItemPost> CREATOR = new Creator<SingleItemPost>() {
        @Override
        public SingleItemPost createFromParcel(Parcel in) {
            return new SingleItemPost(in);
        }

        @Override
        public SingleItemPost[] newArray(int size) {
            return new SingleItemPost[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(group_id);
        dest.writeString(user_id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(image);
        dest.writeString(date);
        dest.writeInt(like_num);
        dest.writeInt(comment_num);
        dest.writeString(scope);
    }

    //getters and setters

}
