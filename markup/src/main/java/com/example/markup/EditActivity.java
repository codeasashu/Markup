package com.example.markup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.markup.helper.DatabaseHelper;
import com.example.markup.model.MarkItem;

/**
 * Created by Ashutosh on 9/10/2014.
 */
public class EditActivity extends Activity {

    public static String ITEM_ID;
    private int _currentItemid;
    private String ITEM_NAME;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_edit);
        db = new DatabaseHelper(getApplicationContext());
        Intent intent = getIntent();
        this._currentItemid =  intent.getIntExtra(MyCalendarActivity.ITEM_ID,0);
        ITEM_NAME = getItemName(this._currentItemid);

        final EditText editItem = (EditText)findViewById(R.id.editItem);
        editItem.setText(ITEM_NAME, TextView.BufferType.EDITABLE);

        Button saver = (Button) findViewById(R.id.editsave);
        saver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newText = editItem.getText().toString();
                if(newText.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter some name", Toast.LENGTH_SHORT).show();
                }else {
                    saveItemName(_currentItemid,newText);
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AnimalListActivity.class);
                    startActivity(intent);
                }

            }
        });

        setTitle(ITEM_NAME);
    }

    private String getItemName(int item_id) {
        // TODO Auto-generated method stub
        MarkItem itemname = db.getItem(item_id);
        return itemname.getItemName();
    }

    private void saveItemName(int item_id,String itemname) {
        // TODO Auto-generated method stub
        db.updateItemName(new MarkItem(item_id,itemname,"0"));
    }
}
