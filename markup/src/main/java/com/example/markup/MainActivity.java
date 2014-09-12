package com.example.markup;

import java.util.ArrayList;
import java.util.List;

import com.example.markup.helper.DatabaseHelper;
import com.example.markup.model.MarkItem;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	ArrayList<String> stringArrayList = new ArrayList<String>();
	// Database Helper
    DatabaseHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpDbList();
		ListView lv = (ListView)findViewById(R.id.listViewAnimals);
		lv.setOnItemClickListener( new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View itemClicked,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView textView = (TextView) itemClicked;
				String strText = textView.getText().toString();
				Toast.makeText(getApplicationContext(), "Listview Clicked: "+position,   Toast.LENGTH_SHORT).show();		
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//Fetch values from DB and prepares list
	public void setUpDbList(){
		db = new DatabaseHelper(getApplicationContext());
		List<MarkItem> contacts = db.getAllItems();   
		for (MarkItem cn : contacts) {
			stringArrayList.add(cn.getItemName());
		}
		String[] listItems = stringArrayList.toArray(new String[stringArrayList.size()]);
		//Setting up list items
		setListAdapter(new ArrayAdapter<Object>(this,android.R.layout.simple_list_item_1, listItems));
	}

}
