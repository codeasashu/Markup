package com.example.markup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.markup.helper.DatabaseHelper;
import com.example.markup.model.MarkItem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ashutosh on 9/10/2014.
 */
public class EditActivity extends Activity {

    public static String ITEM_ID;
    private int _currentItemid;
    private ArrayList<Integer> selectedItemList;
    private String ITEM_NAME;
    private float ITEM_PRICE;
    private String[] ITEM_OPTIONS;
    private StringBuilder stringBuilder;
    private ArrayList<String> freezeList;
    private String myList[] = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_edit);
        db = new DatabaseHelper(getApplicationContext());
        Intent intent = getIntent();
        this._currentItemid =  intent.getIntExtra(MyCalendarActivity.ITEM_ID,0);
        MarkItem item = db.getItem(this._currentItemid);
        ITEM_NAME = getItemName(item);
        ITEM_PRICE = getItemPrice(item);
        ITEM_OPTIONS = getItemOptions(this._currentItemid);
        freezeList = new ArrayList<String>(Arrays.asList(ITEM_OPTIONS));
    //    myList = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        //Selected Item Index List
        selectedItemList = new ArrayList<Integer>();

        final EditText editItem = (EditText)findViewById(R.id.editItem);
        editItem.setText(ITEM_NAME, TextView.BufferType.EDITABLE);

        final EditText editPrice = (EditText)findViewById(R.id.editItemPrice);
        editPrice.setText(String.format("%.2f", ITEM_PRICE), TextView.BufferType.EDITABLE);

        Button freeze = (Button) findViewById(R.id.freezeitem);
        freeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(myList);
            }
        });

        Button saver = (Button) findViewById(R.id.editsave);
        saver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    builder.show();
            //    String selected = String.valueOf(spinner1.getSelectedItem());

            //    Toast.makeText(getApplicationContext(), "Selected:"+selected, Toast.LENGTH_SHORT).show();
                String delim = "";
                stringBuilder = new StringBuilder();
                if(freezeList.size() != 0) {
                    for (int i = 0; i < freezeList.size(); i++) {
                        stringBuilder.append(delim).append(freezeList.get(i));
                        delim = ",";
                    }
                }

                String newText = editItem.getText().toString();
                String priceText = editPrice.getText().toString();
                if(newText.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter some name", Toast.LENGTH_SHORT).show();
                }else {
                    saveItemName(_currentItemid,newText,priceText);
                    saveOptions(_currentItemid,stringBuilder.toString());

                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), AnimalListActivity.class);
                startActivity(intent);
                }
            }
        });

        setTitle(ITEM_NAME);
    }

    private void showAlert(final String lister[]){
        boolean[] bl = new boolean[lister.length];

        for(int j=0; j < bl.length;j++ )
        {
            if(freezeList.contains(String.valueOf(j))){
                bl[j] = true;
            }else{
                bl[j] = false;
            }
        }
        final multiChoiceListDialogListener dialogListener = new multiChoiceListDialogListener() {
            @Override
            public void onOkay(ArrayList<Integer> arrayList) {
                if(arrayList.size() != 0){
                    for (int i=0;i<arrayList.size();i++){
                        if(freezeList.contains(arrayList.get(i))){
                            continue;
                        }else{
                            freezeList.add(String.valueOf(arrayList.get(i)));
                        }
                    }
                }
            }

            @Override
            public void onCancel() {

            }
        };


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        selectedItemList.clear();

        builder.setTitle("Select Day")
                .setMultiChoiceItems(lister,bl,new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            selectedItemList.add(i);
                        }else{
                            if(selectedItemList.contains(i)) {
                                selectedItemList.remove(Integer.valueOf(i));
                            }
                            if(freezeList.contains(String.valueOf(i))){
                                freezeList.remove(String.valueOf(i));
                            }
                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogListener.onOkay(selectedItemList);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogListener.onCancel();
                    }
                });

        builder.show();
    }
    private interface multiChoiceListDialogListener{
        public void onOkay(ArrayList<Integer> arrayList);
        public void onCancel();

    }

    private String[] getItemOptions(int _currentItemid){
        String[] options = {};
        options = db.getItemOptions(_currentItemid);
        return options;
    }

    private String getItemName(MarkItem item) {
        // TODO Auto-generated method stub
        return item.getItemName();
    }

    private void saveOptions(int itemid, String options){
        int count = db.getItemOptionsCount(itemid);
        if(count == 0){
            db.addOption(itemid,options);
        }else{
            db.updateOption(itemid,options);
        }
    }

    private float getItemPrice(MarkItem item){
        return Float.valueOf(item.getItemPrice());
    }

    private void saveItemName(int item_id,String itemname,String price) {
        // TODO Auto-generated method stub
        db.updateItemName(new MarkItem(item_id,itemname,price));
    }
}
