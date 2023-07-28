package net.ncie.dmv.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.ncie.dmv.bean.QuestionsBean;
import net.ncie.dmv.bean.TopicBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    private static final String TAG = "HttpUtils";

    public static void sendGetRequest(String urlString, final HttpCallback<ArrayList<String>> callback) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();
                    Log.d(TAG, "Response: " + responseString);

                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray resultArray = jsonObject.getJSONArray("result");

                //    JSONArray resultArray = jsonArray.getJSONArray(2);

                    ArrayList<String> resultArrayList = new ArrayList<>();
                    int length = resultArray.length();
                    for (int i = 0; i < length; i++) {
                        String item = resultArray.getString(i);
                        resultArrayList.add(item);
                    }

                    callback.onSuccess(resultArrayList);

                } catch (JSONException e) {
                    Log.e(TAG, "JSON parse failed: " + e.getMessage());
                    callback.onFailure(e);
                }
            }
        });
    }

    public static void sendPostRequest(String urlString, String requestBody, final HttpCallback<String> callback) {


        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(requestBody, mediaType);
        Request request = new Request.Builder()
                .url(urlString)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e);
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null) {
                    try {
                        String responseString = response.body().string();
                        Log.d(TAG, "Response: " + responseString);
                        callback.onSuccess(responseString);
                    } catch (Exception e) {
                        Log.e(TAG, "JSON parse failed: " + e.getMessage());
                        callback.onFailure(e);
                    }
                }
            }
        });
    }

    public static void sendGetRequestWithParams(String urlString, Map<String, String> params, HttpCallback<ArrayList<QuestionsBean>> callback) throws UnsupportedEncodingException {

        // Build the query string from the params map
        StringBuilder queryStringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            queryStringBuilder.append(URLEncoder.encode(key, "UTF-8"));
            queryStringBuilder.append("=");
            queryStringBuilder.append(URLEncoder.encode(value, "UTF-8"));
            queryStringBuilder.append("&");
        }
        String queryString = queryStringBuilder.toString();

        // Build the full URL with the query string
        String fullUrlString = urlString + "?" + queryString;

        MyUtil.MyLog(fullUrlString);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(fullUrlString)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();


                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray resultArray = jsonObject.getJSONArray("result");
                    Gson gson = new Gson();
                    ArrayList<QuestionsBean> list = gson.fromJson(resultArray.toString(), new TypeToken<ArrayList<QuestionsBean>>() {}.getType());


                    callback.onSuccess(list);

                } catch (JSONException e) {
                    Log.e(TAG, "JSON parse failed: " + e.getMessage());
                    callback.onFailure(e);
                }
            }
        });
    }


    public static void getExamQuestions(String urlString, Map<String, String> params, HttpCallback<TopicBean> callback) throws UnsupportedEncodingException {

        // Build the query string from the params map
        StringBuilder queryStringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            queryStringBuilder.append(URLEncoder.encode(key, "UTF-8"));
            queryStringBuilder.append("=");
            queryStringBuilder.append(URLEncoder.encode(value, "UTF-8"));
            queryStringBuilder.append("&");
        }
        String queryString = queryStringBuilder.toString();

        // Build the full URL with the query string
        String fullUrlString = urlString + "?" + queryString;

        MyUtil.MyLog(fullUrlString);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(fullUrlString)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();


                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray resultArray = jsonObject.getJSONArray("result");
                    MyUtil.MyLog(jsonObject);
                    Gson gson = new Gson();
                    TopicBean topicBean = gson.fromJson(jsonObject.toString(), new TypeToken<TopicBean>() {}.getType());


                    callback.onSuccess(topicBean);

                } catch (JSONException e) {
                    Log.e(TAG, "JSON parse failed: " + e.getMessage());
                    callback.onFailure(e);
                }
            }
        });
    }

    public static void getBitmapFromURL(String urlString, final HttpCallback<Bitmap> callback) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    InputStream input = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    input.close();


                    callback.onSuccess(bitmap);

                } catch (Exception e) {
                    Log.e(TAG, "JSON parse failed: " + e.getMessage());
                    callback.onFailure(e);
                }
            }
        });
    }

    public interface HttpCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }
}
