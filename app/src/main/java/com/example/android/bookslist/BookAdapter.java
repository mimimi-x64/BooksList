package com.example.android.bookslist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by phartmann on 16/02/2018.
 */

public class BookAdapter extends ArrayAdapter<BookList> {

    public BookAdapter( @NonNull Context context, @NonNull List<BookList> objects ) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent ) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        /** Catch an item of list */
        BookList currentItemOnPosition = getItem(position);

        /** Set author to textView */
        TextView authorView = listItemView.findViewById(R.id.author_view);
        authorView.setText(currentItemOnPosition.getmAutor());

        /** Set title to textView*/
        TextView titleView = listItemView.findViewById(R.id.title_view);
        titleView.setText(currentItemOnPosition.getmTitle());

        return listItemView;
    }
}
