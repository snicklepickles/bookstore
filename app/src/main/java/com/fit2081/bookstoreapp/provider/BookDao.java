package com.fit2081.bookstoreapp.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

public interface BookDao {
    @Query("select * from books")
    LiveData<List<Book>> getAllBooks();

    @Query("select * from books where id=:id")
    Book getBook(int id);

    @Insert
    void addBook(Book book);

    @Query("delete from books where id= :id")
    void deleteBook(int id);

    @Query("delete FROM books")
    void deleteAllBooks();
}
