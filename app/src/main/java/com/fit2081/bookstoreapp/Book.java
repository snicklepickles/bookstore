package com.fit2081.bookstoreapp;

public class Book {
    private final String bookId;
    private final String title;
    private final String isbn;
    private final String author;
    private final String description;
    private final String price;

    public Book(String bookId, String title, String isbn, String author, String description, String price) {
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.description = description;
        this.price = price;
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
}
