package com.example.homekippa;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.LoadPath;
import com.example.homekippa.function.Loading;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;

import java.io.InputStream;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
    private String url;
    private ImageView imageView;
    private Cache cache;
    private Boolean ispost;

    private Context context;
    private ServiceApi service;
    private Bitmap bitmap;

    private Loading loading;

    public ImageLoadTask(String url, ImageView imageView, Context context, boolean ispost) {
        this.url = url;
        this.imageView = imageView;
        this.context = context;
        this.ispost = ispost;
        service = RetrofitClient.getClient().create(ServiceApi.class);
        loading = new Loading();
        cache = new Cache(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {

        Log.d("bitmap", "1");
        try {
            String[] w = url.split("/");
            String key = w[w.length - 1];
            service.getProfileImage(url).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        InputStream is = response.body().byteStream();
                        Bitmap b = BitmapFactory.decodeStream(is);
                        bitmap = b;

                        if (bitmap != null)
                            cache.saveBitmapToJpeg(bitmap, key);
                    } else {
                        Log.d("Yes", "server contact failed");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "이미지 로드 에러 발생 ", Toast.LENGTH_SHORT).show();
                    Log.e("createGroup error", t.getMessage());
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(Bitmap b) {
        Log.d("bitmap", "3");
        super.onPostExecute(b);
        if (b != null) {
            Log.d("bitmap", b.toString());
            if (ispost) {
                imageView.setImageBitmap(b);
            } else {
                Glide.with(context).load(b).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).circleCrop().into(imageView);
            }
        }
        imageView.invalidate();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
