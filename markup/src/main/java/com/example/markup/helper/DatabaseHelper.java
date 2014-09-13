package com.example.markup.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.markup.model.MarkCalender;
import com.example.markup.model.MarkItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper{

	// Logcat tag
		private static final String LOG = "DatabaseHelper";

		// Database Version
		private static final int DATABASE_VERSION = 1;

		// Database Name
		private static final String DATABASE_NAME = "MarkupManager";

		// Table Names
		private static final String TABLE_ITEMS = "items";
		private static final String TABLE_STATS = "stats";
		private static final String TABLE_OPTIONS = "options";

		// ITEMS Table - column names
		private static final String KEY_ID = "id";
		private static final String KEY_ITEMNAME = "item_name";
		private static final String KEY_ITEMPRICE = "item_price";
		private static final String KEY_ITEMCREATED_AT = "created_at";

		// STATS Table - column names
		private static final String KEY_STATDATE = "item_date";
		private static final String KEY_ITEMID = "item_id";
		private static final String KEY_ITEMSTATUS = "item_stat";

        // OPTIONS Table - coun
        private static final String KEY_FREEZE = "item_freeze";



		// Table Create Statements
		// ITEMS table create statement
		private static final String CREATE_TABLE_ITEMS = "CREATE TABLE "
				+ TABLE_ITEMS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ITEMNAME
				+ " TEXT," + KEY_ITEMPRICE + " TEXT," + KEY_ITEMCREATED_AT
				+ " DATETIME" + ")";

		// STAT table create statement
		private static final String CREATE_TABLE_STATS = "CREATE TABLE " + TABLE_STATS
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_STATDATE + " DATETIME,"
				+ KEY_ITEMID + " INTEGER," + KEY_ITEMSTATUS + " TEXT"
				+ ")";

        // STAT table create statement
        private static final String CREATE_TABLE_OPTIONS = "CREATE TABLE " + TABLE_OPTIONS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"+ KEY_ITEMID + " INTEGER,"
            + KEY_FREEZE + " TEXT"+ ")";


		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			// creating required tables
			db.execSQL(CREATE_TABLE_ITEMS);
			db.execSQL(CREATE_TABLE_STATS);
            db.execSQL(CREATE_TABLE_OPTIONS);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// on upgrade drop older tables
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ITEMS);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_STATS);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_OPTIONS);

			// create new tables
			onCreate(db);
		}

		// ------------------------ "ITEMS" table methods ----------------//

		// Adding new item
	    public void addItem(MarkItem markitem) {
	        SQLiteDatabase db = this.getWritableDatabase();
	 
	        ContentValues values = new ContentValues();
	        values.put(KEY_ITEMNAME, markitem.getItemName()); // Item Name
	        values.put(KEY_ITEMPRICE, markitem.getItemPrice()); // Item Price
	        values.put(KEY_ITEMCREATED_AT, getDateTime()); // Item creation Date
	 
	        // Inserting Row
	        db.insert(TABLE_ITEMS, null, values);
	        db.close(); // Closing database connection
	    }

		/*
		 *  Get single item
		 */
		public MarkItem getItem(long item_id) {
			SQLiteDatabase db = this.getReadableDatabase();

			String selectQuery = "SELECT  * FROM " + TABLE_ITEMS + " WHERE "
					+ KEY_ID + " = " + item_id;

			Log.e(LOG, selectQuery);

			Cursor c = db.rawQuery(selectQuery, null);

			MarkItem td = new MarkItem();
			
			if(c.moveToFirst()) { //edit
				
				td.setID(c.getInt(c.getColumnIndex(KEY_ID)));
				td.setName((c.getString(c.getColumnIndex(KEY_ITEMNAME))));
				td.setItemCreatedAt(c.getString(c.getColumnIndex(KEY_ITEMCREATED_AT)));
				td.setPrice(c.getString(c.getColumnIndex(KEY_ITEMPRICE)));
			}else{
				
				td = null;
			}
			
			return td;
		}

		/*
		 * Get all items
		 * 
		 */
		public List<MarkItem> getAllItems() {
			List<MarkItem> items = new ArrayList<MarkItem>();
			String selectQuery = "SELECT  * FROM " + TABLE_ITEMS;

			Log.e(LOG, selectQuery);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (c.moveToFirst()) {
				do {
					MarkItem td = new MarkItem();
					
					td.setID(Integer.parseInt(c.getString(0)));
	                td.setName(c.getString(1));
	                td.setItemCreatedAt(c.getString(2));
					// adding to todo list
					items.add(td);
				} while (c.moveToNext());
			}

			return items;
		}

		/*
		 * @TODO : getting all items under single date
		 * 
		 */
		/*
		public List<Todo> getAllToDosByTag(String tag_name) {
			List<Todo> todos = new ArrayList<Todo>();

			String selectQuery = "SELECT  * FROM " + TABLE_TODO + " td, "
					+ TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg."
					+ KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
					+ " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
					+ "tt." + KEY_TODO_ID;

			Log.e(LOG, selectQuery);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (c.moveToFirst()) {
				do {
					Todo td = new Todo();
					td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
					td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
					td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

					// adding to todo list
					todos.add(td);
				} while (c.moveToNext());
			}

			return todos;
		}
		*/

		/*
		 * getting items count
		 */
		public int getItemsCount() {
			String countQuery = "SELECT  * FROM " + TABLE_ITEMS;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);

			int count = cursor.getCount();
			cursor.close();

			// return count
			return count;
		}
		

		/*
		 * @TODO: Updating a item
		 */
        public int updateItemName(MarkItem item) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ITEMNAME, item.getItemName());
            values.put(KEY_ITEMPRICE,item.getItemPrice());

            // updating row
            return db.update(TABLE_ITEMS, values, KEY_ID + " = ?",
                    new String[] { String.valueOf(item.getID()) });
        }
		/*
		public int updateItem(MarkItem item) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_ITEMNAME, item.getItemName());

			// updating row
			return db.update(TABLE_ITEMS, values, KEY_ID + " = ?",
					new String[] { String.valueOf(item.getID()) });
		}
		*/

		/*
		 * Deleting a item
		 */
		public void deleteItem(long item_id) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_ITEMS, KEY_ID + " = ?",
					new String[] { String.valueOf(item_id) });
		}

        public void deleteStatOn(int item_id, String date){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_STATS, KEY_ITEMID+"='"+item_id+"' and "+KEY_STATDATE+" like '"+date+"'", null);
        }
		
		public void deleteAllItems() {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("delete from "+ TABLE_ITEMS);
		}

    // ------------- OPTIONS=--------------//
    public int getItemOptionsCount(int itemid){

        String countQuery = "SELECT  * FROM " + TABLE_OPTIONS + " WHERE "
                + KEY_ITEMID  + " = " + itemid;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public String[] getItemOptions(int itemid){
        String[] options = {};
        String selectQuery = "SELECT  * FROM " + TABLE_OPTIONS + " WHERE "
                + KEY_ITEMID + " = " + itemid;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                MarkCalender t = new MarkCalender();
                t.setID(c.getInt((c.getColumnIndex(KEY_ID))));
                t.SetItemId(c.getInt(c.getColumnIndex(KEY_ITEMID)));
                String option = c.getString(c.getColumnIndex(KEY_FREEZE));
                options = option.split(",");
            } while (c.moveToNext());
        }
        return options;
    }

    public int updateOption(int itemid, String option) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FREEZE,option);

        // updating row
        return db.update(TABLE_OPTIONS, values, KEY_ITEMID + " = ?",
                new String[] { String.valueOf(itemid) });
    }

    public long addOption(int itemid, String options) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEMID, itemid);
        values.put(KEY_FREEZE , options);
        // insert row
        long insert_id = db.insert(TABLE_OPTIONS, null, values);
        return insert_id;
    }
		
		// ------------------------ "STATS" table methods ----------------//

		/*
		 * Creating STAT
		 */
		public long createStat(MarkCalender stat, int status) {
			SQLiteDatabase db = this.getWritableDatabase();

			String item_status = (status == 1)?MarkCalender._item_mark_complete:MarkCalender._item_mark_incomplete;
			
			ContentValues values = new ContentValues();
			values.put(KEY_ITEMID, stat.GetItemId());
			values.put(KEY_ITEMSTATUS , item_status);
			values.put(KEY_STATDATE, getDate());

			// insert row
			long insert_id = db.insert(TABLE_STATS, null, values);
			return insert_id;
		}

    /*
     * Creating STAT
     */
    public long createStatOn(MarkCalender stat, int status, String dater) {
        SQLiteDatabase db = this.getWritableDatabase();

        String item_status = (status == 1)?MarkCalender._item_mark_complete:MarkCalender._item_mark_incomplete;

        ContentValues values = new ContentValues();
        values.put(KEY_ITEMID, stat.GetItemId());
        values.put(KEY_ITEMSTATUS , item_status);
        values.put(KEY_STATDATE, dater);

        // insert row
        long insert_id = db.insert(TABLE_STATS, null, values);
        return insert_id;
    }
		/**
		 * getting item stat
		 * */
		public List<MarkCalender> getStatItem(String markcal, long item_id) {
			List<MarkCalender> stats = new ArrayList<MarkCalender>();
			String selectQuery = "SELECT  * FROM " + TABLE_STATS + " WHERE "
					+ KEY_STATDATE  + " = " + markcal + " AND " 
					+ KEY_ITEMID + " = " + item_id;

			Log.e(LOG, selectQuery);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (c.moveToFirst()) {
				do {
					MarkCalender t = new MarkCalender();
					t.setID(c.getInt((c.getColumnIndex(KEY_ID))));
					t.SetItemId(c.getInt(c.getColumnIndex(KEY_ITEMID)));
					Log.e("ITEM FOUND. item id: ",String.valueOf(t.GetItemId()));
					// adding to stat list
					stats.add(t);
				} while (c.moveToNext());
			}
			return stats;
		}

		/*
		 * Updating a stat
		 */
		public int updateStat(MarkCalender markcal, int status) {
            String date = String.valueOf(getDate());
			List itemsp = getStatItem(date,markcal.GetItemId());
			
			SQLiteDatabase db = this.getWritableDatabase();

			String item_status = String.valueOf(status);
	//		String item_status = (status == 1)?MarkCalender._item_mark_complete:MarkCalender._item_mark_incomplete;
			
			ContentValues values = new ContentValues();
			values.put(KEY_ITEMSTATUS, item_status);
			Log.d("Item ID updating is: ", String.valueOf(markcal.GetItemId()));
			Log.d("Item ID status will be: ", item_status);
			// updating row
			return db.update(TABLE_STATS, values, (KEY_ITEMID + " = ? AND " + KEY_STATDATE + " = "+ "'"+date+"'"),
					new String[] { String.valueOf(markcal.GetItemId()) });
		}

        /*
         * Updating a stat on date
         */
        public int updateStatOn(MarkCalender markcal, int status, String dater) {
            String date = String.valueOf(dater);
            List itemsp = getStatItem(date,markcal.GetItemId());

            SQLiteDatabase db = this.getWritableDatabase();

            String item_status = String.valueOf(status);
            //		String item_status = (status == 1)?MarkCalender._item_mark_complete:MarkCalender._item_mark_incomplete;

            ContentValues values = new ContentValues();
            values.put(KEY_ITEMSTATUS, item_status);
            Log.d("Item ID updating is: ", String.valueOf(markcal.GetItemId()));
            Log.d("Item ID status will be: ", item_status);
            // updating row
            return db.update(TABLE_STATS, values, (KEY_ITEMID + " = ? AND " + KEY_STATDATE + " = "+ "'"+date+"'"),
                    new String[] { String.valueOf(markcal.GetItemId()) });
        }

    /*
     * getting Current Month Stat
     */

    public List getItemCurrentMonth(int itemid){
        String date = GetFirstDay();
        List stats = new ArrayList();

        String countQuery = "SELECT  * FROM " + TABLE_STATS + " WHERE "
                + KEY_ITEMID  + " = " + itemid;
        //        + " AND " + KEY_STATDATE  + " > " + "'"+date+"'";


		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(countQuery, null);

		// looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                MarkCalender t = new MarkCalender();
                t.setID(c.getInt((c.getColumnIndex(KEY_ID))));
                t.SetItemId(c.getInt(c.getColumnIndex(KEY_ITEMID)));
                t.setStatDate(c.getString(c.getColumnIndex(KEY_STATDATE)));
                t.SetStat(c.getInt(c.getColumnIndex(KEY_ITEMSTATUS)));

                // adding to stat list
                stats.add(t);
            } while (c.moveToNext());
        }
        return stats;
    }

    public float getTotalPrice(int item_id, String date){
        float total = 0;
        String countQuery = "SELECT "+ TABLE_ITEMS +"."+KEY_ITEMPRICE+", "+TABLE_STATS+"."+KEY_ITEMSTATUS+" FROM " + TABLE_STATS + " "
                + "INNER JOIN "+TABLE_ITEMS+" ON "+TABLE_ITEMS+"."+KEY_ID+" = "+TABLE_STATS+"."+KEY_ITEMID
                +" WHERE strftime('%Y-%m',"+KEY_STATDATE+") = '" + date + "' AND "+KEY_ITEMID +" = "+ item_id;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
        //        Log.d("Stat","Item Price: "+c.getInt(c.getColumnIndex(KEY_ITEMPRICE))+", Item Stat: "+c.getInt(c.getColumnIndex(KEY_ITEMSTATUS)));
                if(c.getInt(c.getColumnIndex(KEY_ITEMSTATUS)) == 1){
                    total += c.getInt(c.getColumnIndex(KEY_ITEMPRICE));
                }else{
                    total -= c.getInt(c.getColumnIndex(KEY_ITEMPRICE));
                }
            } while (c.moveToNext());
        }
        return total;
    }


    private String GetFirstDay(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-01", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
		
		/*
		 * getting items count (as on current Date)
		 */
		public int getStatItemCount(int item_id) {
			String date = String.valueOf(getDate());
			String countQuery = "SELECT  * FROM " + TABLE_STATS + " WHERE "
					+ KEY_ITEMID  + " = " + item_id
					+ " AND " + KEY_STATDATE  + " = " + "'"+date+"'";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			
			int count = cursor.getCount();
			cursor.close();

			// return count
			return count;
		}
		
		public int getItemStat(int item_id) {
			int item_stat = -1;
			String date = String.valueOf(getDate());
			String countQuery = "SELECT  * FROM " + TABLE_STATS + " WHERE "
					+ KEY_ITEMID  + " = " + item_id
					+ " AND " + KEY_STATDATE  + " = " + "'"+date+"'";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					item_stat = cursor.getInt((cursor.getColumnIndex(KEY_ITEMSTATUS)));
				} while (cursor.moveToNext());
			}
			cursor.close();

			
			// return count
			return item_stat;
		}
		
		/**
		 * get date
		 * */
		private String getDate() {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd", Locale.getDefault());
			Date date = new Date();
			return dateFormat.format(date);
		}

		/**
		 * get datetime
		 * */
		private String getDateTime() {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			Date date = new Date();
			return dateFormat.format(date);
		}
}
