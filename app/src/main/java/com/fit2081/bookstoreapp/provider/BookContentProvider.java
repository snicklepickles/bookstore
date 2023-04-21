package com.fit2081.bookstoreapp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BookContentProvider extends ContentProvider {

    public static final String CONTENT_AUTHORITY = "fit2081.tasks.db.provider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    private static final int MULTIPLE_ROWS_TASKS = 1;
    private static final int SINGLE_ROW_TASKS = 2;
    private static final int MULTIPLE_ROWS_USERS = 3;
    private static final int SINGLE_ROW_USERS = 4;

    BookDatabase db;
    private static final UriMatcher sUriMatcher = createUriMatcher();



    //
    private static UriMatcher createUriMatcher() {

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        //sUriMatcher will return code 1 if uri like authority/tasks
        uriMatcher.addURI(authority, Book.TABLE_NAME, MULTIPLE_ROWS_TASKS);

        //sUriMatcher will return code 2 if uri like e.g. authority/tasks/7 (where 7 is id of row in tasks table)
        uriMatcher.addURI(authority, Book.TABLE_NAME + "/#", SINGLE_ROW_TASKS);

        //sUriMatcher will return code 1 if uri like authority/users
        uriMatcher.addURI(authority, "users", MULTIPLE_ROWS_USERS);

        //sUriMatcher will return code 2 if uri like e.g. authority/users/7 (where 7 is id of row in users table)
        uriMatcher.addURI(authority, "users" + "/#", SINGLE_ROW_USERS);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        db = BookDatabase.getDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d("week8tasks","query");
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Book.TABLE_NAME);
        String query = builder.buildQuery(projection, selection, null, null, sortOrder, null);
        Log.d("week8tasks",query);
        final Cursor cursor = db
                .getOpenHelper()
                .getReadableDatabase()
                .query(query);
        Log.d("week8tasks", String.valueOf(cursor));
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        assert contentValues != null;
        long rowId = db
                .getOpenHelper()
                .getWritableDatabase()
                .insert(Book.TABLE_NAME, 0, contentValues);

        return ContentUris.withAppendedId(CONTENT_URI, rowId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletionCount;

        deletionCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .delete(Book.TABLE_NAME, selection, selectionArgs);

        return deletionCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updateCount;
        updateCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .update(Book.TABLE_NAME, 0, values, selection, selectionArgs);

        return updateCount;
    }
}
