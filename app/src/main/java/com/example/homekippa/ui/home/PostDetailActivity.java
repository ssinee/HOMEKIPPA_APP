package com.example.homekippa.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homekippa.MainActivity;
import com.example.homekippa.R;
import com.example.homekippa.data.CommentData;
import com.example.homekippa.data.CommentGetResponse;
import com.example.homekippa.data.CommentResponse;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.UserData;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.ListCommentAdapter;
import com.example.homekippa.ui.group.ListPostAdapter;
import com.example.homekippa.ui.group.ListPostImageAdapter;
import com.example.homekippa.ui.group.SingleItemComment;
import com.example.homekippa.ui.group.SingleItemPost;
import com.example.homekippa.ui.group.SingleItemPostImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "postDetail";

    private PostViewModel viewModel;

    private GroupData group;
    private UserData user;
    private Intent intent;

    int postPosition;

    private SingleItemPost post;
    ImageView postGroupProfile;
    TextView postGroupName;
    TextView postNickName;
    TextView postGroupLocation;
    TextView postTitle;
    TextView postContent;
    TextView postLikeNum;
    TextView postCommentNum;

    TextView comment;
    ArrayList<CommentData> comment_List = new ArrayList<>();
    ArrayList<UserData> user_List = new ArrayList<>();
    ArrayList<GroupData> group_List = new ArrayList<>();

    RecyclerView recyclerView_postImages;
    RecyclerView recyclerView_postComments;
    ArrayList<SingleItemPostImage> post_ImageList;
    EditText commentInput;
    private TextView postComment;
    private int commentNum;

    private ServiceApi service;


    @Override
    public void onBackPressed() {
        Log.d("THIS IS COMMENT NUM", "sdfsdfsdf");
        intent.putExtra("commentnum", String.valueOf(commentNum));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        setPostDetail();

        postComment = (TextView) findViewById(R.id.textView_postComment);
        commentInput = (EditText) findViewById(R.id.editText_comment);

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable str_comment = commentInput.getText();
                //TODO change the date format
                String date_comment = "2020-11-11";

                CommentData commentData = new CommentData(post.getPostId(), user.getUserId(), str_comment.toString(), date_comment);
                service.setComment(commentData).enqueue(new Callback<CommentResponse>() {
                    @Override
                    public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                        if (response.code() == 200) {
                            Log.d("comment", "success");
                            commentInput.setText(null);

                            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            setPostComment(recyclerView_postComments);

//                            Log.d("comment", viewModel.getCounter().getValue().toString());
                            PostViewModel.increase(postPosition);
                            setPostDetail();


                        }
                    }

                    @Override
                    public void onFailure(Call<CommentResponse> call, Throwable t) {
                        Log.d("comment", "fail");
                    }
                });
            }
        });


    }

    private void setPostDetail() {

        intent = getIntent();
        post = (SingleItemPost) intent.getExtras().get("post");
        group = (GroupData) intent.getExtras().get("group");
        user = (UserData) intent.getExtras().get("user");
        postPosition = (int) intent.getExtras().get("pos");


        Log.d("post group", group.getName());

//        postGroupProfile = (ImageView) findViewById(R.id.imageView_DetailPostGroupProfile);
        postGroupName = (TextView) findViewById(R.id.textView__DetailPostGroupName);
        postGroupLocation = (TextView) findViewById(R.id.textView__DetailPostGroupLocation);
        postTitle = (TextView) findViewById(R.id.textView_DetailPostTitle);
        postContent = (TextView) findViewById(R.id.textView_DetailPostContent);
        postLikeNum = (TextView) findViewById(R.id.textView_LikedNum);
        postCommentNum = (TextView) findViewById(R.id.textView_commentNum);
        recyclerView_postImages = (RecyclerView) findViewById(R.id.listview_DetailPostImages);
        recyclerView_postComments = (RecyclerView) findViewById(R.id.listview_PostComments);

//        postGroupProfile.setImageResource(post.getGroupPostProfile());
        post_ImageList = post.getGroupPostImage();
        postGroupName.setText(group.getName());
        postGroupLocation.setText(group.getAddress());
        postTitle.setText(post.getTitle());
        postContent.setText(post.getContent());
        postLikeNum.setText(String.valueOf(post.getLikeNum()));
//        postCommentNum.setText(String.valueOf(post.getCommentNum()));

        viewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(PostViewModel.class);
        postCommentNum.setText(String.valueOf(viewModel.getPostList().getValue().get(postPosition).getCommentNum()));

        setPostImage(post_ImageList);
        commentNum = post.getCommentNum();
        setPostComment(recyclerView_postComments);
    }

    private void setPostImage(ArrayList<SingleItemPostImage> post_ImageList) {
        ListPostImageAdapter adapter = new ListPostImageAdapter(post_ImageList);
        recyclerView_postImages.setLayoutManager(new LinearLayoutManager(this
                , LinearLayoutManager.HORIZONTAL
                , false));
        recyclerView_postImages.setAdapter(adapter);
    }

    //TODO: Set Post Comment
    private void setPostComment(RecyclerView listView) {
        Log.d("comment", "setoistcomnt");
        service.getComment(post.getPostId()).enqueue(new Callback<CommentGetResponse>() {
            @Override
            public void onResponse(Call<CommentGetResponse> call, Response<CommentGetResponse> response) {
                if (response.code() == 200) {

                    CommentGetResponse commentGetResponse = response.body();

                    comment_List = commentGetResponse.getComments();
                    user_List = commentGetResponse.getUsers();
                    group_List = commentGetResponse.getGroups();
                    ArrayList<SingleItemComment> comments = new ArrayList<>();

                    //TODO: Change the image of GROUP
                    for (int i = 0; i < comment_List.size(); i++) {
                        Log.d("comment", comment_List.get(i).getContent());
                        SingleItemComment comment = new SingleItemComment(R.drawable.dog_thang, group_List.get(i).getName(), user_List.get(i).getUserName(), group_List.get(i).getAddress(), comment_List.get(i).getContent());
                        comments.add(comment);
                    }

                    ListCommentAdapter commentAdapter = new ListCommentAdapter(getApplicationContext(), comments, comment_List);
                    listView.setAdapter(commentAdapter);

                    LinearLayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext());
                    pLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    listView.setLayoutManager(pLayoutManager);
                    listView.setItemAnimator(new DefaultItemAnimator());

                }
            }

            @Override
            public void onFailure(Call<CommentGetResponse> call, Throwable t) {

            }
        });

//        SingleItemComment comment = new SingleItemComment(R.drawable.dog_thang, "땡이네 콩 ", groupCommentNickName, "경기도 용인시 기흥구 흥덕중앙로", "어머나!!!!!넘모 귕영웡용 ");
//        comment_List.add(comment);


    }

    public SingleItemPost getPost() {
        return post;
    }
}