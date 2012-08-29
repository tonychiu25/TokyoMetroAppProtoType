package com.example.myapp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MetroSystemBackend.MSTMetro;
import MetroSystemBackend.MetroBuilder;
import MetroSystemBackend.Route;
import MetroSystemBackend.subwaySystem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.test.InstrumentationTestCase;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;



public class MyFirstAppActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.example.myapp.MESSAGE";
	
	private MetroBuilder subBuilder;
	private subwaySystem subSys;
	private MSTMetro mstSub;
	private Route r;
	private static final String[] stations = new String[] {
		"Asakusa", "Ueno", "Shibuya", "Shinjuku", "Shin Okubo", "Akasaka-Mitsuke"
	};
	
	private void setupAutoCompleteTextField() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, stations);
        AutoCompleteTextView originStationTextField = (AutoCompleteTextView)findViewById(R.id.origin_station);
        originStationTextField.setThreshold(1);
        originStationTextField.setAdapter(adapter);
        
        AutoCompleteTextView terminusStationTextField = (AutoCompleteTextView)findViewById(R.id.terminus_station);
        terminusStationTextField.setThreshold(1);
        terminusStationTextField.setAdapter(adapter);
	}
	
	private void setUpClearFieldButtons() {
		final AutoCompleteTextView oriStationTextField;
		final AutoCompleteTextView terminsStationTextField;
		Button clearOriginButton, clearTerminusButton;
		
		clearOriginButton = (Button) findViewById(R.id.clear_origin_button);
		clearTerminusButton = (Button) findViewById(R.id.clear_terminus_button);
		
		oriStationTextField = (AutoCompleteTextView)findViewById(R.id.origin_station);
		terminsStationTextField = (AutoCompleteTextView)findViewById(R.id.terminus_station);
		
		clearOriginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				oriStationTextField.setText("");
			}
		});
		
		clearTerminusButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				terminsStationTextField.setText("");
			}
		});
	}
	
	private void calculateShortestPath() {
		Intent intent = new Intent(this, RenderListViewActivity.class);
		startActivity(intent);
	}
	
	private void setUpSubmitButton() {
		Button submitButton;
		
		submitButton = (Button) findViewById(R.id.submit_button);
		submitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				calculateShortestPath();
			}
		});
	}
	
	final static String[] ITEMS = {"blah", "floop", "gnarlp", "stuff"};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {	
    	subBuilder = new MetroBuilder();
    	subSys = new subwaySystem();
    	mstSub = new MSTMetro();
    	
		InstrumentationTestCase it = new InstrumentationTestCase();
		InputStream inputStream = this.getResources().openRawResource(R.raw.book2);
    	
    	try {
			subSys = subBuilder.buildSubwayFromLineCSV(inputStream);
			mstSub = subBuilder.buildMSTMetro(subSys);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setupAutoCompleteTextField();
        setUpClearFieldButtons();
        setUpSubmitButton();
        
        ListView lv = (ListView)findViewById(R.id.listView1);
        
        String downarrow = new String("        \u25BC");
        
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        String[] tmp = {"Hi", downarrow, "Try", "d", "Cry"};
        for (String str:tmp) {
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("title", str);
            datum.put("date", str);
            data.add(datum);
        }

        
        
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_1,
                new String[] {"title", "date"},
                new int[] {android.R.id.text1,
                           android.R.id.text2});
        lv.setAdapter(adapter);
    }
    
    public void sendMessage(View view) throws Exception {
    	EditText editText = (EditText) findViewById(R.id.origin_station);
    	//Integer s1Index = Integer.parseInt(editText.getText().toString());
    	
    	editText = (EditText) findViewById(R.id.terminus_station);
    	//Integer s2Index = Integer.parseInt(editText.getText().toString());
    	
    	//r = mstSub.getShortestPath(s1Index, s2Index);
    	
    	/*Intent intent = new Intent(this, DisplayMessageActivity.class);
    	EditText editText = (EditText) findViewById(R.id.edit_message);
    	String message = editText.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE, "DSaD");
    	startActivity(intent);*/
    	
    	new AlertDialog.Builder(this)
        .setTitle("No Title")
        .setMessage(r.getSectionLines().toString())
        .show();
    }
}