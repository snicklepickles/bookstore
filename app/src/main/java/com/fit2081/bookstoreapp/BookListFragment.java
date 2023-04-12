package com.fit2081.bookstoreapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.bookstoreapp.provider.BookViewModel;

public class BookListFragment extends Fragment {
    private final BookAdapter bookAdapter = new BookAdapter();

    public BookListFragment() {
        super(R.layout.fragment_book_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.book_titles_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(bookAdapter);
        BookViewModel mBookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        // Add an observer on the LiveData returned by getAllBooks
        mBookViewModel.getAllBooks().observe(getViewLifecycleOwner(), books -> {
            // Update the cached copy of the books in the adapter
            bookAdapter.setData(books);
            bookAdapter.notifyDataSetChanged();
        });
    }
}