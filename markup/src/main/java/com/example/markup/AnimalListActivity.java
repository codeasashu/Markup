package com.example.markup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.markup.helper.DatabaseHelper;
import com.example.markup.model.MarkCalender;
import com.example.markup.model.MarkItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AnimalListActivity extends Activity{

	private ArrayList<String> animalsNameList,SellerIds,ItemStat;
    private HashMap<String, Integer> itemMapper;
	public static String ITEM_ID;
	DatabaseHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_list);
        setActionBar("Spec");

        //Add Button Click
        Button imgbtn = (Button) findViewById(R.id.ibn);
        imgbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Info","Got button click");
                Toast.makeText(getApplicationContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AddMark.class);
                startActivity(intent);
            }
        });


        db = new DatabaseHelper(getApplicationContext());

        ListView animalList = (ListView) findViewById(R.id.listViewAnimals);
        animalsNameList = new ArrayList<String>();
        SellerIds = new ArrayList<String>();
        ItemStat = new ArrayList<String>();
        itemMapper  = new HashMap<String, Integer>();
        getAnimalNames();

        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<?> arrayAdapter =
                new PotListAdapter(this, R.layout.animal_list_item, animalsNameList);
        // Set The Adapter
        animalList.setAdapter(arrayAdapter);
        animalList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Listid: " + SellerIds.get(position), Toast.LENGTH_SHORT).show();

                int inmid = Integer.parseInt(SellerIds.get(position));
        //      Intent intent = new Intent(getApplicationContext(), ItemOnSelect.class); //Show Calendar
        //      Intent intent = new Intent(getApplicationContext(), WeekListActivity.class); //Show WeekList
                Intent intent = new Intent(getApplicationContext(), MyCalendarActivity.class); //Show Another Calendar
                intent.putExtra(ITEM_ID, inmid);
                startActivity(intent);

            }

        });


    }
	
	public void removeAtomPayOnClickHandler(View v) {
		Object btntag = v.getTag();
		Toast.makeText(getApplicationContext(), "Button Clicked: "+btntag,   Toast.LENGTH_LONG).show();
	}

	void getAnimalNames()
    {
		List<MarkItem> items = db.getAllItems();
		Log.i("Info","Retrieving item list....");
		for(MarkItem temp: items){
			Log.i("Info","Got ITEM "+ String.valueOf(temp.getID()));
			animalsNameList.add(String.valueOf(temp.getItemName()));
			SellerIds.add(String.valueOf(temp.getID()));
			int itemStatic = db.getItemStat(temp.getID());
            itemMapper.put(String.valueOf(temp.getID()),itemStatic);

			Log.d("ItemStat before: ", String.valueOf(itemStatic));
			ItemStat.add(String.valueOf(itemStatic));
		}
		
    } 
	
	public void setActionBar(String heading) {
	    // TODO Auto-generated method stub
		setTitle(heading);
	}

    private void RemoveItem(int itemid){
        String date = String.valueOf(getDate());
        db.deleteStatOn(itemid,date);
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

	private void MarkItem(View v, int status)
	{
		int count = db.getStatItemCount(Integer.parseInt(v.getTag().toString()));
	       
    	int affected_rows=0;
    	int insert_id = 0;
		if(count == 0){
    		insert_id = (int) db.createStat(new MarkCalender(Integer.parseInt(v.getTag().toString())), status);
    		Log.d("Newly inserted id: ", String.valueOf(insert_id));
		}else{
    		affected_rows = db.updateStat(new MarkCalender(Integer.parseInt(v.getTag().toString())), status);
    		Log.d("No. of affected rows: ", String.valueOf(affected_rows));
		}
	}	
        
	public class PotListAdapter extends ArrayAdapter<String>{
		
		private List obj;
		public PotListAdapter(Context context, int resource, List objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
			this.obj = objects;
		}
		
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(AnimalListActivity.this);
                convertView = inflater.inflate(R.layout.animal_list_item, parent, false);
                holder = new ViewHolder();

                holder.text = (TextView)convertView.findViewById(R.id.atomPay_value);
                holder.Completebutton = (ImageButton)convertView.findViewById(R.id.atomPay_addPay);
                holder.InCompletebutton = (ImageButton)convertView.findViewById(R.id.atomPay_removePay);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            
            /*
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
					Toast.makeText(getApplicationContext(), "isttt",   Toast.LENGTH_SHORT).show();
                }
            });
            */
            holder.text.setText(animalsNameList.get(position));
            holder.Completebutton.setOnClickListener(new OnClickListener(){

                public void onClick(View v) {
                	// Disable Button
                //	v.setEnabled(false);
                    String item_id = v.getTag().toString();
                    String status = "";
                    if(itemMapper.containsKey(item_id)){
                        int stat = itemMapper.get(item_id);
                        if(stat == 1){
                            // Mark item as un-received
                            MarkItem(v, 0);
                            v.setBackgroundColor(Color.YELLOW);
                            itemMapper.put(item_id,0);
                            status = "Marked Received";
                            Toast.makeText(getApplicationContext(), "Marked Un-Received", Toast.LENGTH_SHORT).show();
                        }else if(stat == 0){
                            // Remove Item
                            RemoveItem(Integer.valueOf(item_id));
                            v.setBackgroundColor(0x00000000);
                            itemMapper.remove(item_id);
                            status = "Reset Item";
                            Toast.makeText(getApplicationContext(), "Reset Item", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        // Mark item as received
                        MarkItem(v, 1);
                        v.setBackgroundColor(Color.BLUE);
                        itemMapper.put(item_id,1);
                        status = "Marked Received";
                        Toast.makeText(getApplicationContext(), "Marked Received", Toast.LENGTH_SHORT).show();
                    }
                //	holder.InCompletebutton.setEnabled(true);


                   }
        	});
            
            holder.InCompletebutton.setOnClickListener(new OnClickListener(){

                public void onClick(View v) {
                    String itemid = v.getTag().toString();
                    Intent intent = new Intent(getApplicationContext(), EditActivity.class); //Show Another Calendar
                    intent.putExtra(ITEM_ID, Integer.valueOf(itemid));
                    startActivity(intent);
                	// Disable Button
                //	v.setEnabled(false);
                //	holder.Completebutton.setEnabled(true);
                	// Mark item as not received
               // 	MarkItem(v, 0);
                //	Toast.makeText(getApplicationContext(), "Marked Un-Received", Toast.LENGTH_SHORT).show();
                   }
        	});
            
            Log.d("ItemStat after: ", ItemStat.get(position));
            
            //If item is marked "not received"
            if(Integer.parseInt(ItemStat.get(position)) == 0){
            //	holder.InCompletebutton.setEnabled(false);
            	holder.InCompletebutton.setBackgroundColor(Color.YELLOW);
            	holder.InCompletebutton.setClickable(false);
            }else if(Integer.parseInt(ItemStat.get(position)) == 1){
            //	holder.Completebutton.setEnabled(false);
            	holder.Completebutton.setBackgroundColor(Color.BLUE);
                holder.Completebutton.setClickable(false);
            }

       //     holder.Completebutton.setFocusable(false);
        //    holder.InCompletebutton.setFocusable(false);
            holder.Completebutton.setTag( SellerIds.get(position) );
            holder.InCompletebutton.setTag( SellerIds.get(position) );
            return convertView;
        }

    }

    static class ViewHolder{
        TextView text;
        ImageButton Completebutton;
        ImageButton InCompletebutton;
	}
}
