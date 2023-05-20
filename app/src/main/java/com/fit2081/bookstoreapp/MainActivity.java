package com.fit2081.bookstoreapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fit2081.bookstoreapp.provider.Book;
import com.fit2081.bookstoreapp.provider.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private DrawerLayout drawerlayout;
    private BookViewModel mBookViewModel;
    //    private DatabaseReference mTable;
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        initViews(savedInstanceState);
        setupToolbarNavFab();
        setupGestureListener();
        loadBooksFromFirebase();
        registerSmsReceiver();
    }

    private void initViews(Bundle savedInstanceState) {
        etBookId = findViewById(R.id.book_id);
        etTitle = findViewById(R.id.title_id);
        etIsbn = findViewById(R.id.isbn_id);
        etAuthor = findViewById(R.id.author_id);
        etDescription = findViewById(R.id.description_id);
        etPrice = findViewById(R.id.price_id);
        // load attributes after configuration change
        if (savedInstanceState == null) loadBooks();
    }

    private void setupToolbarNavFab() {
        drawerlayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // tell the activity to use our toolbar as the action bar
        setSupportActionBar(toolbar);

        // set up the navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        // set up the floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> new IntentIntegrator(this).initiateScan());
    }

    private void setupGestureListener() {
        View gestureView = findViewById(R.id.gesture_view);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        gestureView.setOnTouchListener((view, event) -> mDetector.onTouchEvent(event));
    }

    private void loadBooksFromFirebase() {
        // load the fragment
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.book_frag, BookListFragment.class, null)
                .commit();

        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

//        // initialise Firebase database
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        mTable = mDatabase.child("books");
    }

    private void registerSmsReceiver() {
        // request permissions to access SMS
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        // create and instantiate the local broadcast receiver (listens to messages from SMSReceiver)
        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();
        // register the broadcast handler with the intent filter that is declared in SMSReceiver
        registerReceiver(myBroadCastReceiver, new android.content.IntentFilter(SMSReceiver.SMS_FILTER));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String isbn = result.getContents();
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(this);
                String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        response -> {
                            try {
                                // Parse the JSON response
                                JSONObject jsonResponse = new JSONObject(response);

                                // Check if items exist
                                if (!jsonResponse.has("items")) {
                                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "No book found with this ISBN", Toast.LENGTH_LONG).show());
                                    return;
                                }

                                // Get the book information from the JSON response
                                JSONObject volumeInfo = jsonResponse.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo");

                                // Extract the title, authors, and ISBN from the JSON
                                String title = volumeInfo.has("title") ? volumeInfo.getString("title") : "";

                                // authors is an array, so we need to convert it to a comma-separated string
                                StringBuilder authors = new StringBuilder();
                                if (volumeInfo.has("authors")) {
                                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                                    for (int i = 0; i < authorsArray.length(); i++) {
                                        authors.append(authorsArray.getString(i));
                                        if (i < authorsArray.length() - 1) {
                                            authors.append(", ");
                                        }
                                    }
                                }

                                runOnUiThread(() -> {
                                    etBookId.setText("");
                                    etTitle.setText(title);
                                    etIsbn.setText(isbn);
                                    etAuthor.setText(authors.toString());
                                    etDescription.setText("");
                                    etPrice.setText("");
                                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error parsing book data", Toast.LENGTH_LONG).show());
                            }
                        }, error -> {
                    error.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error retrieving book data", Toast.LENGTH_LONG).show());
                });
                queue.add(stringRequest);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void addBook() {
        // get attributes
        String bookId = etBookId.getText().toString();
        String title = etTitle.getText().toString();
        String isbn = etIsbn.getText().toString();
        String author = etAuthor.getText().toString();
        String description = etDescription.getText().toString();
        String priceStr = etPrice.getText().toString();

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
        editor.putString(KEY_BOOK_ID, bookId);
        editor.putString(KEY_TITLE, title);
        editor.putString(KEY_ISBN, isbn);
        editor.putString(KEY_AUTHOR, author);
        editor.putString(KEY_DESCRIPTION, description);
        editor.putString(KEY_PRICE, priceStr);
        editor.apply();

        // add book to recycler view
        Book book = new Book(bookId, title, isbn, author, description, priceStr);
        mBookViewModel.insert(book);
//        mTable.push().setValue(book);
        Log.d("BOOK_APP", "Added book: " + book);
    }

    public void clearFields() {
        // clear fields
        etBookId.setText("");
        etTitle.setText("");
        etIsbn.setText("");
        etAuthor.setText("");
        etDescription.setText("");
        etPrice.setText("");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear_fields_menu_id) {
            clearFields();
        } else if (id == R.id.load_data_menu_id) {
            loadBooks();
        }
        // tell the OS
        return true;
    }

    private class MyBroadCastReceiver extends BroadcastReceiver {
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
    private class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.add_book_menu_id) {
                addBook();
            } else if (id == R.id.remove_last_menu_id) {
                mBookViewModel.deleteLastBook();
            } else if (id == R.id.remove_all_menu_id) {
                mBookViewModel.deleteAll();
//                mTable.removeValue();
            } else if (id == R.id.list_all_menu_id) {
                Intent i = new Intent(MainActivity.this, BookListActivity.class);
                startActivity(i);
            }
            drawerlayout.closeDrawers();
            // tell the OS
            return true;
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            etIsbn.setText(RandomString.generateNewRandomString(5));
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            clearFields();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float deltaX = e2.getX() - e1.getX();
            float deltaY = e2.getY() - e1.getY();
            if (Math.abs(deltaX) > 150 && Math.abs(deltaY) < 150) {
                String priceStr = etPrice.getText().toString();
                float price = priceStr.isEmpty() ? 0 : Float.parseFloat(priceStr);
                etPrice.setText(String.valueOf(price + Math.round(distanceX)));
                return true;
            }
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (velocityX > 1000) {
                moveTaskToBack(true);
                return true;
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            loadBooks();
        }
    }
}