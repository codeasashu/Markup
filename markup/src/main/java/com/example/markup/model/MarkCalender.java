package com.example.markup.model;

public class MarkCalender {

	//private variables
    int _id;
    int _itemid;
    String _caldate;
    int _stat;
    
    public static final String _item_mark_complete = "1";
    public static final String _item_mark_incomplete = "0";
     
    // Empty constructor
    public MarkCalender(){
         
    }
    // constructor
    public MarkCalender(int id, int itemID, String date, int stat){
        this._id = id;
        this._itemid = itemID;
        this._caldate = date;
        this._stat = stat;
    }
     
    // constructor
    public MarkCalender(int itemID){
        this._itemid = itemID;
 
    }
    // getting ID
    public int getID(){
        return this._id;
    }
     
    public int GetItemId(){
    	return this._itemid;
    }
    
    public void SetItemId(int itemid){
    	this._itemid = itemid;
    }

    public int GetStat(){ return this._stat; }

    public void SetStat(int stat){
        this._stat = stat;
    }
    
    // setting id
    public void setID(int id){
        this._id = id;
    }
     
    // getting date
    public String getStatDate(){
        return this._caldate;
    }
     
    // setting date
    public void setStatDate(String date){
        this._caldate = date;
    }
}
