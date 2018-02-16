package com.example.android.bookslist;

import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phartmann on 16/02/2018.
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookList>> {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 12;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Call Connectivity Manager */
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        /** Get connection status */
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        /** Create new BookAdapter to this Activity */
        bookAdapter = new BookAdapter(this, new ArrayList<BookList>());

        /** Find view and set adapter */
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(bookAdapter);

        /** Interact with loader if isConnected */
        LoaderManager loaderManager = getLoaderManager();

        if (isConnected) {
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            /** If not connected, tell to the user */
            Toast.makeText(this, "Verifique sua conexão", Toast.LENGTH_SHORT ).show();
        }

        Log.i(LOG_TAG, "onCreate executed X5S" );
    }

    /** Call the Loader passing a List */
    @Override
    public Loader<List <BookList>> onCreateLoader( int id, Bundle args ) {
        Log.i(LOG_TAG, "onCreateLoader executed X5S" );
        return new BookLoader(this, QueryBooks.urlAPI);
    }

    /** Check if is valid and show on Activity */
    @Override
    public void onLoadFinished( Loader <List <BookList>> loader, List <BookList> bookdata ) {
        /** First clean up the adapter */
        bookAdapter.clear();

        /** Then check if there are a valid list */
        if (bookdata != null && !bookdata.isEmpty()){
            bookAdapter.addAll(bookdata);
            Log.i(LOG_TAG, "onLoadFinished executed X5S" );
        }
        Log.i(LOG_TAG, "onLoadFinished NOT executed X5S" );
    }

    /** Clean up when loader is reseted */
    @Override
    public void onLoaderReset( Loader <List <BookList>> loader ) {
        bookAdapter.clear();
        Log.i(LOG_TAG, "onLoaderReset executed X5S" );
    }
}
