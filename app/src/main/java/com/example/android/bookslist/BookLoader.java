package com.example.android.bookslist;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by phartmann on 16/02/2018.
 */

public class BookLoader extends AsyncTaskLoader<List<BookList>> {

    public static final String LOG_TAG = BookLoader.class.getSimpleName();

    /** URL da busca */
    private String mUrl;

    /** Build a constructor, passing contexta and url */
    public BookLoader( Context context, String url ) {
        super(context);
        mUrl = url;
    }

    /** Thread in background */
    @Override
    public List <BookList> loadInBackground() {
        /** Check if url is valid */
        if (mUrl == null){
            Log.i(LOG_TAG, "loadInBack NOT executed X5S" );
            return null;
        }
        /** Do http resquest, decode, extract and fill the list */
        Log.i(LOG_TAG, "loadInBack executed X5S" );
        List<BookList> bookLists = QueryBooks.fetchBooksData(mUrl);
        return bookLists;

    }

    @Override
    protected void onForceLoad() {
        Log.i(LOG_TAG, "onForceLoad executed X5S" );
        onForceLoad( );
    }
}
