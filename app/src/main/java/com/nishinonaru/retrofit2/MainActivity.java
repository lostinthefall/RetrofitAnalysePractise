package com.nishinonaru.retrofit2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        test1();


    }

    private void test1() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                test();
                return null;
            }
        }.execute();
    }

    String TAG = "N_N_N";
    String URL_BASE = "https://api.github.com";
    ApiStore mApiStore;

    private void test() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiStore = retrofit.create(ApiStore.class);

        try {
            Response<DataBean> response = mApiStore.thisIsAApiStoreMethod().execute();
            String url = response.body().getUrl();
            Log.d(TAG, "test: url= " + url);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    interface ApiStore {
        @GET("/users/basil2style")
        Call<DataBean> thisIsAApiStoreMethod();
    }

}
