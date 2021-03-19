package medPal.App.PillReminder;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;

import medPal.App.DatabaseHelper;
import medPal.App.R;

import static android.view.View.GONE;

/**
 * Activity to add new pill reminder.
 */
public class NewPillReminder extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private ImageButton uploadImageButton;
    private Bitmap bitmap = null;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pill_reminder);
        getSupportActionBar().setTitle("New Pill Reminder");

        // Hide all error messages
        hideAllErrorMessage();

        // Confirm button onclick
        Button addReminderButton = (Button) findViewById(R.id.newPillReminderConfirm);
        addReminderButton.setOnClickListener(new View.OnClickListener() {
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

        // Hide days interval and days of week input fields
        View daysIntervalLayout = (View) findViewById(R.id.DaysIntervalInputField);
        View daysOfWeekLayout = (View) findViewById(R.id.DaysOfWeekInputField);
        daysIntervalLayout.setVisibility(GONE);
        daysOfWeekLayout.setVisibility(GONE);

        // Spinner element for pill reminder frequency
        final Spinner frequency = (Spinner) findViewById(R.id.NewPillReminderType);
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

        // Hide time picker for pill reminder time
        RelativeLayout timePicker = (RelativeLayout) findViewById(R.id.time_picker);
        timePicker.setVisibility(GONE);
        // Automatically set current time in time picker
        TimePicker timeInput = (TimePicker) findViewById(R.id.NewPillReminderTime);
        timeInput.setIs24HourView(false);
        timeInput.setHour(LocalTime.now().getHour());
        timeInput.setMinute(LocalTime.now().getMinute());

        // Hide date picker for pill reminder starting date
        RelativeLayout sdPicker = (RelativeLayout) findViewById(R.id.startDatePicker);
        sdPicker.setVisibility(GONE);
        // Automatically set today as starting date
        int year = LocalDate.now().getYear();
        int monthOfYear = LocalDate.now().getMonthValue();
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        setInputStartDate(year,monthOfYear,dayOfMonth);
        String result = dayOfMonth + "-" + monthOfYear + "-" + year;
        TextView displayTime = (TextView) findViewById(R.id.displayStartDate);
        displayTime.setText(result);

        // Hide date picker for pill reminder ending date
        RelativeLayout edPicker = (RelativeLayout) findViewById(R.id.endDatePicker);
        edPicker.setVisibility(GONE);

        // Upload image feature
        uploadImageButton = (ImageButton) findViewById(R.id.NewPillReminderImage);
        uploadImageButton.setOnClickListener(event -> {
            if(bitmap == null){
                showSourceOption();
            }else{
                showActionOption();
            }
        });
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
     * Get user input and validate user input.
     * If inputs are valid, send the data to database.
     * @throws UnsupportedEncodingException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getUserInput() throws UnsupportedEncodingException, ExecutionException, InterruptedException {

        // Indicate if there is invalid input field
        boolean warning = false;

        // Y positions that might need to scroll to later
        int medicineNameY = (int) ((RelativeLayout) findViewById(R.id.NewMedicineNameLayout)).getY();
        int dosageY = (int) ((RelativeLayout) findViewById(R.id.DosageLayout)).getY();
        int daysIntervalY = (int) ((RelativeLayout) findViewById(R.id.frequencyLayout)).getY();
        int daysOfWeekY = (int) ((RelativeLayout) findViewById(R.id.frequencyLayout)).getY();
        int timeY = (int) ((RelativeLayout) findViewById(R.id.NewPillTimeLayout)).getY();
        int quantityY = (int) ((RelativeLayout) findViewById(R.id.NoPillLayout)).getY();
        int endDateY = (int) ((RelativeLayout) findViewById(R.id.endDateLayout)).getY();

        // Get medicine name
        String medicineNameInput = "";
        EditText medicineNameInputField = (EditText) findViewById(R.id.NewPillReminderMedicineName);
        if(TextUtils.isEmpty(medicineNameInputField.getText().toString())) {
            medicineNameInputField.setError("Please enter the medicine name.");
            ((ScrollView)findViewById(R.id.NewPillReminderScrollView)).scrollTo(0,medicineNameY);
            warning = true;
        }else{
            medicineNameInput = medicineNameInputField.getText().toString();
        }

        // Get manufacturer name (Optional)
        String manufacturerInput = ((EditText) findViewById(R.id.NewPillReminderManufacturer)).getText().toString();

        // Get dosage(strength)
        int dosageInput = 0;
        EditText dosageInputField = (EditText) findViewById(R.id.NewPillReminderDosage);
        if(TextUtils.isEmpty(dosageInputField.getText().toString())) {
            dosageInputField.setError("Please enter the strength of the medicine.");
            if(!warning){
                ((ScrollView)findViewById(R.id.NewPillReminderScrollView)).scrollTo(0,dosageY);
                warning = true;
            }
        }else{
            dosageInput = Integer.parseInt(dosageInputField.getText().toString());
            if(dosageInput <= 0){
                dosageInputField.setError("Please enter a valid number.");
                if(!warning){
                    ((ScrollView)findViewById(R.id.NewPillReminderScrollView)).scrollTo(0,dosageY);
                    warning = true;
                }
            }
        }

        // Get frequency
        int typeInput = prType;

        // Get days interval
        int daysInterval = 0;
        if(typeInput == 2){
            EditText daysIntervalInputField = (EditText) findViewById(R.id.NewPillReminderDaysInterval);
            if(TextUtils.isEmpty(daysIntervalInputField.getText().toString())) {
                daysIntervalInputField.setError("Please enter the days interval.");
                if(!warning){
                    ((ScrollView)findViewById(R.id.NewPillReminderScrollView)).scrollTo(0,daysIntervalY);
                    warning = true;
                }
            }else{
                int daysIntervalInput = Integer.parseInt(daysIntervalInputField.getText().toString());
                if(daysIntervalInput < 2){
                    daysIntervalInputField.setError("Days interval must be more than 2.");
                    if(!warning){
                        ((ScrollView)findViewById(R.id.NewPillReminderScrollView)).scrollTo(0,daysIntervalY);
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
                    ((ScrollView)findViewById(R.id.NewPillReminderScrollView)).scrollTo(0,daysOfWeekY);
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
                ((ScrollView)findViewById(R.id.NewPillReminderScrollView)).scrollTo(0,timeY);
                warning = true;
            }
        }else{
            timeErrMessage.setVisibility(View.GONE);
        }

        // Get number of pills
        int quantityInput = 0;
        EditText quantityInputField = (EditText) findViewById(R.id.NewPillReminderNoPill);
        if(TextUtils.isEmpty(quantityInputField.getText().toString())) {
            quantityInputField.setError("Please enter the number of pills.");
            if(!warning){
                ((ScrollView)findViewById(R.id.NewPillReminderScrollView)).scrollTo(0,quantityY);
                warning = true;
            }
        }else{
            quantityInput = Integer.parseInt(quantityInputField.getText().toString());
            if(quantityInput <= 0){
                quantityInputField.setError("Please enter a valid number.");
                if(!warning){
                    ((ScrollView)findViewById(R.id.NewPillReminderScrollView)).scrollTo(0,quantityY);
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
                    ((ScrollView)findViewById(R.id.NewPillReminderScrollView)).scrollTo(0,endDateY);
                    warning = true;
                }
            }else{
                edErrMessage.setVisibility(View.GONE);
            }
        }

        // Get purpose
        String purposeInput = ((EditText) findViewById(R.id.NewPillReminderMedicinePurpose)).getText().toString();
        // Get remarks
        String remarkInput = ((EditText) findViewById(R.id.NewPillReminderMedicineRemark)).getText().toString();

        if(!warning){
            sendData(medicineNameInput, manufacturerInput, dosageInput, prType, daysInterval, week_bit, time, quantityInput, startDateInput, endDateInput, purposeInput, remarkInput, bitmap);
        }

    }

    /**
     * Insert to database.
     * @param medicineName
     * @param manufacturer
     * @param dosage
     * @param prType
     * @param daysInterval
     * @param week_bit
     * @param time
     * @param quantity
     * @param startDate
     * @param endDate
     * @param purpose
     * @param remark
     * @param bitmap
     * @throws UnsupportedEncodingException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendData(String medicineName, String manufacturer, int dosage, int prType, int daysInterval, String week_bit, String time, int quantity, String startDate, String endDate, String purpose, String remark, Bitmap bitmap) throws UnsupportedEncodingException, ExecutionException, InterruptedException {
        DatabaseHelper insertDB = new DatabaseHelper(DatabaseHelper.INSERT,DatabaseHelper.PILL_REMINDER);
        insertDB.encodeData("medicine",medicineName);
        insertDB.encodeData("manufacturer",manufacturer);
        insertDB.encodeData("dosage", String.valueOf(dosage));
        insertDB.encodeData("type",String.valueOf(prType));
        insertDB.encodeData("frequency", String.valueOf(daysInterval));
        insertDB.encodeData("week_bit",week_bit);
        insertDB.encodeData("time", time);
        insertDB.encodeData("quantity",String.valueOf(quantity));
        insertDB.encodeData("start_date",startDate);
        insertDB.encodeData("end_date",endDate);
        insertDB.encodeData("purpose",purpose);
        insertDB.encodeData("remark", remark);

        String message = insertDB.send();
        if(message.length() > 1) {
            message = message.substring(1,message.length()-1);
            String medicineId = message.split(",")[1];
            int pill_reminder_id = Integer.parseInt(message.split(",")[2]);
            if(bitmap != null) {
                DatabaseHelper imageDB = new DatabaseHelper(DatabaseHelper.INSERT, DatabaseHelper.MEDICINE_IMAGE);
                imageDB.encodeData("medId", medicineId);
                imageDB.encodeImage(bitmap);
                message = imageDB.send();
                if(Integer.parseInt(message) == 1) {
                    setAlarm(prType,daysInterval,week_bit,time,startDate,pill_reminder_id);
                    Toast toast = Toast.makeText(getApplicationContext(), "Reminder added successfully", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Failed to upload image.,", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
            }else{
                setAlarm(prType,daysInterval,week_bit,time,startDate,pill_reminder_id);
                Toast toast = Toast.makeText(getApplicationContext(), "Reminder added successfully", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Failed to add new pill reminder.", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAlarm(int prType, int daysInterval, String week_bit, String time, String startDate, int pill_reminder_id) {
        PillReminderAlarmHelper alarmHelper = new PillReminderAlarmHelper(getApplicationContext(),prType,daysInterval,week_bit,time,startDate,pill_reminder_id);
        alarmHelper.setAlarm();
    }

    /**
     * Handles the changes on frequency selector.
     * @param parent
     * @param view
     * @param position
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
     * Handle the changes on time picker.
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onclickTimePicker(View view){
        ImageButton button = (ImageButton) findViewById(R.id.clockButtonId);
        RelativeLayout timePicker = (RelativeLayout) findViewById(R.id.time_picker);
        if(timePicker.getVisibility() == GONE){
            timePicker.setVisibility(View.VISIBLE);
            button.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            TimePicker timeInput = (TimePicker) findViewById(R.id.NewPillReminderTime);
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
                    String result = time + " " + AMPM;
                    TextView displayTime = (TextView) findViewById(R.id.displayTime);
                    displayTime.setText(result);
                }
            });
        }else{
            timePicker.setVisibility(GONE);
            button.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        }
    }

    /**
     * Store user input time.
     * @param h Hour.
     * @param m Minute.
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
            DatePicker sdInput = (DatePicker) findViewById(R.id.NewPillReminderStartDate);
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
     * Handle the changes on end date picker.
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
            DatePicker edInput = (DatePicker) findViewById(R.id.NewPillReminderEndDate);
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

    /**
     * Display dialog for image source options
     */
    public void showSourceOption() {
        AlertDialog.Builder sourceDialog = new AlertDialog.Builder(this);
        sourceDialog.setTitle("Select Image Source");
        sourceDialog.setCancelable(true);
        String[] sourceDialogItems = {"Photo Gallery", "Camera"};
        sourceDialog.setItems(sourceDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case 0:
                        requestGalleryPermission();
                        break;
                    case 1:
                        requestCameraPermission();
                        break;
                }
            }
        });
        sourceDialog.show();
    }

    /**
     * Display dialog for actions on selected pill image.
     */
    public void showActionOption() {
        AlertDialog.Builder actionDialog = new AlertDialog.Builder(this);
        actionDialog.setTitle("Select Action");
        actionDialog.setCancelable(true);
        String[] actionDialogItems = {"Remove Image","Select Other Image"};
        actionDialog.setItems(actionDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        uploadImageButton.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
                        bitmap = null;
                        return;
                    case 1:
                        showSourceOption();
                }
            }
        });
        actionDialog.show();
    }

    /**
     * Request permission for accessing image gallery.
     */
    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            showFileChooser();
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    /**
     * Request permission for accessing camera.
     */
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePhotoFromCamera();
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
    }

    /**
     * Handle user permissions.
     * @param requestCode Request code. 1 for image gallery permission, and 2 for camera permission.
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //Checking the request code of our request
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow in your app.
                    showFileChooser();
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(this, "Unable to access photo gallery", Toast.LENGTH_LONG).show();
                }
                return;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow in your app.
                    takePhotoFromCamera();
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(this, "Unable to access camera.", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    /**
     * Select image from image gallery.
     */
    private void showFileChooser(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 1);
    }

    /**
     * Take picture using camera.
     */
    private void takePhotoFromCamera(){
        Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 2);
    }

    /**
     * Get bitmap from image gallery or camera.
     * @param requestCode Request code. 1 for image gallery and 2 for camera.
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == 1) {
                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(requestCode == 2){
                bitmap = (Bitmap) data.getExtras().get("data");
            }
            uploadImageButton.setImageBitmap(bitmap);
        }
    }

}

