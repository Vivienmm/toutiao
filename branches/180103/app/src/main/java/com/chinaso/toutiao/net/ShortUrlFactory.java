package com.chinaso.toutiao.net;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShortUrlFactory {
    FactoryCallBack callBack;
    String urlString;

    public ShortUrlFactory(FactoryCallBack back) {
        this.callBack = back;
    }

    public void factoryShortUrl(String url) throws UnsupportedEncodingException {
        urlString = URLDecoder.decode(url, "UTF-8");
        urlString = urlString.replace("　", "");
        urlString = urlString.replace(" ", "");
        final String urlFinal = urlString;
        OkHttpClient client = new OkHttpClient();
        FormBody requestBody = new FormBody.Builder().add("longurl", urlFinal).build();
        Request request = new Request.Builder().url("http://t.chinaso.com/shorten").post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    jsonAnalysis(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void jsonAnalysis(String jsonString) throws JSONException {
        JSONObject resultobj = new JSONObject(jsonString);

        Boolean result = resultobj.getBoolean("result");

        if (result) {
            String shorturl = resultobj.getString("shorturl");
            callBack.callBack(shorturl);
        } else {
            callBack.callBack(urlString);
        }
    }

    public interface FactoryCallBack {
        void callBack(String url);
    }
}
