package com.example.markup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.markup.helper.DatabaseHelper;
import com.example.markup.model.MarkItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ashutosh on 8/31/2014.
 */
public class WeekListActivity extends Activity {
    private ArrayList<String> listOfWeeks;
    DatabaseHelper db;
    private String ITEM_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weeklist);

        db = new DatabaseHelper(getApplicationContext());

        Intent intent = getIntent();
        int item_id =  intent.getIntExtra(AnimalListActivity.ITEM_ID,0);
        ITEM_NAME = getItemName(item_id);

        setActionBar("Markup - "+ITEM_NAME);

        TextView weekHead = (TextView) findViewById(R.id.WeektextView);
        String currentDate = String.valueOf(getCurrentDate());

        weekHead.setText("Today - "+currentDate);

        ListView weekList = (ListView) findViewById(R.id.WeekListView);

        listOfWeeks = new ArrayList<String>();
        geWeekNames();

        ArrayAdapter<?> weekAdapter = new WeekLiAdapter(this, R.layout.animal_list_item, listOfWeeks);
        weekList.setAdapter(weekAdapter);
        weekList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Listid: "+i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getItemName(int item_id) {
        // TODO Auto-generated method stub
        MarkItem itemname = db.getItem(item_id);
        return itemname.getItemName();
    }

    public void setActionBar(String heading) {
        // TODO Auto-generated method stub
        setTitle(heading);
    }

    void geWeekNames(){
        listOfWeeks.add("Sunday");
        listOfWeeks.add("Monday");
        listOfWeeks.add("Tuesday");
        listOfWeeks.add("Wednesday");
        listOfWeeks.add("Thursday");
        listOfWeeks.add("Friday");
        listOfWeeks.add("Saturday");
    }

    private String CurrentDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        return dayOfTheWeek;
    }


    public class WeekLiAdapter extends ArrayAdapter<String>{
        private List obj;
        private String WeekDayText;
        public WeekLiAdapter(Context context, int textViewResourceId, List objects) {
            super(context, textViewResourceId, objects);
            this.obj = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final WeekHolder holdit;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(WeekListActivity.this);
                convertView = inflater.inflate(R.layout.week_item,parent,false);
                holdit = new WeekHolder();
                holdit.WeekText = (TextView) convertView.findViewById(R.id.txtPresidentName);
                convertView.setTag(holdit);
            }else {
                holdit = (WeekHolder) convertView.getTag();
            }


            if (CurrentDay().equals(listOfWeeks.get(position))){
                WeekDayText = listOfWeeks.get(position) + " (Current)";
                convertView.setBackgroundColor(Color.BLUE);
            }else{
                WeekDayText = listOfWeeks.get(position);
            }
            holdit.WeekText.setText(WeekDayText);

            return convertView;
        }
    }

    static class WeekHolder{
        TextView WeekText;
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
