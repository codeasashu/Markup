package com.example.markup;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;


public class TestActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Button btnShowAnimal=(Button)findViewById(R.id.butttonShowAnimal);
		Button btnShowCal = (Button)findViewById(R.id.butttonShowCalender);
		Button btnShowWeek = (Button)findViewById(R.id.butttonShowWeek);
		Button btnShowCall = (Button)findViewById(R.id.butttonShowCal);

		btnShowCal.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View v) {
                // TODO Auto-generated method stub
                
                /// Create Intent for AnimalListActivity and Start The Activity
                Intent intentAnimalList=new Intent(getApplicationContext(),SampleTimesSquareActivity.class);
                startActivity(intentAnimalList);
            }
        });

        btnShowCall.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                /// Create Intent for AnimalListActivity and Start The Activity
                Intent intentAnimalList=new Intent(getApplicationContext(),MyCalendarActivity.class);
                startActivity(intentAnimalList);
            }
        });

		btnShowAnimal.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View v) {
                // TODO Auto-generated method stub
                
                /// Create Intent for AnimalListActivity and Start The Activity
                Intent intentAnimalList=new Intent(getApplicationContext(),AnimalListActivity.class);
                startActivity(intentAnimalList);
            }
        });

        btnShowWeek.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                /// Create Intent for AnimalListActivity and Start The Activity
                Intent intentWeekList=new Intent(getApplicationContext(),WeekListActivity.class);
                startActivity(intentWeekList);
            }
        });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	  MenuInflater inflater = getMenuInflater();
	 
	  inflater.inflate(R.menu.main, menu);
	  return super.onCreateOptionsMenu(menu);
	}

}
