package com.example.markup;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class WidgetProvider extends AppWidgetProvider {

	private static final String MyOnClick = "myOnClickTag";
	private Context con;
	
	public static final String ITEM_ID = "0";
	public static final String APPWIDGET_ID = "0";
	public static final String ACTION_START_ACTIVITY = "startActivity";
	public static final String BROADCAST_BTN_COMPLETE = "completeintentId";
	public static final String BROADCAST_PARAM_PACKAGE_NAME = "intentPackage";
	public static final String BROADCAST_PARAM_CLASS_NAME = "intentClassName";
	
	public static final String TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";
    

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		final int N = appWidgetIds.length;
		/*int[] appWidgetIds holds ids of multiple instance of your widget
		 * meaning you are placing more than one widgets on your homescreen*/
		for (int i = 0; i < N; ++i) {
			RemoteViews remoteViews = updateWidgetListView(context,
					appWidgetIds[i]);
	        
			//Launching MainActivity 
			Intent clickIntent = new Intent(context, WidgetProvider.class);
			clickIntent.setAction(ACTION_START_ACTIVITY);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
			remoteViews.setPendingIntentTemplate(R.id.listViewWidget, pendingIntent);
			
			/*
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));
			PendingIntent clickPI = PendingIntent.getActivity(context, 0,
			        clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setPendingIntentTemplate(com.example.markup.R.id.listViewWidget, clickPI);		
			*/
			//Launching Add Activity
			Intent addmark = new Intent(context, AddMark.class);
			PendingIntent clickAdd = PendingIntent.getActivity(context, 0,
					addmark, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.ib, clickAdd);
			
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
		con = context;
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	//	Toast.makeText(context, "Widget Button Clicked",   Toast.LENGTH_SHORT).show();
	}
    
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
	    /*
		if(WidgetProvider.ACTION_START_ACTIVITY.equals(intent.getAction())) {
			int btn_type = intent.getIntExtra(WidgetProvider.BROADCAST_BTN_COMPLETE,0);
			int position = intent.getIntExtra(WidgetProvider.EXTRA_ITEM,0);
			int item_id =  intent.getIntExtra(WidgetProvider.ITEM_ID,0);
			
			if(btn_type == 1)
			{
				Toast.makeText(context, "Widget Complete Button Clicked: "+item_id,   Toast.LENGTH_SHORT).show();
			}else if(btn_type == 0){
				Toast.makeText(context, "Widget InComplete Button Clicked: "+item_id,   Toast.LENGTH_SHORT).show();
			}
			
		}
	    */
		
		super.onReceive(context, intent);
	}

	

	@SuppressWarnings("deprecation")
	private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

		//which layout to show on widget
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		
		//RemoteViews Service needed to provide adapter for ListView
		Intent svcIntent = new Intent(context, WidgetService.class);
		//passing app widget id to that RemoteViews Service
		svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		//setting a unique Uri to the intent
		//don't know its purpose to me right now
		svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
		//setting adapter to listview of the widget
		remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
				svcIntent);
		//setting an empty view in case of no data
		remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);      
		return remoteViews;
	}

}
