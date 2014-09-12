package com.example.markup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.markup.helper.DatabaseHelper;
import com.example.markup.model.MarkItem;

public class AddMark extends Activity implements OnClickListener{

	private EditText itemName;
	private EditText itemPrice;
	private String _itemname;
	private String _itemprice;
	private Button cancelButton;
	private Button saveButton;
	
	DatabaseHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_mark);
		db = new DatabaseHelper(getApplicationContext());
		//Item Name
		itemName = (EditText) findViewById(R.id.insertItemName);
		itemPrice = (EditText) findViewById(R.id.insertItemPrice);
		//Save button and cancel buttons
		cancelButton = (Button) findViewById(R.id.cancelbtn);
		cancelButton.setOnClickListener(this);
		saveButton = (Button) findViewById(R.id.savebtn);
		saveButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.cancelbtn){
			//IF cancel, return to previous screen
			db.deleteAllItems();
			finish();
		}else if(v.getId() == R.id.savebtn){
	         
	        /**
	         * CRUD Operations
	         * */
	        // Inserting Contacts
	        Log.d("Insert: ", "Inserting .."); 
	        
	        //Getting variables
	        _itemname = itemName.getText().toString();
	        _itemprice = itemPrice.getText().toString();
	        
	        db.addItem(new MarkItem(_itemname, _itemprice));
            Intent intent = new Intent(getApplicationContext(), AnimalListActivity.class);
            startActivity(intent);
            /*
	        // Reading all contacts
	        Log.d("Reading: ", "Reading all items.."); 
	        List<MarkItem> contacts = db.getAllItems();       
	         
	        for (MarkItem cn : contacts) {
	            String log = "Id: "+cn.getID()+" ,Name: " + cn.getItemName() + " ,Price: " + cn.getItemPrice();
	                // Writing Contacts to log
	            Log.d("Name: ", log);
	        }
	        
	        Intent intent = new Intent(this,WidgetProvider.class);
	        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
	        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
	        // since it seems the onUpdate() is only fired on that:	 
	        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), WidgetProvider.class));
	        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
	        sendBroadcast(intent);
			// Release from the existing UI and go back to the previous UI
			finish();
		    */
		}
}}
