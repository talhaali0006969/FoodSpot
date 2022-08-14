package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHelperExtra extends SQLiteOpenHelper {

    private static final String TABLE_CART = "tbl_cart";
    private static final String KEY_CART_ID = "Id";
    private static final String KEY_CART_PRODUCT_ID = "product_id";
    private static final String KEY_CART_PRODUCT_NAME = "product_name";
    private static final String KEY_CART_PRODUCT_PRICE = "product_price";
    private static final String KEY_CART_PRODUCT_IMAGE = "product_image";
    private static final String KEY_CART_QUANTITY = "quantity";


    private static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + "(" +
            KEY_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_CART_PRODUCT_ID + " TEXT," +
            KEY_CART_PRODUCT_NAME + " TEXT," +
            KEY_CART_PRODUCT_PRICE + " INTEGER," +
            KEY_CART_PRODUCT_IMAGE + " TEXT," +
            KEY_CART_QUANTITY + " INTEGER" +
            ")";

    public DbHelperExtra(@Nullable Context context) {
        super(context, "cart.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);

    }

    public long addTOCar(deals deals, int quantity) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_CART_PRODUCT_ID, deals.getProductId());
        cv.put(KEY_CART_PRODUCT_NAME, deals.getProductName());
        cv.put(KEY_CART_PRODUCT_PRICE, deals.getProductPrice());
        cv.put(KEY_CART_PRODUCT_IMAGE, deals.getProductImage());
        cv.put(KEY_CART_QUANTITY, quantity);
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_CART,  null, cv);

    }

    public int deleteFromCart(int cartId){

        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_CART, KEY_CART_ID + "=" + cartId, null);

    }

    public int getCartCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ KEY_CART_ID + " FROM "+TABLE_CART,null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }

    public boolean isProductIn(deals deals) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ KEY_CART_ID +" FROM " + TABLE_CART + " WHERE " + KEY_CART_PRODUCT_ID + "=?", new String[]{deals.getProductId()} );
        int count = cursor.getCount();
        cursor.close();
        if (count > 0 ){
            return true;
        } else {
            return false;
        }
    }

    public void updateProductIn( deals deals, int quantity) {

        String query = "UPDATE "+ TABLE_CART + " SET " + KEY_CART_QUANTITY + "=" + KEY_CART_QUANTITY + " + " + quantity + " WHERE " + KEY_CART_PRODUCT_ID + "=" + "=" +deals.getProductId();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }
    public  int setProductQuantity (int cartId , int quantity){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_CART_QUANTITY, quantity);
        return db.update(TABLE_CART , cv, KEY_CART_ID + "=" + cartId, null );
    }

    @SuppressLint("Range")
    public ArrayList<com.example.myapplication.CartItem> getAllCartItems(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_CART, null);
        ArrayList<com.example.myapplication.CartItem> cartItemList = new ArrayList<>();

        while (cursor.moveToNext()){
            @SuppressLint("Range") int cartId = cursor.getInt(cursor.getColumnIndex(KEY_CART_ID));

           deals product = new deals();

            product.setProductId(cursor.getString(cursor.getColumnIndex(KEY_CART_PRODUCT_ID)));
            product.setProductName(cursor.getString(cursor.getColumnIndex(KEY_CART_PRODUCT_NAME)));
            product.setProductPrice(cursor.getInt(cursor.getColumnIndex(KEY_CART_PRODUCT_PRICE)));
            product.setProductImage(cursor.getString(cursor.getColumnIndex(KEY_CART_PRODUCT_IMAGE)));

            int quantity = cursor.getInt(cursor.getColumnIndex(KEY_CART_QUANTITY));
            com.example.myapplication.CartItem cartItem = new com.example.myapplication.CartItem(cartId ,product , quantity);
            cartItemList.add(cartItem);

        }
        cursor.close();
        return cartItemList;
    }

}
