package com.lecode.chatranslator;

/*
 * Developed by Jingo Kisakye
 * Rights reserved by lecode
 * Dedicated to my Maama - Marion Sebunya Namuddu.
 * She has been great in my live
 * I love you Mom
 * */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "ChatTagsManager";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_CHATTAGS = "chatTags";
	private static final String KEY_ID = "id";
	private static final String KEY_LEFT_USER = "left_user";
	private static final String KEY_RIGHT_MACHINE = "right_machine";

	public DatabaseHandler(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String CREATE_CHATTAGS_TABLE = "CREATE TABLE " + TABLE_CHATTAGS + " (" +
		KEY_ID + " INTEGER PRIMARY KEY," + KEY_LEFT_USER + " TEXT,"+
				KEY_RIGHT_MACHINE + " TEXT"+ ")";
	
	   db.execSQL(CREATE_CHATTAGS_TABLE);
	}
    //upgrade database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATTAGS);
		onCreate(db);
		
	}
	
	//crud operations
	//adding new chattag
	public void addChatTags(ChatTags chatTags){
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_LEFT_USER, chatTags.get_left_user());
		values.put(KEY_RIGHT_MACHINE, chatTags.get_right_machine());
		
		//inserting row
		db.insert(TABLE_CHATTAGS, null, values);
		db.close();
	}
	//getting single chattags
	public ChatTags getChatTags(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_CHATTAGS, new String[]{KEY_ID,KEY_LEFT_USER,KEY_RIGHT_MACHINE}, KEY_ID + "=?", new String[]{String.valueOf(id)},null,null,null,null);
		
		if(cursor!=null)
			cursor.moveToFirst();
		ChatTags chatTags = new ChatTags(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
		
		return chatTags;
		
	}
	//Getting All Chattags
	public List<ChatTags> getAllChatTags(){
		
		List< ChatTags> chatTagsList = new ArrayList<ChatTags>();
		String selectQuery = "SELECT * FROM " + TABLE_CHATTAGS;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor =
				db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do {
				ChatTags chatTags = new ChatTags();
				chatTags.set_id(Integer.parseInt(cursor.getString(0)));
				chatTags.set_left_user(cursor.getString(1));
				chatTags.set_right_machine(cursor.getString(2));
				chatTagsList.add(chatTags);
			} while (cursor.moveToNext());
		}
		return chatTagsList;
	}
	
	public int getChatTagsCount(){
		
	String countQuery = "SELECT * FROM "+ TABLE_CHATTAGS;
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = db.rawQuery(countQuery, null);
	cursor.close();
	
	return cursor.getCount();
		
	}
	
	public int updateContact(ChatTags chatTags){
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values  =new ContentValues();
		values.put(KEY_LEFT_USER, chatTags.get_left_user());
		values.put(KEY_RIGHT_MACHINE, chatTags.get_right_machine());
		
		return db.update(TABLE_CHATTAGS, values, KEY_ID+" = ?", new String[]{String.valueOf(chatTags.get_id())});
		
	}
	//delet
	public void deleteChatTags(ChatTags chatTags){
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CHATTAGS , KEY_ID +" = ?", new String[]{String.valueOf(chatTags.get_id())});
		db.close();
	}
	
}
