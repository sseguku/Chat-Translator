package com.lecode.chatranslator;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatArrayAdapter extends ArrayAdapter<OneComment>{
	
	
	private TextView traslationName;
	private List<OneComment> translations = new ArrayList<OneComment>();
	private LinearLayout wrapper;

	@Override
	public void add(OneComment object) {
		translations.add(object);
		super.add(object);
	}


	public ChatArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		
		
	}

	
	public int getCount() {
		return this.translations.size();
	}

	public OneComment getItem(int index) {
		return this.translations.get(index);
	}

	
	

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.listitem, parent, false);
		}

		wrapper = (LinearLayout) row.findViewById(R.id.wrapper);
		OneComment coment = getItem(position);
		traslationName = (TextView) row.findViewById(R.id.comment);
		traslationName.setText(coment.comment);
		traslationName.setBackgroundResource(coment.left ? R.drawable.bubble_yellow : R.drawable.bubble_green);
		wrapper.setGravity(coment.left ? Gravity.LEFT : Gravity.RIGHT);

		return row;
	}

	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}
	
	
	
	
}
