package com.example.markup.model;

public class MarkItem {

	//private variables
    int _id;
    String _name;
    String _price;
    String _created_at;
     
    // Empty constructor
    public MarkItem(){
         
    }
    // constructor
    public MarkItem(int id, String name, String _price){
        this._id = id;
        this._name = name;
        this._price = _price;
    }
     
    // constructor
    
    public MarkItem(String name, String _price){
        this._name = name;
        this._price = _price;
    }


    
    // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
     
    // getting item name
    public String getItemName(){
        return this._name;
    }
     
    // setting item name
    public void setName(String name){
        this._name = name;
    }
     
    // getting item price
    public String getItemPrice(){
        return this._price;
    }
     
    // setting item price
    public void setPrice(String _price){
        this._price = _price;
    }
    
 // getting item creation date
    public String getItemCreatedAt(){
        return this._created_at;
    }
    public void setItemCreatedAt( String created_at ){
    	this._created_at = created_at;
    }
}


