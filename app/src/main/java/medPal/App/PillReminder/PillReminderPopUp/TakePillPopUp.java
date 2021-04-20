package medPal.App.PillReminder.PillReminderPopUp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TreeMap;

import medPal.App.AlarmAndNotification.AlarmHelper;
import medPal.App.AlarmAndNotification.TimeAndIntervalHelper;
import medPal.App.PillReminder.PillReminder;
import medPal.App.PillReminder.PillReminderController;
import medPal.App.R;

/**
 * This class creates and handles the pop-up window.
 */
public class TakePillPopUp {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("InflateParams")
    public void showPopupWindow(Context context, final View view) throws UnsupportedEncodingException {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.pop_up_pill_reminder, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Get data
        ArrayList<PillReminder> toTake = new ArrayList<>(getData(context));

        // Set time
        TextView title = (TextView) popupView.findViewById(R.id.titleText);
        title.setText(to12Format(toTake.get(0).getTime()));

        // Display pill reminders
        RecyclerView recyclerView = (RecyclerView) popupView.findViewById(R.id.recycler_view);
        PopUpPillReminderAdapter adapter = new PopUpPillReminderAdapter(toTake);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        Button takePill = (Button) popupView.findViewById(R.id.takePillButton);
        takePill.setOnClickListener(event -> {
            takePill(context, toTake,1);
            Toast toast = Toast.makeText(context, "Record saved", Toast.LENGTH_SHORT);
            toast.show();
            popupWindow.dismiss();
        });

        Button snooze = (Button) popupView.findViewById(R.id.snoozeButton);
        snooze.setOnClickListener(event -> {
            snooze(context);
            Toast toast = Toast.makeText(context, "Snooze for 5 minutes", Toast.LENGTH_SHORT);
            toast.show();
            popupWindow.dismiss();
        });

        Button skip = (Button) popupView.findViewById(R.id.skipButton);
        skip.setOnClickListener(event -> {
            takePill(context, toTake,2);
            Toast toast = Toast.makeText(context, "Record saved", Toast.LENGTH_SHORT);
            toast.show();
            popupWindow.dismiss();
        });
    }

    /**
     * Get the pills that need to be taken now.
     * @param context
     * @return List of pill reminders.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<PillReminder> getData(Context context) throws UnsupportedEncodingException {
        // Get all pill reminders that have to be taken today
        TreeMap<LocalTime, ArrayList<PillReminder>> prByTime = new TreeMap<LocalTime, ArrayList<PillReminder>>();
        PillReminderController prController = new PillReminderController(context);
        prByTime = prController.getUpcomingPillReminder();

        // Return the pill reminders of the earliest time slot
        return new ArrayList<>(prByTime.get(prByTime.firstKey()));
    }

    /**
     * When user clicks on take pill or skip option, save the status in database.
     * @param context
     * @param pillReminder List of pill reminders.
     * @param status 1 for taken; 2 for skipped
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void takePill(Context context, ArrayList<PillReminder> pillReminder, int status) {
        LocalDate d = LocalDate.now();
        String date = String.valueOf(d.getYear());
        if(d.getMonthValue() < 10)
            date += "0";
        date += String.valueOf(d.getMonthValue());
        if(d.getDayOfMonth() < 10)
            date += "0";
        date += String.valueOf(d.getDayOfMonth());

        PillTakingDBHelper dbHelper = new PillTakingDBHelper(context);
        for(PillReminder pr : pillReminder) {
            String medicine = pr.getMedicine().getMedicineName() + " " + pr.getMedicine().getDosage() + "mg";
            if(pr.getMedicine().getManufacturer().length() > 0) {
                medicine += " (" + pr.getMedicine().getManufacturer() + ")";
            }

            dbHelper.insertRecord(d.getMonthValue(),date,pr.getTime(),medicine,pr.getQuantity(),status);
        }
    }

    /**
     * When user clicks on 'Snooze' option, set another alarm 5 minutes from now.
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void snooze(Context context) {
        long currentTime = TimeAndIntervalHelper.getMilli(LocalDateTime.now());
        long interval = TimeAndIntervalHelper.getHourInterval() / 12; // Snooze for 5 minutes
        long alarmTime = currentTime + interval;

        AlarmHelper alarmHelper = new AlarmHelper(context, AlarmHelper.PILL_REMINDER);
        alarmHelper.setAlarm(alarmTime,AlarmHelper.SNOOZE_ALARM_REFERENCE_CODE);
    }

    /**
     * Convert 24-hour format time (string) to 12-hour format time (string)
     * @param t
     * @return Time in 12-hour format
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String to12Format(String t) {
        int h = Integer.parseInt(t.substring(0,2));
        int m = Integer.parseInt(t.substring(2));
        LocalTime time = LocalTime.of(h,m);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("hh:mm a");
        return time.format(df);
    }

}
