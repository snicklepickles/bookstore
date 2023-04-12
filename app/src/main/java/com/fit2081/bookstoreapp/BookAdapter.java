package com.fit2081.bookstoreapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.bookstoreapp.provider.Book;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{
    private ArrayList<Book> data = new ArrayList<>();

    public void setData(ArrayList<Book> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CardView inflated as RecyclerView list item
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bookIdTv.setText(String.format("ID: %s", data.get(position).getBookId()));
        holder.titleTv.setText(String.format("Title: %s", data.get(position).getTitle()));
        holder.isbnTv.setText(String.format("ISBN: %s", data.get(position).getIsbn()));
        holder.authorTv.setText(String.format("Author: %s", data.get(position).getAuthor()));
        holder.descriptionTv.setText(String.format("Desc: %s", data.get(position).getDescription()));
        holder.priceTv.setText(String.format("Price: %s", data.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bookIdTv;
        private final TextView titleTv;
        private final TextView isbnTv;
        private final TextView authorTv;
        private final TextView descriptionTv;
        private final TextView priceTv;

        public ViewHolder(@NonNull View bookView) {
            super(bookView);
            bookIdTv = bookView.findViewById(R.id.book_id);
            titleTv = bookView.findViewById(R.id.title_id);
            isbnTv = bookView.findViewById(R.id.isbn_id);
            authorTv = bookView.findViewById(R.id.author_id);
            descriptionTv = bookView.findViewById(R.id.description_id);
            priceTv = bookView.findViewById(R.id.price_id);
        }
    }
}
