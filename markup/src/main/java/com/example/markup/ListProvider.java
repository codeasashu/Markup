package com.example.markup;

import java.util.ArrayList;
import java.util.List;

import com.example.markup.helper.DatabaseHelper;
import com.example.markup.model.MarkItem;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class ListProvider implements RemoteViewsFactory {
	private ArrayList<ListItem> listItemList = new ArrayList<ListItem>();
	private Context context = null;
	private Intent remoteIntent;
	private int appWidgetId;

	DatabaseHelper db;
	
	public ListProvider(Context context, Intent intent) {
		this.context = context;
		this.remoteIntent = intent;
		
		appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
		populateListItem();
	}

	private void populateListItem() {
		db = new DatabaseHelper(this.context);
		List<MarkItem> contacts = db.getAllItems();   
		for (MarkItem cn : contacts) {
			ListItem listItem = new ListItem();
			listItem.itemid = cn.getID();
			listItem.heading = cn.getItemName();
			listItem.content = cn.getID()
					+ " Price: "+cn.getItemPrice();
			listItemList.add(listItem);
		}
		 
	}

	@Override
	public int getCount() {
		return listItemList.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 *Similar to getView of Adapter where instead of View
	 *we return RemoteViews 
	 * 
	 */
	@Override
	public RemoteViews getViewAt(int position) {
		final RemoteViews remoteView = new RemoteViews(
				context.getPackageName(), R.layout.list_row);
		ListItem listItem = listItemList.get(position);
		remoteView.setTextViewText(R.id.heading, listItem.heading);
		remoteView.setTextViewText(R.id.content, listItem.content);
		if(position == 1){
			remoteView.setBoolean(R.id.buttonwidget, "setEnabled", false);
		}
		
		
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(WidgetProvider.BROADCAST_BTN_COMPLETE, 1);
        fillInIntent.putExtra(WidgetProvider.EXTRA_ITEM, position);
        fillInIntent.putExtra(WidgetProvider.ITEM_ID, listItem.itemid);
        remoteView.setOnClickFillInIntent(R.id.buttonwidget, fillInIntent);
        
        Intent fillInIntentb = new Intent();
        fillInIntentb.putExtra(WidgetProvider.BROADCAST_BTN_COMPLETE, 0);
        fillInIntentb.putExtra(WidgetProvider.EXTRA_ITEM, position);
        fillInIntentb.putExtra(WidgetProvider.ITEM_ID, listItem.itemid);
        remoteView.setOnClickFillInIntent(R.id.buttonwidgetb, fillInIntentb);
        
		return remoteView;
	}
	

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDataSetChanged() {
	
	}

	@Override
	public void onDestroy() {
	}

}
