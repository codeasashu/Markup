package com.example.markup;

import java.util.Calendar;
import java.util.Date;

import com.example.markup.helper.DatabaseHelper;
import com.example.markup.model.MarkItem;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ItemOnSelect extends Activity{
	
	private String ITEM_NAME;
	private CalendarPickerView calendar;
	DatabaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_select);
		//Calender view
		final Calendar nextYear = Calendar.getInstance();
	    nextYear.add(Calendar.YEAR, 1);

	    final Calendar lastYear = Calendar.getInstance();
	    lastYear.add(Calendar.YEAR, -1);

	    calendar = (CalendarPickerView) findViewById(R.id.calendar_viewb);
	    calendar.init(lastYear.getTime(), nextYear.getTime()) //
	        .inMode(SelectionMode.SINGLE) //
	        .withSelectedDate(new Date());
	    
		db = new DatabaseHelper(getApplicationContext());
		Intent intent = getIntent();
		int item_id =  intent.getIntExtra(AnimalListActivity.ITEM_ID,0);
		ITEM_NAME = getItemName(item_id);
	//	setActionBar(String.valueOf(item_id));
		setTitle(ITEM_NAME);
		
		
	}
	
	private String getItemName(int item_id) {
		// TODO Auto-generated method stub
		MarkItem itemname = db.getItem(item_id);
		return itemname.getItemName();
	}

	public void setActionBar(MarkItem iTEM_NAME2) {
	    // TODO Auto-generated method stub
		setTitle((CharSequence) iTEM_NAME2);
	}

}
