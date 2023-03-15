package com.fit2081.bookstoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            onLoadBookBtnClick(null);
        }
    }

    public void onAddBookBtnClick(View view) {
        // get ref to EditTexts
        EditText bookIdEt = findViewById(R.id.book_id);
        EditText titleEt = findViewById(R.id.title_id);
        EditText isbnEt = findViewById(R.id.isbn_id);
        EditText authorEt = findViewById(R.id.author_id);
        EditText descriptionEt = findViewById(R.id.description_id);
        EditText priceEt = findViewById(R.id.price_id);

        // get attributes
        String bookId = bookIdEt.getText().toString();
        String title = titleEt.getText().toString();
        String isbn = isbnEt.getText().toString();
        String author = authorEt.getText().toString();
        String description = descriptionEt.getText().toString();

        // get isbn and price (default to 0 if empty)
        float price = 0;
        if (!priceEt.getText().toString().isEmpty()) {
            price = Float.parseFloat(priceEt.getText().toString());
        }

        // display toast
        String msg = String.format(Locale.ENGLISH, "Book (%s) and the price (%.2f)", title, price);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        // save attributes
        SharedPreferences prefs = getSharedPreferences("book", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("book_id", bookId);
        editor.putString("title", title);
        editor.putString("isbn", isbn);
        editor.putString("author", author);
        editor.putString("description", description);
        editor.putFloat("price", price);
        editor.apply();
    }

    public void onClearFieldsBtnClick(View view) {
        ((EditText) findViewById(R.id.book_id)).setText("");
        ((EditText) findViewById(R.id.title_id)).setText("");
        ((EditText) findViewById(R.id.isbn_id)).setText("");
        ((EditText) findViewById(R.id.author_id)).setText("");
        ((EditText) findViewById(R.id.description_id)).setText("");
        ((EditText) findViewById(R.id.price_id)).setText("");
    }

    public void onLoadBookBtnClick(View view) {
        SharedPreferences prefs = getSharedPreferences("book", MODE_PRIVATE);
        ((EditText) findViewById(R.id.book_id)).setText(prefs.getString("book_id", ""));
        ((EditText) findViewById(R.id.title_id)).setText(prefs.getString("title", ""));
        ((EditText) findViewById(R.id.isbn_id)).setText(prefs.getString("isbn", ""));
        ((EditText) findViewById(R.id.author_id)).setText(prefs.getString("author", ""));
        ((EditText) findViewById(R.id.description_id)).setText(prefs.getString("description", ""));

        float price = prefs.getFloat("price", 0);
        if (price == 0) {
            ((EditText) findViewById(R.id.price_id)).setText("");
        } else if (price == Math.round(price)) {
            ((EditText) findViewById(R.id.price_id)).setText(String.valueOf((int) price));
        } else {
            ((EditText) findViewById(R.id.price_id)).setText(String.valueOf(price));
        }
    }
}