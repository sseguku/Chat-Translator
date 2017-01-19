package com.lecode.chattranslator;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;

import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import com.memetix.mst.detect.Detect;

 
public class MainActivity extends Activity {

    com.lecode.chattranslator.ChatArrayAdapter adapter;
	ListView lv;
	EditText edittextFromLang;
	String translatedText, langToBeTranslated, langSelected, originalText = "";
    Spinner froSpinnerLang, toSpinnerLang;	
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //declarations of spinners
        
        addItemsOnFroSpinner();
        addItemsOnToSpinner();
        
              
        //declaration of adapters
        adapter = new ChatArrayAdapter(getApplicationContext(), R.layout.listitem);
		
        //declarations of list
        lv = (ListView) findViewById(R.id.listView1);			
		lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		lv.setStackFromBottom(true);
        lv.setAdapter(adapter);
		
		
		
       lv.setOnItemLongClickListener(new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			return false;
		}
	});
		
		
        //declarations of edit text
		edittextFromLang = (EditText) findViewById(R.id.edittext_fro_lang);	
	    
		//event of edit text
		edittextFromLang.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					adapter.add(new OneComment(true, edittextFromLang.getText().toString()));
					addTranslation();
					return true;
				}
				return false;
			}
		});
		
		//enable copy on long press
		lv.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return true;
			}
		});
				

    }
    
   //translation process
    public void addTranslation(){
    	
    	try {
    		//selections from spinners


            
        	class bgStuff extends AsyncTask<Void, Void, Void>{
                
                @Override
                protected Void doInBackground(Void... params) {
                      
                	Translate.setClientId("qsaa"); //Change this
                    Translate.setClientSecret("xricxFYQUOTYVNzInBv6xfWbcje6qQ0CVeKZR+tzPmQ=");  //Change this
                    originalText=edittextFromLang.getText().toString();
                    langToBeTranslated=froSpinnerLang.getSelectedItem().toString().toUpperCase();
                    langSelected=toSpinnerLang.getSelectedItem().toString();
               

                	 try {//start catch
                	     Language detectedLanguage = Detect.execute(originalText);//detect lang from edittext

                		 if(langToBeTranslated=="AUTO_DETECT"){
                      	   translatedText = Translate.execute(originalText,  Language.valueOf(langSelected));
                             
                         }
                         else{
                        	 
                        	   //checks if the lang z equal to detected lang
                      	       if(froSpinnerLang.getSelectedItem().toString().toUpperCase().equalsIgnoreCase(detectedLanguage.getName(Language.ENGLISH))){
                            translatedText = Translate.execute(originalText, Language.valueOf(langToBeTranslated), Language.valueOf(langSelected));
	                          }  
                      	   else{
                      		   translatedText ="That seems to be " + detectedLanguage.getName(Language.ENGLISH).toUpperCase()+ ". Type only words in "+ langToBeTranslated.toUpperCase()+ "or Change to Auto Detect";
                      		   
                      	   }
                      	   
                         }
                		 
                		 //end try
                	 } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	//end catch
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                	adapter.add(new OneComment(false, translatedText));

					edittextFromLang.setText("");
                	super.onPostExecute(result);
                }
                 
            }
             
            new bgStuff().execute();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
    	

    }
    
//adding items to spinner one
    public void  addItemsOnFroSpinner(){
    	froSpinnerLang =(Spinner)findViewById(R.id.fro_spinner);
    	List <String> frolist = new ArrayList<String>();
    	
    	for(Language lang: Language.values()){
    		frolist.add(lang.name());
    	}
       // Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<String> sp1adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, frolist);
    	// Specify the layout to use when the list of choices appears
    	sp1adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	froSpinnerLang.setAdapter(sp1adapter);
    }
    
    //adding items to spinner 2
    public void  addItemsOnToSpinner(){
    	toSpinnerLang = (Spinner)findViewById(R.id.to_spinner);
        List <String> frolist = new ArrayList<String>();
    	
    	for(Language lang: Language.values()){
    		frolist.add(lang.name());
    	}
    	
    	frolist.remove(0);
       // Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<String> sp2adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, frolist);
    	// Specify the layout to use when the list of choices appears
    	sp2adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	toSpinnerLang.setAdapter(sp2adapter); 
    	toSpinnerLang.setSelection(12);
    	
    }
    

    
    
}
