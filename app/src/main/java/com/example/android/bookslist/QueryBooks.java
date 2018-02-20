package com.example.android.bookslist;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by phartmann on 15/02/2018.
 */

public class QueryBooks {

    public static final String LOG_TAG = QueryBooks.class.getSimpleName();

    public static String author;
    public static String title;

    public static List<BookList> fetchBooksData(String requestUrl){

        URL url = createUrl(requestUrl);
        String jsonResponse = "";

        Log.i(LOG_TAG, "fetchBooksData executed X5S" );


        /** Try to make a Http Resquest*/
        try {
            jsonResponse = makeHttpRequest(url);
            Log.i(LOG_TAG, "fetchBook TRY executed X5S" );
        } catch (IOException e){
            Log.e(LOG_TAG, "Error on httpRequest");
        }
        Log.i(LOG_TAG, "fetchBook Before RETURN executed X5S" );
        return QueryBooks.extractFromResponse(jsonResponse);
    }

    private static URL createUrl(String stringUrl){
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception){
            Log.e(LOG_TAG, "Error when creating URL", exception);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest( URL url ) throws IOException {

        String jsonResponse = "";
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;

        /** Returns json early if it's empty */
        if (url == null){
            return jsonResponse;
        }

        try {
            /** Open Connection */
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(12000);
            urlConnection.connect();

            /** Handle only 200 http code */
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                /** Otherwise show a Error */
                // Couldn't be a throw here?
                Log.e(LOG_TAG, "Error on Response Code - CODE " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem with this Connection.");
        } finally {
            /** Release resources */
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream !=  null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            /** Do a loop util finish of read */
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<BookList> extractFromResponse( String jsonResponse ) {



        /** If response is null, do nothing */
        if (TextUtils.isEmpty(jsonResponse)){
            Log.e(LOG_TAG, "jsonResponse is NULL X5S");
            return null;
        }

        /** Create an empty ArrayList */
        ArrayList<BookList> books = new ArrayList <>();

        /** Parse JSON */
        try {
            /** Create JSON Object and handle it */
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray itemsArray = root.getJSONArray("items");

            /** Loop to catch all books */
            for (int i=0; i < itemsArray.length(); i++ ){
                JSONObject index = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = index.getJSONObject("volumeInfo");
                /** Catch title */
                title = volumeInfo.getString("title");

                if (volumeInfo.has("authors")){
                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                    /** Check if has more than 1 author and handle it */
                    if (authorsArray.length() != 1){
                        author = "Vários Autores";
                    } else {
                        author = authorsArray.getString(0);
                    }
                } else {
                    author = "Autor não informado";
                }
                /** Start to fill */
                books.add(new BookList(author, title));
            }
        } catch (JSONException e) {
            e.printStackTrace( );
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);

        }
        Log.v(LOG_TAG, "extractFromResponse executed + json: " + jsonResponse);

        /** Return the list */
        return books;
    }
}