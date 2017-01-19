package com.lecode.chatranslator;

/*
 * Developed by Jingo Kisakye
 * Rights reserved by lecode
 * Dedicated to my Maama - Marion Sebunya Namuddu.
 * She has been great in my live
 * I love you Mom
 * */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatArrayAdapter extends ArrayAdapter<OneComment>{
	
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
	
	
	private TextView traslationName;
	private List<OneComment> translations = new ArrayList<OneComment>();
	private LinearLayout wrapper;
	public ChatArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		
		
	}

	
	//selection methods of single item
	public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

	
	
	@Override
	public void add(OneComment object) {
		translations.add(object);
		super.add(object);
	}

   

	public int getCount() {
		return this.translations.size();
	}

	public OneComment getItem(int index) {
		return this.translations.get(index);
	}

	public void removeSelection(int position) {
	      //  translations.remove(position);
	       
	        mSelection.remove(position);
	    
	    	notifyDataSetChanged();
	    }
	public void removeItem(int position) {
	      //  translations.remove(position);
	       
	        translations.remove(position);
	    	notifyDataSetChanged();
	    }
	

	@SuppressLint("ResourceAsColor") public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		 
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.listitem, parent, false);
			
		}
		
		row.setBackgroundColor(android.R.color.background_light); //default color
		if (mSelection.get(position) != null) {
			row.setBackgroundColor(Color.rgb(187, 247, 251));// this is a selected position so make it red
		   
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
