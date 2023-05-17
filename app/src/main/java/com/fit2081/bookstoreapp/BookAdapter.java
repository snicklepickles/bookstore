package com.fit2081.bookstoreapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.bookstoreapp.provider.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<Book> data;

    public void setData(List<Book> data) {
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
        holder.idTv.setText(String.format("ID: %s", data.get(position).getId()));
        holder.bookIdTv.setText(String.format("Book ID: %s", data.get(position).getBookId()));
        holder.titleTv.setText(String.format("Title: %s", data.get(position).getTitle()));
        holder.isbnTv.setText(String.format("ISBN: %s", data.get(position).getIsbn()));
        holder.authorTv.setText(String.format("Author: %s", data.get(position).getAuthor()));
        holder.descriptionTv.setText(String.format("Desc: %s", data.get(position).getDescription()));
        holder.priceTv.setText(String.format("Price: %s", data.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        else
            return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView idTv;
        private final TextView bookIdTv;
        private final TextView titleTv;
        private final TextView isbnTv;
        private final TextView authorTv;
        private final TextView descriptionTv;
        private final TextView priceTv;

        public ViewHolder(@NonNull View bookView) {
            super(bookView);
            idTv = bookView.findViewById(R.id.id);
            bookIdTv = bookView.findViewById(R.id.book_id);
            titleTv = bookView.findViewById(R.id.title_id);
            isbnTv = bookView.findViewById(R.id.isbn_id);
            authorTv = bookView.findViewById(R.id.author_id);
            descriptionTv = bookView.findViewById(R.id.description_id);
            priceTv = bookView.findViewById(R.id.price_id);

        }
    }
}
