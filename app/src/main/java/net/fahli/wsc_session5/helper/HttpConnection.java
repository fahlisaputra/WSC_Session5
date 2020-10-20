package net.fahli.wsc_session5.helper;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpConnection {
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }

            sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return sb.toString();
    }

    private String Request(String url, String requestMethod, HashMap<String, String> params) {
        URL path;
        String response = "";
        try {
            path = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) path.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setRequestMethod(requestMethod);
            connection.setAllowUserInteraction(false);
            connection.setInstanceFollowRedirects(true);

            if (requestMethod.equals("POST")) {
                connection.setDoInput(true);
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                try {
                    writer.write(getPostDataString(params));
                    writer.flush();
                    writer.close();
                    outputStream.close();
                } catch (Exception ignored) {}


            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = reader.readLine();
            }
        } catch (Exception ignored) {}
        return response;
    }


        public static class sendRequest extends AsyncTask<String, Void, String> {
            WeakReference<HttpCallback> callback;
            int requestCode;
            HashMap<String, String> params;
            String requestMethod;

            public sendRequest(HttpCallback callback, int requestCode, HashMap<String, String> params, String requestMethod) {
                this.callback = new WeakReference<>(callback);
                this.requestCode = requestCode;
                this.params = params;
                this.requestMethod = requestMethod;
            }

            @Override
            protected void onPostExecute(String s) {
                HttpCallback listener = callback.get();
                if (listener != null) {
                    listener.onCompleted(s, requestCode);
                }
            }

            @Override
            protected String doInBackground(String... strings) {
                HttpConnection connection = new HttpConnection();
                String result = "";
                try {
                    result = connection.Request(strings[0], requestMethod, params);
                } catch (Exception ignored) {}
                return result;
            }
        }

}
