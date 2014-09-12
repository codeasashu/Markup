package com.example.markup;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.markup.helper.DatabaseHelper;
import com.example.markup.model.MarkCalender;
import com.example.markup.model.MarkItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ashutosh on 9/5/2014.
 */

@TargetApi(3)
public class MyCalendarActivity extends Activity implements OnClickListener {
    private static final String tag = "MyCalendarActivity";

    DatabaseHelper db;
    private String ITEM_NAME;
    public static String ITEM_ID;

    private int times = 0;
    private TextView currentMonth;
    private Button selectedDayMonthYearButton;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private Button addEvent;
    private GridView calendarView;
    private int counter;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    protected int _currentItemid;

    @SuppressLint("NewApi")
    private int month, year;
    @SuppressWarnings("unused")
    @SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_calendar_view);

        db = new DatabaseHelper(getApplicationContext());

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("abc",123);
        map.put("def",456);

        int l = 0;
        if(map.containsKey("lmn")){
            l = map.get("lmn");
        }else if(map.containsKey("def")){
            l = map.get("def");
        }

        Log.e("Got hash:",String.valueOf(l));

        Intent intent = getIntent();
        this._currentItemid =  intent.getIntExtra(AnimalListActivity.ITEM_ID,0);
        ITEM_NAME = getItemName(this._currentItemid);

        setTitle("Markup - "+ITEM_NAME);

        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);

        selectedDayMonthYearButton = (Button) this.findViewById(R.id.selectedDayMonthYear);
        selectedDayMonthYearButton.setText("Total Due: ");

        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (TextView) this.findViewById(R.id.currentMonth);
        currentMonth.setText(DateFormat.format(dateTemplate,_calendar.getTime()));

        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        addEvent = (Button) this.findViewById(R.id.addEvent);
        addEvent.setOnClickListener(this);

        calendarView = (GridView) this.findViewById(R.id.calendar);

        // Initialised
        adapter = new GridCellAdapter(getApplicationContext(),
                R.id.calendar_day_gridcell, month, year);
    //    _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    private String getItemName(int item_id) {
        // TODO Auto-generated method stub
        MarkItem itemname = db.getItem(item_id);
        return itemname.getItemName();
    }

    /**
     *
     * @param month
     * @param year
     */
    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(getApplicationContext(),
                R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate,_calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == prevMonth) {
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);

            setGridCellAdapterToDate(month, year);

        }
        if (v == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
            Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
        if(v == addEvent) {
            Intent intent = new Intent(getApplicationContext(), EditActivity.class); //Show Another Calendar
            intent.putExtra(ITEM_ID, _currentItemid);
            startActivity(intent);
        }
    }



    @Override
    public void onDestroy() {
        Log.d(tag, "Destroying View ...");
        super.onDestroy();
    }

    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
                "Wed", "Thu", "Fri", "Sat" };
        private final String[] months = { "January", "February", "March",
                "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };
        private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31 };
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private float totalItemPrice = 0;
        private Button gridcell;
        private float totalP;
        private List<MarkCalender> _dbListStat;
        private MarkItem _dbListPrice;
        private TextView num_events_per_day;
        private final HashMap<String, Integer> eventsPerMonthMap, mapper;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "dd-MMM-yyyy");

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId,
                               int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            this._dbListStat = db.getItemCurrentMonth(_currentItemid);
            long itemid = Long.parseLong(String.valueOf(_currentItemid));
            this._dbListPrice = db.getItem(itemid);
            this.totalItemPrice = Float.valueOf(this._dbListPrice.getItemPrice());

            mapper  = new HashMap<String, Integer>();

            Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
                    + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());
            String mmn = String.valueOf(lPadZero(month,2));
            totalP = db.getTotalPrice(_currentItemid,year+"-"+mmn);

            // Print Month
            printMonth(month, year);

            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy) {
            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
                    + daysInMonth + " days.");

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            Log.d(tag, "Week Day:" + currentWeekDay + " is "
                    + getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                Log.d(tag,
                        "PREV MONTH:= "
                                + prevMonth
                                + " => "
                                + getMonthAsString(prevMonth)
                                + " "
                                + String.valueOf((daysInPrevMonth
                                - trailingSpaces + DAY_OFFSET)
                                + i));
                list.add(String
                        .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                + i)
                        + "-GREY"
                        + "-"
                        + getMonthAsString(prevMonth)
                        + "-"
                        + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                Log.d(currentMonthName, String.valueOf(i) + " "
                        + getMonthAsString(currentMonth) + " " + yy);
                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) + "-BLUE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-"
                        + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }

        /**
         * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
         * ALL entries from a SQLite database for that month. Iterate over the
         * List of All entries, and get the dateCreated, which is converted into
         * day.
         *
         * @param year
         * @param month
         * @return
         */
        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
                                                                    int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();

            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.screen_gridcell, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            gridcell.setOnClickListener(this);

            // ACCOUNT FOR SPACING

            Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                    num_events_per_day = (TextView) row
                            .findViewById(R.id.num_events_per_day);
                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

            // Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
                    + theyear);

            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(getResources()
                        .getColor(R.color.lightgray));
            }
            if (day_color[1].equals("WHITE")) {
                gridcell.setTextColor(getResources().getColor(
                        R.color.lightgray02));
            }
            if (day_color[1].equals("BLUE")) {
                gridcell.setTextColor(getResources().getColor(R.color.orrange));
            }

            //Enable or Disable Check
        //    int CurrentMonth = month;
            int CurrentMonth = Arrays.asList(months).indexOf(themonth) + 1;

            String currMonth = String.valueOf(lPadZero(CurrentMonth,2));
            String nextMonth = String.valueOf(lPadZero(CurrentMonth+1,2));
            String currDay = String.valueOf(lPadZero(Integer.parseInt(theday),2));
            String secondaryDate = theyear+"-"+currMonth+"-"+currDay;

            for(MarkCalender marker:this._dbListStat){
                String[] monthsec = marker.getStatDate().split("-");

                if(currMonth.equals(monthsec[1]) && secondaryDate.equals(marker.getStatDate())) {
                    if (marker.GetStat() == 0) {
                        mapper.put(marker.getStatDate(),0);
                        gridcell.setTextColor(getResources().getColor(R.color.Aqua));
                        gridcell.setBackgroundColor(Color.YELLOW);
                    //    gridcell.setClickable(false);

                        Log.e("Setup at: ( "+CurrentMonth+" ): "+secondaryDate,"true");
                    } else if (marker.GetStat() == 1) {
                        mapper.put(marker.getStatDate(),1);
                        gridcell.setTextColor(getResources().getColor(R.color.BurlyWood));
                        gridcell.setBackgroundColor(Color.BLUE);
                        this.totalItemPrice += this.totalItemPrice;

                        Log.e("Setup at ( "+CurrentMonth+" ): "+secondaryDate,"false");
                    }
                }
            //    this.totalItemPrice = this.totalItemPrice*counter;
            //    Log.e("Item ID( "+String.valueOf(marker.getID())+" ) : "+ String.valueOf(marker.getStatDate()) +
            //            "=",String.valueOf(marker.GetStat()) + " Matching "+ secondaryDate);
            }
            selectedDayMonthYearButton.setText("Total Due: " + totalP);
            return row;
        }

        /**
         * @param in The integer value
         * @param fill The number of digits to fill
         * @return The given value left padded with the given number of digits
         */
        public String lPadZero(int in, int fill){

            boolean negative = false;
            int value, len = 0;

            if(in >= 0){
                value = in;
            } else {
                negative = true;
                value = - in;
                in = - in;
                len ++;
            }

            if(value == 0){
                len = 1;
            } else{
                for(; value != 0; len ++){
                    value /= 10;
                }
            }

            StringBuilder sb = new StringBuilder();

            if(negative){
                sb.append('-');
            }

            for(int i = fill; i > len; i--){
                sb.append('0');
            }

            sb.append(in);
            return sb.toString();
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();
            String[] day_chosen = date_month_year.split("-");
            int CurrentMonth = Arrays.asList(months).indexOf(day_chosen[1]) + 1;
            String currMonth = String.valueOf(lPadZero(CurrentMonth,2));
            String currDay = String.valueOf(lPadZero(Integer.parseInt(day_chosen[0]),2));
            String secondaryDate = day_chosen[2]+"-"+currMonth+"-"+currDay;
        //    float priceChange=totalP;

            if(mapper.containsKey(secondaryDate)){
                //if Marked
                int status = mapper.get(secondaryDate);
                if(status == 1){
                    db.updateStatOn(new MarkCalender(_currentItemid), 0,secondaryDate);
                    view.setBackgroundColor(Color.YELLOW);
                    mapper.put(secondaryDate,0);
            //        priceChange = priceChange - this.totalItemPrice;
                }
                else{
                //    db.updateStatOn(new MarkCalender(_currentItemid), 1,secondaryDate);
                    db.deleteStatOn(_currentItemid,secondaryDate);
                    view.setBackgroundColor(0x00000000);
                //    mapper.put(secondaryDate,1);
                    mapper.remove(secondaryDate);
            //        priceChange = priceChange + this.totalItemPrice;
                }
            }else{
                //Set as Marked
                db.createStatOn(new MarkCalender(_currentItemid), 1,secondaryDate);
                view.setBackgroundColor(Color.BLUE);
                mapper.put(secondaryDate,1);
            //    priceChange = priceChange + this.totalItemPrice;
            }
            float totalPb = db.getTotalPrice(_currentItemid,year+"-"+currMonth);

            Log.e("Selected day: ",secondaryDate);
            selectedDayMonthYearButton.setText("Total Due: " + totalPb);

        //    selectedDayMonthYearButton.setText("Selected: " + this.totalItemPrice);
        //    Log.e("Selected date", date_month_year);
            try {
                Date parsedDate = dateFormatter.parse(date_month_year);
                Log.d(tag, "Parsed Date: " + parsedDate.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }
    }
}

