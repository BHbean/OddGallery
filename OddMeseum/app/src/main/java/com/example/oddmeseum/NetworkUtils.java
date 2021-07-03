package com.example.oddmeseum;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {
    private static final String LOG_TAG = com.example.oddmeseum.NetworkUtils.class.getSimpleName();
    // Base URL for Books API.
    private static final String PAINTING_BASE_URL = "https://hello-cloudbase-9gzwa8wu94a21817-1305329478.ap-shanghai.service.tcloudbase.com/api/v1.0/";
    // Parameter for the search string.
    private static String queryCollection;
    private static final String QUERY_PAINTING_LIMIT = "limit";
    private static final String QUERY_PAINTING_SKIP = "skip";
    private static final String QUERY_PAINTING_FIELDS = "fields";
    private static final String QUERY_PAINTING_SORT = "sort";

    static String getPaintingList(LANGUAGES language) {
        switch (language) {
            case ENGLISH: {
                queryCollection = "english_painting";
                break;
            }
            case CHINESE: {
                queryCollection = "paintings";
                break;
            }
        }
        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String paintingJSONString = null;

        try {
            Uri builtURI = Uri.parse(PAINTING_BASE_URL).buildUpon()
                    .appendPath(queryCollection)
                    .build();
            URL requestURL = new URL(builtURI.toString());

            urlConnection = (HttpsURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();
            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));
            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();
            // Read the input line-by-line into the string while there is still input.
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                // Since it's JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }
            if (builder.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            paintingJSONString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.d(LOG_TAG, paintingJSONString);
        return paintingJSONString;
    }

    static ArrayList<Painting> parseJson(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray itemsArray = jsonObject.getJSONArray("data");
            ArrayList<Painting> paintings = new ArrayList<>();

            int i = 0;
            while (i < itemsArray.length()) {
                // Get the current item information.
                JSONObject paintingJson = itemsArray.getJSONObject(i);

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    Painting painting = new Painting(
                            paintingJson.getString("name"),
                            paintingJson.getString("author"),
                            paintingJson.getString("author_nation"),
                            paintingJson.getString("description"),
                            paintingJson.getString("location"),
                            paintingJson.getString("locating_country"),
                            paintingJson.getString("image")
                    );

                    paintings.add(painting);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            return paintings;

        } catch (JSONException e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap getImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
