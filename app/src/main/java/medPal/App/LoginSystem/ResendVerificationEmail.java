package medPal.App.LoginSystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import medPal.App.DatabaseHelper;
import medPal.App.R;

public class ResendVerificationEmail extends AppCompatActivity {

    //Create key for Intent to start VerifyEmail Activity
    public final static String EXTRA_RESENDEMAIL = "com.medPal.App.login_signup.RESENDEMAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resend_verification_email);

        EditText resendEmail_view = (EditText) findViewById(R.id.resend_email);
        Button sendbutton_view = (Button) findViewById(R.id.resend_button);
        ProgressBar resendProgress_view = (ProgressBar) findViewById(R.id.resend_progress);
        resendProgress_view.setVisibility(View.GONE);

        //set listener for button Send view
        sendbutton_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get input from View Element 'EditText'
                String email_input = resendEmail_view.getText().toString();

                //check if all input fields id filled
                if (email_input != null) {
                    //Start ProgressBar (Set visibility VISIBLE)
                    resendProgress_view.setVisibility(View.VISIBLE);
                    //set event handler for onClick
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //DATABASE CONNECTION CHANGE http://hpyzl1.jupiter.nottingham.edu.my/Login_php/login.php
                            try {
                                DatabaseHelper dbHelper = new DatabaseHelper("http://hpyzl1.jupiter.nottingham.edu.my/medpal-db/resendVerificationEmail.php");
                                //DatabaseHelper dbHelper = new DatabaseHelper("http://hpyzl1.jupiter.nottingham.edu.my/Login_php/login.php");
                                dbHelper.encodeData("email",email_input);

                                String result = dbHelper.send();
                                result = result.substring(1,result.length()-1);
                                //End ProgressBar (Set visibility to GONE)
                                resendProgress_view.setVisibility(View.GONE);
                                Log.i("PutData", result);
                                //check php echo reply 'result'
                                if (result.equals("Verification email sent")) {
                                    Toast toast_resend = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                                    toast_resend.show();
                                    Intent intent_toVerifyEmail = new Intent(getApplicationContext(), VerifyEmail.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(EXTRA_RESENDEMAIL, email_input);
                                    intent_toVerifyEmail.putExtras(bundle);
                                    startActivity(intent_toVerifyEmail);
                                    finish();
                                } else if (result.equals("Account already activated, please log in.")) {
                                    Toast toast_accountalreadyActivated = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                                    toast_accountalreadyActivated.show();
                                    finish();
                                } else {
                                    Toast toast_loginFail = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                                    toast_loginFail.show();
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } /*
				else {
					Toast toast_incompleteField = Toast.makeText(getApplicationContext(), R.string.resend_emailNotGiven, Toast.LENGTH_LONG);
					toast_incompleteField.show();
				}
				*/
            }
        });
    }
}
