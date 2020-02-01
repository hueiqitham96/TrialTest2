package com.example.trialtest.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.trialtest.DBHandler;

public class UrlContentProvider extends ContentProvider {

    private static final String WEBSITE_TABLE = "websiteName";

    public static final int WEBSITE = 1;
    public static final int WEBSITE_ID = 2;

    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    // uri matchers for actions (insert/query/update/delete)
    static {
        sURIMatcher.addURI(MyContractClass.AUTHORITY, WEBSITE_TABLE, WEBSITE);
        sURIMatcher.addURI(MyContractClass.AUTHORITY, WEBSITE_TABLE + "/#",
                WEBSITE_ID);
    }

    private DBHandler myDB;

    public UrlContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO: Implement this to handle requests to delete one or more rows.
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            // delete whole table
            case WEBSITE:
                rowsDeleted = sqlDB.delete(DBHandler.TABLE_WEBSITE,
                        selection,
                        selectionArgs);
                break;
                // delete according to id
            case WEBSITE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(DBHandler.TABLE_WEBSITE,
                            DBHandler.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(DBHandler.TABLE_WEBSITE,
                            DBHandler.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " +
                        uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            // insert values to table column respectively
            case WEBSITE:
                id = sqlDB.insert(DBHandler.TABLE_WEBSITE,
                        null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "
                        + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(WEBSITE_TABLE + "/" + id);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        myDB = new DBHandler(getContext(),null,null,1);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBHandler.TABLE_WEBSITE);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case WEBSITE_ID:
                queryBuilder.appendWhere(DBHandler.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            case WEBSITE:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor = queryBuilder.query(myDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case WEBSITE:
                rowsUpdated =
                        sqlDB.update(DBHandler.TABLE_WEBSITE,
                                values,
                                selection,
                                selectionArgs);
                break;
            case WEBSITE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated =
                            sqlDB.update(DBHandler.TABLE_WEBSITE,
                                    values,
                                    DBHandler.COLUMN_ID + "=" + id,
                                    null);
                } else {
                    rowsUpdated =
                            sqlDB.update(DBHandler.TABLE_WEBSITE,
                                    values,
                                    DBHandler.COLUMN_ID + "=" + id
                                            + " and "
                                            + selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "
                        + uri);
        }
        getContext().getContentResolver().notifyChange(uri,
                null);
        return rowsUpdated;
    }

}
