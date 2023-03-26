package com.fit2081.bookstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NonNls;

import java.util.Locale;
import java.util.StringTokenizer;

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
    private androidx.drawerlayout.widget.DrawerLayout drawerlayout;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        etBookId = findViewById(R.id.book_id);
        etTitle = findViewById(R.id.title_id);
        etIsbn = findViewById(R.id.isbn_id);
        etAuthor = findViewById(R.id.author_id);
        etDescription = findViewById(R.id.description_id);
        etPrice = findViewById(R.id.price_id);

        drawerlayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);

        // load attributes after configuration change
        if (savedInstanceState == null) {
            loadBooks();
        }

        // tell the activity to use our toolbar as the action bar
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();

        // request permissions to access SMS
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        // create and instantiate the local broadcast receiver (listens to messages from SMSReceiver)
        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        // register the broadcast handler with the intent filter that is declared in SMSReceiver
        registerReceiver(myBroadCastReceiver, new android.content.IntentFilter(SMSReceiver.SMS_FILTER));

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // save title and isbn
        outState.putString(KEY_TITLE, etTitle.getText().toString());
        outState.putString(KEY_ISBN, etIsbn.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // restore title and isbn
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
        // clear fields
        etBookId.setText("");
        etTitle.setText("");
        etIsbn.setText("");
        etAuthor.setText("");
        etDescription.setText("");
        etPrice.setText("");
    }

    public void onLoadBookBtnClick(View view) {
        loadBooks();
    }

    public void loadBooks() {
        // load book attributes
        SharedPreferences prefs = getSharedPreferences("book", MODE_PRIVATE);
        etBookId.setText(prefs.getString(KEY_BOOK_ID, ""));
        etTitle.setText(prefs.getString(KEY_TITLE, ""));
        etIsbn.setText(prefs.getString(KEY_ISBN, ""));
        etAuthor.setText(prefs.getString(KEY_AUTHOR, ""));
        etDescription.setText(prefs.getString(KEY_DESCRIPTION, ""));
        etPrice.setText(prefs.getString(KEY_PRICE, ""));
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        // executed when SMSReceiver sends a broadcast
        @Override
        public void onReceive(Context context, Intent intent) {
            // get message from the intent
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);

            // parse message and update the UI
            StringTokenizer sT = new StringTokenizer(msg, "|");
            etBookId.setText(sT.nextToken());
            etTitle.setText(sT.nextToken());
            etIsbn.setText(sT.nextToken());
            etAuthor.setText(sT.nextToken());
            etDescription.setText(sT.nextToken());
            etPrice.setText(sT.nextToken());
        }
    }

    // handle the menu item clicks
    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.add_item_menu_id) {
                // Do something
            } else if (id == R.id.clear_fields_menu_id) {
                // Do something
            }
            // close the drawer
            drawerlayout.closeDrawers();
            // tell the OS
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.option_item_id_1) {
            // Do something
        } else if (id == R.id.option_item_id_1) {
            // Do something
        }
        // tell the OS
        return true;
    }
}