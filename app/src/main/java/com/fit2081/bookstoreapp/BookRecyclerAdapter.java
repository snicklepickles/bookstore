package com.fit2081.bookstoreapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder>{
    ArrayList<Book> data = new ArrayList<>();

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
        holder.bookIdTv.setText(data.get(position).getBookId());
        holder.authorTv.setText(data.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bookIdTv;
        public TextView authorTv;

        public ViewHolder(@NonNull View bookView) {
            super(bookView);
            bookIdTv = bookView.findViewById(R.id.book_id);
            authorTv = bookView.findViewById(R.id.author_id);
        }
    }
}
