package com.lecode.chatranslator;



import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import com.memetix.mst.detect.*;



import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

@SuppressLint("DefaultLocale")
public class MainActivity extends Activity {
	
	/** The view to show the ad. */
	  private AdView adView;

	  /* Your ad unit id. Replace with your actual ad unit id. */
	  private static final String AD_UNIT_ID = "ca-app-pub-1912550983684124/2780791895";


    com.lecode.chatranslator.ChatArrayAdapter adapter;
    final Context context = this;
    ListView lv,  speechInText;
	Dialog matchTextDialog;
	ArrayList<String> matchesText;
	
	EditText edittextFromLang;
	String TranslatedText, langToBeTranslated, langSelected, originalText= null ;
    Spinner froSpinnerLang, toSpinnerLang;	
    private static final int REQUEST_CODE = 1001;
    ImageView mic;
    
    //db operations
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
       
        
        
        
        //declarations 
        //microphone
        mic =(ImageView)findViewById(R.id.mic);
       
        //spinners
        addItemsOnFroSpinner();
        addItemsOnToSpinner();
        //edit texts
        edittextFromLang = (EditText) findViewById(R.id.edittext_fro_lang);	
        //adapters
        adapter = new ChatArrayAdapter(getApplicationContext(), R.layout.listitem);
		
        //lists
        lv = (ListView) findViewById(R.id.listView1);			
		lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		lv.setStackFromBottom(true);		
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setAdapter(adapter);
        
        //Listeners
        //mic 
        mic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				if(isConnected())
					checkVoiceRecognition();
				else
				//not connected to the internet
                  noInternetConnection();
			}
		});
    	//edittext on enter
		edittextFromLang.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
		// If the event is a key-down event on the "enter" button
			if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
		// Perform action on key press
		//check for internet connection
		   		if(isConnected()){	
		   		 if (edittextFromLang!=null &&edittextFromLang.length()>0) 
		   		 { addTranslation();
		   		 }}
		   		
		   		else{
		   		noInternetConnection();
		   		}
		   		    
		   		 return true;
							}
				return false;
					}
				});
		
		
		 // Create an ad.
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_UNIT_ID);

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        LinearLayout layout = (LinearLayout) findViewById(R.id.ads_layout);
        layout.addView(adView);
        
     // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .addTestDevice("TA8830MZ82")
            .build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);

		
		
    }
    

	//translation process
    public void addTranslation(){   	
    	//add to left chat
    	adapter.add(new OneComment(true, edittextFromLang.getText().toString()));
		
    	class bgStuff extends AsyncTask<Void, Void, Void>{                   
            
            @Override
            protected Void doInBackground(Void... params) {
            	
            	Translate.setClientId("etranslator123");
            	Translate.setClientSecret("FP556NJH3bcPYNMff2OEIWhxKMqd4TZO5PyDsC5fLBM=");
            	originalText=edittextFromLang.getText().toString();
                langToBeTranslated=froSpinnerLang.getSelectedItem().toString().toUpperCase();
                langSelected=toSpinnerLang.getSelectedItem().toString();
            
            	
               try {
            	   Language detectedLanguage = Detect.execute(originalText);//detect lang from 
                	 
            	   if(langToBeTranslated=="AUTO_DETECT"){
            		   TranslatedText = Translate.execute(originalText,  Language.valueOf(langSelected));
                       
            	   }
            	   else{
                  	 
                	   //checks if the lang z equal to detected lang
              	    if(froSpinnerLang.getSelectedItem().toString().toUpperCase().equalsIgnoreCase(detectedLanguage.getName(Language.ENGLISH))){
                    TranslatedText = Translate.execute(originalText, Language.valueOf(langToBeTranslated), Language.valueOf(langSelected));
                      }  
              	   else{
              		   TranslatedText ="That seems to be " + detectedLanguage.getName(Language.ENGLISH).toUpperCase()+ ". Type only words in "+ langToBeTranslated.toUpperCase()+ "or Change to Auto Detect";
              		   
              	   }
              	   
                 }
        		
            	   
            	   TranslatedText = Translate.execute(originalText, Language.GERMAN);
					
				} catch (Exception e) {
					TranslatedText = "Try again...... Server Error";
				    e.printStackTrace();
				}//end try catch block
				return null;
                
            }//end doinbackground method

            @Override
            protected void onPostExecute(Void result) {
            	adapter.add(new OneComment(false, TranslatedText));
        		edittextFromLang.setText("");
            }
    	}
    	//end of asynctask class
    	 new bgStuff().execute();                          
         

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
    
    
//voice recognition check presence]]
    public void checkVoiceRecognition(){
    	
    	PackageManager pm = getPackageManager();
    	List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
         
    	if(activities.size()==0){
     	//Toast.makeText(this, "Voice recognizer not present", Toast.LENGTH_SHORT).show();
       //dialog for voice recognizer
    
    		noVoiceRecognizerDialog();
    	
    	}
    	else{
    		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    		startActivityForResult(intent, REQUEST_CODE);
    		
    	}
    	
    }
    //end of voice recognition
    
    //check internet boolean    
    public boolean isConnected(){
    	ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo net = cm.getActiveNetworkInfo();
    	if(net!=null && net.isAvailable() && net.isConnected()){
    		return true;
    	}
    	else{
    	return false;}
    	
    }

	@Override
//voice recognizer	
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     	
		if(requestCode ==REQUEST_CODE && resultCode==RESULT_OK){
			if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			     matchTextDialog = new Dialog(MainActivity.this);
			     matchTextDialog.setContentView(R.layout.dialog_matches_frag);
			     matchTextDialog.setTitle("Select Matching Text");
			     speechInText = (ListView)matchTextDialog.findViewById(R.id.speakin_list);
			     matchesText = data
					     .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			     ArrayAdapter<String> adapterVoice =    new ArrayAdapter<String>(this,
			    	     android.R.layout.simple_list_item_1, matchesText);
			     speechInText.setAdapter(adapterVoice);
			     speechInText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			     @Override
			     public void onItemClick(AdapterView<?> parent, View view,
			                             int position, long id) {
			    	 
			    	 originalText=matchesText.get(position).toString();
					adapter.add(new OneComment(true,originalText));
						
						//translation process
						

			    	class bgStuff extends AsyncTask<Void, Void, Void>{                   
			            
			            @Override
			            protected Void doInBackground(Void... params) {
			            	
			            	Translate.setClientId("etranslator123");
			            	Translate.setClientSecret("FP556NJH3bcPYNMff2OEIWhxKMqd4TZO5PyDsC5fLBM=");
			                langToBeTranslated=froSpinnerLang.getSelectedItem().toString().toUpperCase();
			                langSelected=toSpinnerLang.getSelectedItem().toString();
			            
			            	
			               try {
			            	   Language detectedLanguage = Detect.execute(originalText);//detect lang from 
			                	 
			            	   if(langToBeTranslated=="AUTO_DETECT"){
			            		   TranslatedText = Translate.execute(originalText,  Language.valueOf(langSelected));
			                       
			            	   }
			            	   else{
			                  	 
			                	   //checks if the lang z equal to detected lang
			              	    if(froSpinnerLang.getSelectedItem().toString().toUpperCase().equalsIgnoreCase(detectedLanguage.getName(Language.ENGLISH))){
			                    TranslatedText = Translate.execute(originalText, Language.valueOf(langToBeTranslated), Language.valueOf(langSelected));
			                      }  
			              	   else{
			              		   TranslatedText ="That seems to be " + detectedLanguage.getName(Language.ENGLISH).toUpperCase()+ ". Type only words in "+ langToBeTranslated.toUpperCase()+ "or Change to Auto Detect";
			              		   
			              	   }
			              	   
			                 }
			        		
			            	   
			            	   TranslatedText = Translate.execute(originalText, Language.GERMAN);
								
							} catch (Exception e) {
								TranslatedText = "Try again...... Server Error";
							    e.printStackTrace();
							}//end try catch block
			               
			            
							return null;
			                
			            }//end doinbackground method

			            @Override
			            protected void onPostExecute(Void result) {
			            	adapter.add(new OneComment(false, TranslatedText));
			        		edittextFromLang.setText("");
			            }
			    	}
			    	//end of asynctask class
			    	 new bgStuff().execute();                          
			         


						

						
						//end of translation process
						
			    	 matchTextDialog.hide();
			     }
			 });
			     matchTextDialog.show();
			     }
	
		
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

//display dialog for no recognizer
	public void noVoiceRecognizerDialog(){
		
		final Dialog dialogVoice = new Dialog(context);
		dialogVoice.setContentView(R.layout.no_voice_recognizer);
		dialogVoice.setTitle("Warning");
		
		Button dialogButton = (Button) dialogVoice.findViewById(R.id.no_voice_button);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogVoice.dismiss();
			}
		});

		dialogVoice.show();

	}

//display dialog for no connection
	public void noInternetConnection(){
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.no_connection);
		dialog.setTitle("Warning");
		
		Button dialogButton = (Button) dialog.findViewById(R.id.no_connection_button);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
		
	}
}
    

