package com.example.android.bookslist;

/**
 * Created by phartmann on 15/02/2018.
 */

public class BookList {

    /** Values */
    private String mAutor;
    private String mTitle;

    /** Constructor */
    public BookList( String mAutor, String mTitle ) {
        this.mAutor = mAutor;
        this.mTitle = mTitle;
    }

    /** Getters */
    public String getmAutor() {
        return mAutor;
    }
    public String getmTitle() {
        return mTitle;
    }

}
