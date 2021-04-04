package medPal.App.PillReminder;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;

import medPal.App.AlarmAndNotification.AlarmHelper;
import medPal.App.DatabaseHelper;
import medPal.App.R;

import static android.view.View.GONE;

/**
 * Activity to let user edit (or delete) an existing pill reminder.
 */
public class EditPillReminderDetail extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private PillReminder pr;

    private int prType;

    private String time = "";
    private int hour;
    private int minute;

    private String startDate = "";
    private int sdYear;
    private int sdMonth;
    private int sdDay;

    private String endDate = "";
    private int edYear;
    private int edMonth;
    private int edDay;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pill_reminder_detail);
        getSupportActionBar().setTitle("Edit Pill Reminder");

        pr = (PillReminder) getIntent().getSerializableExtra("PillReminderObj");

        putDataIntoInputField();

        // Hide all error messages
        hideAllErrorMessage();

        // Confirm button onclick
        Button editReminderButton = (Button) findViewById(R.id.editPillReminderConfirm);
        editReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getUserInput();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        // Delete pill reminder
        TextView deletePillReminder = (TextView) findViewById(R.id.deletePillReminderConfirm);
        deletePillReminder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    deleteReminder(pr.getPillReminderId());
                } catch (UnsupportedEncodingException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Spinner element for pill reminder frequency
        final Spinner frequency = (Spinner) findViewById(R.id.editPillReminderType);
        // Spinner click listener
        frequency.setOnItemSelectedListener(this);
        // Options for frequency input field
        String[] options = {"Every Day", "Days Interval", "Specific Days of Week"};
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Attaching data adapter to spinner
        frequency.setAdapter(dataAdapter);
        frequency.setSelection(prType-1);

        // Hide time picker for pill reminder time
        RelativeLayout timePicker = (RelativeLayout) findViewById(R.id.time_picker);
        timePicker.setVisibility(GONE);
        // Automatically set current time in time picker
        TimePicker timeInput = (TimePicker) findViewById(R.id.editPillReminderTime);
        timeInput.setIs24HourView(false);
        timeInput.setHour(hour);
        timeInput.setMinute(minute);
        TextView displayTime = (TextView) findViewById(R.id.displayTime);
        displayTime.setText(to12HourFormat(hour,minute));

        // Hide date picker for pill reminder starting date
        RelativeLayout sdPicker = (RelativeLayout) findViewById(R.id.startDatePicker);
        sdPicker.setVisibility(GONE);
        // Automatically set today as starting date
        String start = sdDay + "-" + sdMonth + "-" + sdYear;
        TextView displayStartDate = (TextView) findViewById(R.id.displayStartDate);
        displayStartDate.setText(start);

        // Hide date picker for pill reminder ending date
        RelativeLayout edPicker = (RelativeLayout) findViewById(R.id.endDatePicker);
        edPicker.setVisibility(GONE);
        if(!pr.isNoEndDate()){
            String end = edDay + "-" + edMonth + "-" + edYear;
            TextView displayEndDate = (TextView) findViewById(R.id.displayEndDate);
            displayEndDate.setText(end);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deleteReminder(int id) throws UnsupportedEncodingException, ExecutionException, InterruptedException {
        DatabaseHelper dbHelper = new DatabaseHelper(DatabaseHelper.DELETE,DatabaseHelper.PILL_REMINDER);
        dbHelper.encodeData("id",String.valueOf(id));
        if(Integer.parseInt(dbHelper.send()) == 1) {
            deleteAlarm();
            Toast toast = Toast.makeText(getApplicationContext(), "Reminder deleted successfully", Toast.LENGTH_SHORT);
            toast.show();
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deleteAlarm() {
        LocalDate date = pr.getStart_date();
        String strDate = "";
        strDate += String.valueOf(date.getYear());
        if(date.getMonthValue() < 10) {
            strDate += "0";
        }
        strDate += String.valueOf(date.getMonthValue());
        if(date.getDayOfMonth() < 10) {
            strDate += "0";
        }
        strDate += String.valueOf(date.getDayOfMonth());
        PillReminderAlarmHelper removeAlarm = new PillReminderAlarmHelper(getApplicationContext(), pr.getType(), pr.getFrequency(), pr.getWeek_bit(), pr.getTime(), strDate, pr.getPillReminderId());
        removeAlarm.deleteAlarm();
    }

    /**
     * Automatically fill in the original data.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void putDataIntoInputField() {
        ImageView pillImage = (ImageView) findViewById(R.id.PillImage);
        Picasso.get().load(pr.getMedicine().getImagePath()).into(pillImage);

        TextView pillName = (TextView) findViewById(R.id.PillName);
        pillName.setText(pr.getMedicine().getMedicineName());

        TextView pillManufacturer = (TextView) findViewById(R.id.PillManufacturer);
        pillManufacturer.setText(pr.getMedicine().getManufacturer());

        TextView pillDosage = (TextView) findViewById(R.id.PillDosage);
        String dosage = String.valueOf(pr.getMedicine().getDosage()) + " mg";
        pillDosage.setText(dosage);

        prType = pr.getType();

        View daysIntervalLayout = (View) findViewById(R.id.DaysIntervalInputField);
        View daysOfWeekLayout = (View) findViewById(R.id.DaysOfWeekInputField);
        switch(prType){
            case 1:
                // Case 0 : Everyday
                daysIntervalLayout.setVisibility(GONE);
                daysOfWeekLayout.setVisibility(GONE);
                break;
            case 2:
                // Case 1 : Every N days (Days Interval)
                daysIntervalLayout.setVisibility(View.VISIBLE);
                daysOfWeekLayout.setVisibility(GONE);
                EditText editDaysInterval = (EditText) findViewById(R.id.editPillReminderDaysInterval);
                editDaysInterval.setText(String.valueOf(pr.getFrequency()));
                break;
            case 3:
                // Case 2 : Specific days of week
                daysIntervalLayout.setVisibility(GONE);
                daysOfWeekLayout.setVisibility(View.VISIBLE);
                for(int i=0; i<7; i++){
                    if(pr.getWeek_bit().charAt(i) == '1'){
                        setDayOfWeekCheckbox(i);
                    }
                }
                break;
        }

        time = pr.getTime().substring(0,2) + pr.getTime().substring(2);
        hour = Integer.parseInt(time.substring(0,2));
        minute = Integer.parseInt(time.substring(2));

        EditText noPill = (EditText) findViewById(R.id.editPillReminderNoPill);
        noPill.setText(String.valueOf(pr.getQuantity()));

        startDate = "";
        sdYear = pr.getStart_date().getYear();
        startDate += String.valueOf(sdYear);
        sdMonth = pr.getStart_date().getMonthValue();
        if(sdMonth < 10) {
            startDate += "0";
        }
        startDate += String.valueOf(sdMonth);
        sdDay = pr.getStart_date().getDayOfMonth();
        if(sdDay < 10) {
            startDate += "0";
        }
        startDate += String.valueOf(sdDay);

        if(pr.isNoEndDate()){
            endDate = "";
        }else{
            endDate = "";
            edYear = pr.getEnd_date().getYear();
            endDate += String.valueOf(edYear);
            edMonth = pr.getEnd_date().getMonthValue();
            if(edMonth < 10) {
                endDate += "0";
            }
            endDate += String.valueOf(edMonth);
            edDay = pr.getEnd_date().getDayOfMonth();
            if(edDay < 10) {
                endDate += "0";
            }
            endDate += String.valueOf(edDay);
        }

    }

    /**
     * A function to set day of week checkbox.
     * @param index Day of week (starting from 0 which represents Monday)
     */
    public void setDayOfWeekCheckbox(int index){
        CheckBox checkbox = null;
        switch(index){
            case 0:
                checkbox = (CheckBox) findViewById(R.id.monday);
                break;
            case 1:
                checkbox = (CheckBox) findViewById(R.id.tuesday);
                break;
            case 2:
                checkbox = (CheckBox) findViewById(R.id.wednesday);
                break;
            case 3:
                checkbox = (CheckBox) findViewById(R.id.thursday);
                break;
            case 4:
                checkbox = (CheckBox) findViewById(R.id.friday);
                break;
            case 5:
                checkbox = (CheckBox) findViewById(R.id.saturday);
                break;
            case 6:
                checkbox = (CheckBox) findViewById(R.id.sunday);
                break;
        }
        checkbox.setChecked(true);
    }

    /**
     * Get user input and validate input.
     * If inputs are valid, send the data to database.
     * @throws UnsupportedEncodingException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    // Validate user input
    public void getUserInput() throws UnsupportedEncodingException, ExecutionException, InterruptedException {

        // Indicate if there is invalid input field
        boolean warning = false;

        // ScrollView
        ScrollView scroll = (ScrollView) findViewById(R.id.editPillReminderScrollView);

        // Y positions that might need to scroll to later
        int daysIntervalY = (int) ((RelativeLayout) findViewById(R.id.frequencyLayout)).getY();
        int daysOfWeekY = (int) ((RelativeLayout) findViewById(R.id.frequencyLayout)).getY();
        int timeY = (int) ((RelativeLayout) findViewById(R.id.editPillTimeLayout)).getY();
        int quantityY = (int) ((RelativeLayout) findViewById(R.id.NoPillLayout)).getY();
        int endDateY = (int) ((RelativeLayout) findViewById(R.id.endDateLayout)).getY();

        // Get frequency
        int typeInput = prType;

        // Get days interval
        int daysInterval = 0;
        if(typeInput == 2){
            EditText daysIntervalInputField = (EditText) findViewById(R.id.editPillReminderDaysInterval);
            if(TextUtils.isEmpty(daysIntervalInputField.getText().toString())) {
                daysIntervalInputField.setError("Please enter the days interval.");
                if(!warning){
                    scroll.scrollTo(0,daysIntervalY);
                    warning = true;
                }
            }else{
                int daysIntervalInput = Integer.parseInt(daysIntervalInputField.getText().toString());
                if(daysIntervalInput < 2){
                    daysIntervalInputField.setError("Days interval must be more than 2.");
                    if(!warning){
                        scroll.scrollTo(0,daysIntervalY);
                        warning = true;
                    }
                }else{
                    daysInterval = daysIntervalInput;
                }
            }
        }

        // Get days of week
        String week_bit = "";
        if(typeInput == 3){
            week_bit += ((CheckBox) findViewById(R.id.monday)).isChecked()?"1":"0";
            week_bit += ((CheckBox) findViewById(R.id.tuesday)).isChecked()?"1":"0";
            week_bit += ((CheckBox) findViewById(R.id.wednesday)).isChecked()?"1":"0";
            week_bit += ((CheckBox) findViewById(R.id.thursday)).isChecked()?"1":"0";
            week_bit += ((CheckBox) findViewById(R.id.friday)).isChecked()?"1":"0";
            week_bit += ((CheckBox) findViewById(R.id.saturday)).isChecked()?"1":"0";
            week_bit += ((CheckBox) findViewById(R.id.sunday)).isChecked()?"1":"0";
            TextView daysOfWeekErrMessage = (TextView) findViewById(R.id.DaysOfWeekErrorMessage);
            if(week_bit.equals("0000000")){
                if(!warning){
                    scroll.scrollTo(0,daysOfWeekY);
                    warning = true;
                }
                daysOfWeekErrMessage.setVisibility(View.VISIBLE);
            }else{
                daysOfWeekErrMessage.setVisibility(GONE);
            }
        }

        // Get pill reminder time
        String timeInput = time;
        TextView timeErrMessage = (TextView) findViewById(R.id.PillReminderTimeErrorMessage);
        if(timeInput.equals("")){
            timeErrMessage.setVisibility(View.VISIBLE);
            if(!warning){
                scroll.scrollTo(0,timeY);
                warning = true;
            }
        }else{
            timeErrMessage.setVisibility(View.GONE);
        }

        // Get number of pills
        int quantityInput = 0;
        EditText quantityInputField = (EditText) findViewById(R.id.editPillReminderNoPill);
        if(TextUtils.isEmpty(quantityInputField.getText().toString())) {
            quantityInputField.setError("Please enter the number of pills.");
            if(!warning){
                scroll.scrollTo(0,quantityY);
                warning = true;
            }
        }else{
            quantityInput = Integer.parseInt(quantityInputField.getText().toString());
            if(quantityInput <= 0){
                quantityInputField.setError("Please enter a valid number.");
                if(!warning){
                    scroll.scrollTo(0,quantityY);
                    warning = true;
                }
            }
        }

        // Get start date
        String startDateInput = startDate;

        // Get end date
        String endDateInput = endDate;
        if(!endDateInput.isEmpty()){
            LocalDate sd = LocalDate.of(sdYear,sdMonth,sdDay);
            LocalDate ed = LocalDate.of(edYear,edMonth,edDay);
            TextView edErrMessage = (TextView) findViewById(R.id.endDateErrorMessage);
            if(sd.isAfter(ed)){
                edErrMessage.setVisibility(View.VISIBLE);
                if(!warning){
                    scroll.scrollTo(0,endDateY);
                    warning = true;
                }
            }else{
                edErrMessage.setVisibility(View.GONE);
            }
        }

        if(!warning){
            sendData(pr.getPillReminderId(),prType,daysInterval,week_bit,time,quantityInput,startDateInput,endDateInput);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendData(int id, int prType, int daysInterval, String week_bit, String time, int quantity, String startDate, String endDate) throws UnsupportedEncodingException, ExecutionException, InterruptedException {
        DatabaseHelper dbHelper = new DatabaseHelper(DatabaseHelper.UPDATE,DatabaseHelper.PILL_REMINDER);
        dbHelper.encodeData("id", String.valueOf(id));
        dbHelper.encodeData("type", String.valueOf(prType));
        dbHelper.encodeData("frequency", String.valueOf(daysInterval));
        dbHelper.encodeData("week_bit",week_bit);
        dbHelper.encodeData("time", time);
        dbHelper.encodeData("quantity",String.valueOf(quantity));
        dbHelper.encodeData("start_date", startDate);
        dbHelper.encodeData("end_date", endDate);

        if(Integer.parseInt(dbHelper.send()) == 1) {
            updateAlarm(id, prType, daysInterval, week_bit, time, startDate);
            Toast toast = Toast.makeText(getApplicationContext(), "Reminder has been changed", Toast.LENGTH_SHORT);
            toast.show();
            setResult(Activity.RESULT_OK);
            finish();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Failed to update pill reminder.", Toast.LENGTH_SHORT);
            toast.show();
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateAlarm(int id, int type, int frequency, String week_bit, String time, String startDate) {
        LocalDate date = pr.getStart_date();
        String strDate = "";
        strDate += String.valueOf(date.getYear());
        if(date.getMonthValue() < 10) {
            strDate += "0";
        }
        strDate += String.valueOf(date.getMonthValue());
        if(date.getDayOfMonth() < 10) {
            strDate += "0";
        }
        strDate += String.valueOf(date.getDayOfMonth());
        PillReminderAlarmHelper removeAlarm = new PillReminderAlarmHelper(getApplicationContext(), pr.getType(), pr.getFrequency(), pr.getWeek_bit(), pr.getTime(), strDate, pr.getPillReminderId());
        removeAlarm.deleteAlarm();

        PillReminderAlarmHelper newAlarm = new PillReminderAlarmHelper(getApplicationContext(), type, frequency, week_bit, time, startDate, id);
        newAlarm.setAlarm();
    }

    /**
     * Hide all error messages.
     */
    public void hideAllErrorMessage(){
        // Error message for days of week
        TextView daysOfWeekErrMessage = (TextView) findViewById(R.id.DaysOfWeekErrorMessage);
        daysOfWeekErrMessage.setVisibility(GONE);
        // Error message for time
        TextView timeErrMessage = (TextView) findViewById(R.id.PillReminderTimeErrorMessage);
        timeErrMessage.setVisibility(GONE);
        // Error message for end date
        TextView endDateErrMessage = (TextView) findViewById(R.id.endDateErrorMessage);
        endDateErrMessage.setVisibility(GONE);

        // Note: Other input fields can use 'setError' to show error messages
    }

    /**
     * Handle changes on the days interval field.
     * @param parent
     * @param view
     * @param position Position of the selected item.
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        View daysIntervalLayout = (View) findViewById(R.id.DaysIntervalInputField);
        View daysOfWeekLayout = (View) findViewById(R.id.DaysOfWeekInputField);
        switch (position) {
            case 0:
                // Case 0 : Everyday
                daysIntervalLayout.setVisibility(GONE);
                daysOfWeekLayout.setVisibility(GONE);
                break;
            case 1:
                // Case 1 : Every N days (Days Interval)
                daysIntervalLayout.setVisibility(View.VISIBLE);
                daysOfWeekLayout.setVisibility(GONE);
                break;
            case 2:
                // Case 2 : Specific days of week
                daysIntervalLayout.setVisibility(GONE);
                daysOfWeekLayout.setVisibility(View.VISIBLE);
                break;
        }
        // Store current option
        prType = position + 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    /**
     * Handle the changes on the time picker.
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onclickTimePicker(View view){
        ImageButton button = (ImageButton) findViewById(R.id.clockButtonId);
        RelativeLayout timePicker = (RelativeLayout) findViewById(R.id.time_picker);
        if(timePicker.getVisibility() == GONE){
            timePicker.setVisibility(View.VISIBLE);
            button.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            TimePicker timeInput = (TimePicker) findViewById(R.id.editPillReminderTime);
            timeInput.setIs24HourView(false);
            if(time == ""){
                timeInput.setHour(LocalTime.now().getHour());
                timeInput.setMinute(LocalTime.now().getMinute());
            }else{
                timeInput.setHour(hour);
                timeInput.setMinute(minute);
            }
            timeInput.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker timePicker, int h, int m) {
                    setInputTime(h,m);
                    String time = to12HourFormat(h,m);
                    TextView displayTime = (TextView) findViewById(R.id.displayTime);
                    displayTime.setText(time);
                }
            });
        }else{
            timePicker.setVisibility(GONE);
            button.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        }
    }

    /**
     * Generate 12-Hour format time string.
     * @param h Hour in 24-hour format.
     * @param m Minute.
     * @return 12-Hour format time string.
     */
    public String to12HourFormat(int h, int m){
        String AMPM;
        String time = "";
        if(h>12){
            AMPM = "PM";
            h -= 12;
        }else if(h==12){
            AMPM = "PM";
        }else{
            if(h==0){
                h += 12;
            }
            AMPM = "AM";
        }
        if(h<10){
            time += "0";
        }
        time += h;
        time += ":";
        if(m<10){
            time += "0";
        }
        time += m;
        return (time + " " + AMPM);
    }

    /**
     * Store user input time.
     * @param h Hour
     * @param m Minute
     */
    public void setInputTime(int h, int m){
        hour = h;
        minute = m;
        String hourZero = "";
        if(hour < 10){
            hourZero = "0";
        }
        String minuteZero = "";
        if(minute < 10){
            minuteZero = "0";
        }
        time = hourZero + String.valueOf(h) + minuteZero + String.valueOf(m);
    }

    /**
     * Handle the changes on start date picker.
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onclickStartDatePicker(View view){
        ImageButton button = (ImageButton) findViewById(R.id.startDateButtonId);
        RelativeLayout sdPicker = (RelativeLayout) findViewById(R.id.startDatePicker);
        if(sdPicker.getVisibility() == GONE){
            // If date picker is hidden, change visibility to visible
            sdPicker.setVisibility(View.VISIBLE);
            // Change arrow down to arrow up
            button.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            // Set previous input (or default input) to the date picker
            DatePicker sdInput = (DatePicker) findViewById(R.id.editPillReminderStartDate);
            sdInput.updateDate(sdYear,sdMonth-1,sdDay);
            // Handle date picker onchange
            sdInput.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // Store input
                    setInputStartDate(year,monthOfYear+1,dayOfMonth);
                    // Display user input
                    String result = dayOfMonth + "-" + (monthOfYear+1) + "-" + year;
                    TextView displayTime = (TextView) findViewById(R.id.displayStartDate);
                    displayTime.setText(result);
                }
            });
        }else{
            // Hide start date picker
            sdPicker.setVisibility(GONE);
            // Change arrow up to arrow down
            button.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        }
    }

    /**
     * Store user input start date.
     * @param year Year.
     * @param month Month.
     * @param day Day.
     */
    public void setInputStartDate(int year, int month, int day){
        startDate = "";

        sdYear = year;
        startDate += String.valueOf(year);

        sdMonth = month;
        if(month < 10) {
            startDate += "0";
        }
        startDate += String.valueOf(month);

        sdDay = day;
        if(day < 10) {
            startDate += "0";
        }
        startDate += String.valueOf(day);
    }

    /**
     * Handle changes on end date picker.
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onclickEndDatePicker(View view){
        ImageButton button = (ImageButton) findViewById(R.id.endDateButtonId);
        RelativeLayout edPicker = (RelativeLayout) findViewById(R.id.endDatePicker);
        if(edPicker.getVisibility() == GONE){
            // Show end date picker
            edPicker.setVisibility(View.VISIBLE);
            // Change arrow down to arrow up
            button.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            DatePicker edInput = (DatePicker) findViewById(R.id.editPillReminderEndDate);
            if(endDate == ""){
                // Set current date (Default)
                edInput.updateDate(LocalDate.now().getYear(),LocalDate.now().getMonthValue()-1,LocalDate.now().getDayOfMonth());
            }else{
                // Set previous input
                edInput.updateDate(edYear,edMonth-1,edDay);
            }
            // Handle end date picker onclick
            edInput.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // Store input
                    setInputEndDate(year,monthOfYear+1,dayOfMonth);
                    // Display user input
                    String result = dayOfMonth + "-" + (monthOfYear+1) + "-" + year;
                    TextView displayTime = (TextView) findViewById(R.id.displayEndDate);
                    displayTime.setText(result);
                }
            });
        }else{
            // Hide end date picker
            edPicker.setVisibility(GONE);
            // Change arrow up to arrow down
            button.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        }
    }

    /**
     * Store user input end date.
     * @param year Year.
     * @param month Month.
     * @param day Day.
     */
    public void setInputEndDate(int year, int month, int day){
        endDate = "";

        edYear = year;
        endDate += String.valueOf(year);

        edMonth = month;
        if(month < 10) {
            endDate += "0";
        }
        endDate += String.valueOf(month);

        edDay = day;
        if(day < 10) {
            endDate += "0";
        }
        endDate += String.valueOf(day);
    }
}