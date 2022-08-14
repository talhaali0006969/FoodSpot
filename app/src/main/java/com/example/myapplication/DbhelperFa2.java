package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

class DbHelperFa2 extends SQLiteOpenHelper {
    private static final String TABLE_Favorite = "tbl_favorite";
    private static final String KEY_CART_ID = "Id";
    private static final String KEY_CART_PRODUCT_ID = "product_id";
    private static final String KEY_CART_PRODUCT_NAME = "product_name";
    private static final String KEY_CART_PRODUCT_PRICE = "product_price";
    private static final String KEY_CART_PRODUCT_IMAGE = "product_image";


    private static final String CREATE_TABLE_Favorite = "CREATE TABLE " + TABLE_Favorite + "(" +
            KEY_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_CART_PRODUCT_ID + " TEXT," +
            KEY_CART_PRODUCT_NAME + " TEXT," +
            KEY_CART_PRODUCT_PRICE + " INTEGER," +
            KEY_CART_PRODUCT_IMAGE + " TEXT" +

            ")";

    public DbHelperFa2(@Nullable Context context) {
        super(context, "favorite.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_Favorite);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Favorite);
        onCreate(db);

    }

    public long addTOFav(deals product) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_CART_PRODUCT_ID, product.getProductId());
        cv.put(KEY_CART_PRODUCT_NAME, product.getProductName());
        cv.put(KEY_CART_PRODUCT_PRICE, product.getProductPrice());
        cv.put(KEY_CART_PRODUCT_IMAGE, product.getProductImage());

        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_Favorite, null, cv);

    }

    public int deleteFromCart(deals deals) {

        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_Favorite, KEY_CART_PRODUCT_ID + "=" + deals.getProductId(), null);

    }


    public boolean isWallpaperFavorite(deals deals) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_Favorite + " where " + KEY_CART_PRODUCT_ID + "=" + deals.getProductId(), null);
        int count = cursor.getCount();
        cursor.close();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }


    @SuppressLint("Range")
    public ArrayList<deals> getAllCartItems() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_Favorite, null);
        ArrayList<deals> cartItemList = new ArrayList<>();

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int cartId = cursor.getInt(cursor.getColumnIndex(KEY_CART_ID));

            deals product = new deals();

            product.setProductId(cursor.getString(cursor.getColumnIndex(KEY_CART_PRODUCT_ID)));
            product.setProductName(cursor.getString(cursor.getColumnIndex(KEY_CART_PRODUCT_NAME)));
            product.setProductPrice(cursor.getInt(cursor.getColumnIndex(KEY_CART_PRODUCT_PRICE)));
            product.setProductImage(cursor.getString(cursor.getColumnIndex(KEY_CART_PRODUCT_IMAGE)));

            cartItemList.add(product);

        }
        cursor.close();
        return cartItemList;
    }

}