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
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.book_titles_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(bookAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BookViewModel mBookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        mBookViewModel.getAllBooks().observe(getViewLifecycleOwner(), books -> {
            // Update the cached copy of the books in the adapter
            bookAdapter.setData(books);
            bookAdapter.notifyDataSetChanged();
        });
    }
}