package medPal.App;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import medPal.App.PillReminder.PillReminderPopUp.TakePillPopUp;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Check if this app is opened from pill reminder notification
        // If yes, it will show the pop up window for taking pill
        // Note: Extra is set in (AlarmAndNotification.Receivers.)PillReminderReceiver
        // Note: Need to wait otherwise the pop up shows too early and will crash (https://stackoverflow.com/a/51436824)
        findViewById(R.id.ReferenceView).post(new Runnable() {
            public void run() {
                boolean showPillReminderPopUp = getIntent().getBooleanExtra("showPillReminderPopUp",false);
                if(showPillReminderPopUp) {
                    // Use fragment view as parent view
                    View view = findViewById(R.id.ReferenceView);
                    TakePillPopUp popUpClass = new TakePillPopUp();
                    popUpClass.showPopupWindow(MainActivity.this,view);
                }
            }
        });

    }

}