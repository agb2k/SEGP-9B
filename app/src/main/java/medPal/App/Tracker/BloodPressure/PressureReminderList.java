package medPal.App.Tracker.BloodPressure;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import medPal.App.R;
import medPal.App.Tracker.Alarm.AlarmCursorAdapter;
import medPal.App.Tracker.Alarm.AlarmReminderContract;
import medPal.App.Tracker.Alarm.AlarmReminderDbHelper;

public class PressureReminderList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private FloatingActionButton mAddReminderButton;
    private Toolbar mToolbar;
    AlarmCursorAdapter mCursorAdapter;
    AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(this);
    ListView reminderListView;
    ProgressDialog prgDialog;
    TextView reminderText1;
    ImageView activeOn;
    ImageView activeOff;
    private String alarmTitle = "";

    private static final int VEHICLE_LOADER = 0;


    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, PressureReminderList.class);
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure_reminder_list);
        getSupportActionBar().setTitle("Pressure Reminder List");


        reminderListView = (ListView) findViewById(R.id.list1234);
        View emptyView = findViewById(R.id.empty_view2);
        reminderListView.setEmptyView(emptyView);
        reminderText1 = (TextView) findViewById(R.id.reminderTextPressure);



        mCursorAdapter = new AlarmCursorAdapter(this, null);
        reminderListView.setAdapter(mCursorAdapter);

        reminderListView.setOnItemClickListener((adapterView, view, position, id) -> {


            Intent intent = new Intent(PressureReminderList.this, NewPressureReminder.class);

            Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, id);

            // Set the URI on the data field of the intent
            intent.setData(currentVehicleUri);


            PendingIntent operation = TaskStackBuilder.create(this).addNextIntentWithParentStack(intent).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Cursor cursor = getContentResolver().query(currentVehicleUri, null, null, null, null);

            String description = "";
            try{
                if (cursor!=null && cursor.moveToFirst()){
                    description = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
                }
            } finally{
                if (cursor != null){
                    cursor.close();
                }
            }

            startActivity(intent);


        });


        mAddReminderButton = (FloatingActionButton) findViewById(R.id.fab2);

        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewPressureReminder();
            }
        });

        getSupportLoaderManager().initLoader(VEHICLE_LOADER, null, this);
        LoaderManager.getInstance(this);


    }
    /*
    private void openNewPressureReminder() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set reminder title");

        final EditText input =  new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty()) {
                    return;
                }

                alarmTitle = input.getText().toString();
                ContentValues values = new ContentValues();

                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, alarmTitle);

                Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

                restartLoader();

                if (newUri == null) {
                    Toast.makeText(getApplicationContext(), "Setting Reminder Title failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Title set successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }



        });
        builder.show();
    }

     */
    private void openNewPressureReminder(){
        Intent intent1 = new Intent(this, NewPressureReminder.class);
        startActivity(intent1);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE

        };

        return new CursorLoader(this,   // Parent activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
        if (cursor.getCount() > 0){
            reminderText1.setVisibility(View.VISIBLE);
        }else{
            reminderText1.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }

    public void restartLoader(){
        getLoaderManager().restartLoader(VEHICLE_LOADER, null, null);
        LoaderManager.getInstance(this);
    }

    /*public void setAlarmOn(View v){
        activeOn = (ImageView) findViewById(R.id.active_image);
        activeOn.setVisibility(View.VISIBLE);
        activeOff = (ImageView) findViewById(R.id.active_image2);
        activeOff.setVisibility(View.GONE);

    }

    public void setAlarmOff(View v){

    }*/
}