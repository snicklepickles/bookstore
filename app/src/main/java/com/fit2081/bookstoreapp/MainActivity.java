package com.fit2081.bookstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NonNls;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_BOOK_ID = "book_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ISBN = "isbn";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PRICE = "price";
    private EditText etBookId;
    private EditText etTitle;
    private EditText etIsbn;
    private EditText etAuthor;
    private EditText etDescription;
    private EditText etPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etBookId = findViewById(R.id.book_id);
        etTitle = findViewById(R.id.title_id);
        etIsbn = findViewById(R.id.isbn_id);
        etAuthor = findViewById(R.id.author_id);
        etDescription = findViewById(R.id.description_id);
        etPrice = findViewById(R.id.price_id);

        if (savedInstanceState == null) {
            SharedPreferences prefs = getSharedPreferences("book", MODE_PRIVATE);
            etBookId.setText(prefs.getString(KEY_BOOK_ID, ""));
            etTitle.setText(prefs.getString(KEY_TITLE, ""));
            etIsbn.setText(prefs.getString(KEY_ISBN, ""));
            etAuthor.setText(prefs.getString(KEY_AUTHOR, ""));
            etDescription.setText(prefs.getString(KEY_DESCRIPTION, ""));
            etPrice.setText(prefs.getString(KEY_PRICE, ""));
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(KEY_TITLE, etTitle.getText().toString());
        outState.putString(KEY_ISBN, etIsbn.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        etTitle.setText(savedInstanceState.getString(KEY_TITLE));
        etIsbn.setText(savedInstanceState.getString(KEY_ISBN));
    }

    public void onAddBookBtnClick(View view) {
        // get title and price
        String title = etTitle.getText().toString();
        float price = 0;
        if (!etPrice.getText().toString().isEmpty()) {
            price = Float.parseFloat(etPrice.getText().toString());
        }

        // display toast
        String msg = String.format(Locale.ENGLISH, "Book (%s) and the price (%.2f)", title, price);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        // save attributes
        SharedPreferences prefs = getSharedPreferences("book", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_BOOK_ID, etBookId.getText().toString());
        editor.putString(KEY_TITLE, title);
        editor.putString(KEY_ISBN, etIsbn.getText().toString());
        editor.putString(KEY_AUTHOR, etAuthor.getText().toString());
        editor.putString(KEY_DESCRIPTION, etDescription.getText().toString());
        editor.putString(KEY_PRICE, etPrice.getText().toString());
        editor.apply();
    }

    public void onClearFieldsBtnClick(View view) {
        etBookId.setText("");
        etTitle.setText("");
        etIsbn.setText("");
        etAuthor.setText("");
        etDescription.setText("");
        etPrice.setText("");
    }

    public void onLoadBookBtnClick(View view) {
        SharedPreferences prefs = getSharedPreferences("book", MODE_PRIVATE);
        etBookId.setText(prefs.getString(KEY_BOOK_ID, ""));
        etTitle.setText(prefs.getString(KEY_TITLE, ""));
        etIsbn.setText(prefs.getString(KEY_ISBN, ""));
        etAuthor.setText(prefs.getString(KEY_AUTHOR, ""));
        etDescription.setText(prefs.getString(KEY_DESCRIPTION, ""));
        etPrice.setText(prefs.getString(KEY_PRICE, ""));
    }
}