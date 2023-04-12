package com.fit2081.bookstoreapp.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {
    private final BookDao mBookDao;
    private final LiveData<List<Book>> mAllBooks;

    BookRepository(Application application) {
        BookDatabase db = BookDatabase.getDatabase(application);
        mBookDao = db.bookDao();
        mAllBooks = mBookDao.getAllBooks();
    }

    LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }

    void insert(Book book) {
        BookDatabase.databaseWriteExecutor.execute(() -> mBookDao.addBook(book));
    }

    void deleteAll(){
        BookDatabase.databaseWriteExecutor.execute(mBookDao::deleteAllBooks);
    }
}
