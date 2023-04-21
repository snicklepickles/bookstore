package com.fit2081.bookstoreapp.provider;

import static com.fit2081.bookstoreapp.provider.Book.TABLE_NAME;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = TABLE_NAME)
public class Book {
    public static final String TABLE_NAME = "books";
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "bookId")
    private final String bookId;
    @ColumnInfo(name = "bookTitle")
    private final String title;
    @ColumnInfo(name = "bookIsbn")
    private final String isbn;
    @ColumnInfo(name = "bookAuthor")
    private final String author;
    @ColumnInfo(name = "bookDescription")
    private final String description;
    @ColumnInfo(name = "bookPrice")
    private final String price;

    public Book(String bookId, String title, String isbn, String author, String description, String price) {
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }


    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

}
