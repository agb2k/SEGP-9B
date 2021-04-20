package medPal.App;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import medPal.App.LoginSystem.Login;
import medPal.App.UserIdentification.UserIdentity;
import medPal.App.UserIdentification.UserIdentityDBHelper;

/**
 * This activity checks whether the user is logged in,
 * If not, go to login system
 * If logged in, go to MainActivity
 */
public class Gateway extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway);

        // Check if user has logged in
        UserIdentity identity = UserIdentity.getInstance();
        if(!identity.loggedIn()) {
            // If not logged in
            // Check if the log in detail is in database
            UserIdentityDBHelper dbHelper = new UserIdentityDBHelper(this.getApplicationContext());
            if(dbHelper.isEmpty()) {
                // If no login information, go to login page
                Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                this.startActivity(loginIntent);
                finish();
            }else{
                // Initialize UserIdentity with the data from database
                dbHelper.getRecord();
                startMain();
            }
        }else {
            startMain();
        }
    }

    private void startMain() {
        Intent mainActivity = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}