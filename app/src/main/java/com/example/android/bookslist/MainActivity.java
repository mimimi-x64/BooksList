package com.example.android.bookslist;

import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phartmann on 16/02/2018.
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookList>> {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static String urlSearch = null;
    private static final int LOADER_ID = 12;
    private BookAdapter bookAdapter;
    private EditText searchText;
    private LoaderManager loaderManager;
    private ListView listView;
    private TextView noConnection;
    private ImageView searchIcon;
    private TextView searchTextHint;


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

        /** Find Views on XML */
        listView = (ListView) findViewById(R.id.list);
        noConnection = findViewById(R.id.no_connection);
        searchIcon = findViewById(R.id.search_icon);
        searchTextHint = findViewById(R.id.blank_url);

        /** Find view and set adapter */
        listView.setAdapter(bookAdapter);

        /** Set visibility for views */
        noConnection.setVisibility(View.GONE);

        /** Interact with loader if isConnected */
        loaderManager = getLoaderManager();

        if (isConnected) {
            loaderManager.initLoader(LOADER_ID, null, this);
            searchTextHint.setVisibility(View.VISIBLE);
            searchIcon.setVisibility(View.VISIBLE);
        } else {
            /** If not connected, tell to the user */
            Toast.makeText(this, "Verifique sua conexão", Toast.LENGTH_SHORT ).show();
            noConnection.setText("Veja se você está conectado a Internet");
            noConnection.setVisibility(View.VISIBLE);
            searchTextHint.setVisibility(View.GONE);
            searchIcon.setVisibility(View.GONE);
        }
        /** Find Search Text */
        searchText = findViewById(R.id.pesquisa_view);

        /** Find button and set action */
        ImageButton imageButton = findViewById(R.id.pesquisa_button);
        imageButton.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick( View v ) {
                onButtonClick();
            }
        });
        Log.i(LOG_TAG, "onCreate executed X5S" );
    }

    /** Hide after onButtonClick to be called */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /** When button is clicked */
    private void onButtonClick(){
        String stringSearch = String.valueOf(searchText.getText());
        if (stringSearch.isEmpty()){
            Toast.makeText(this, "Por favor, Informe algum livro", Toast.LENGTH_LONG).show();
            return;
        } else if (stringSearch.contains(" ")){
            String stringQuery = stringSearch.replace(" ", "+");
            String stringQueryFinal = "https://www.googleapis.com/books/v1/volumes?q=" + stringQuery + "&maxResults=20";
            urlSearch = stringQueryFinal;
        } else {
            String stringQueryFinal = "https://www.googleapis.com/books/v1/volumes?q=" + stringSearch + "&maxResults=20";
            urlSearch = stringQueryFinal;
        }
        loaderManager.restartLoader(LOADER_ID, null, this);
        hideSoftKeyboard();
        Log.v(LOG_TAG, "asd123 " + urlSearch);
    }

    /** Call the Loader passing a List */
    @Override
    public Loader<List <BookList>> onCreateLoader( int id, Bundle args ) {
        Log.v(LOG_TAG, "onCreateLoader executed X5S" );
        return new BookLoader(this, urlSearch);
    }

    /** Check if is valid and show on Activity */
    @Override
    public void onLoadFinished( Loader <List <BookList>> loader, List <BookList> bookdata ) {
        /** First clean up the adapter */
        bookAdapter.clear();

        /** Then check if there are a valid list */
        if (bookdata != null && !bookdata.isEmpty()){
            bookAdapter.addAll(bookdata);
            Log.v(LOG_TAG, "onLoadFinished executed X5S" + bookdata.toString() );
        }
        Log.v(LOG_TAG, "onLoadFinished NOT executed X5S" );
    }

    /** Clean up when loader is reseted */
    @Override
    public void onLoaderReset( Loader <List <BookList>> loader ) {
        bookAdapter.clear();
        Log.i(LOG_TAG, "onLoaderReset executed X5S" );
    }
}
