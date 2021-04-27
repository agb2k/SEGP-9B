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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import medPal.App.DatabaseHelper;
import medPal.App.R;

public class ForgotPassword extends AppCompatActivity {
    //Create key for Intent to start VerifyEmail Activity
    public final static String EXTRA_RESETPASSWORDEMAIL = "com.medPal.App.login_signup.EXTRA_RESETPASSWORDEMAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Access UI Element
        EditText email_view = (EditText) findViewById(R.id.forgotPassword_email);
        Button sendbutton_view = (Button) findViewById(R.id.forgotPassword_sendButton);
        TextView backToLogin_view = (TextView) findViewById(R.id.forgotPassword_backToLogin);
        ProgressBar forgotPassword_progress = (ProgressBar) findViewById(R.id.forgotPassword_progress);
        forgotPassword_progress.setVisibility(View.GONE);

        //set OnClick Listener for TextView to go to Login Activity
        backToLogin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set OnClick Listener for Button to send Email
        sendbutton_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get input from View Element 'EditText'
                String email_input = email_view.getText().toString();

                //check if all input fields id filled
                if (email_input != null) {
                    //Start ProgressBar (Set visibility VISIBLE)
                    forgotPassword_progress.setVisibility(View.VISIBLE);
                    //set event handler for onClick
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //DATABASE CONNECTION CHANGE http://hpyzl1.jupiter.nottingham.edu.my/Login_php/login.php
                            try {
                                DatabaseHelper dbHelper = new DatabaseHelper("http://hpyzl1.jupiter.nottingham.edu.my/medpal-db/LoginSystem/sendForgotPasswordEmail.php");
                                //DatabaseHelper dbHelper = new DatabaseHelper("http://hpyzl1.jupiter.nottingham.edu.my/Login_php/login.php");
                                dbHelper.encodeData("email",email_input);

                                String result = dbHelper.send();
                                result = result.substring(1,result.length()-1);
                                //End ProgressBar (Set visibility to GONE)
                                forgotPassword_progress.setVisibility(View.GONE);
                                Log.i("PutData", result);
                                //check php echo reply 'result'
                                if (result.equals("Reset password email sent")) {
                                    Toast toast_resetPasswordEmailSent = Toast.makeText(getApplicationContext(), R.string.forgotPassword_resetEmailSentMsg, Toast.LENGTH_SHORT);
                                    toast_resetPasswordEmailSent.show();
                                } else if (result.equals("Account not verified yet. Please request for verification email at Sign Up page")) {
                                    Toast toast_ResetPasswordEmailFail = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                                    toast_ResetPasswordEmailFail.show();
                                    Intent intent_toSignUp = new Intent(getApplicationContext(), SignUp.class);
                                    startActivity(intent_toSignUp);
                                    finish();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                                    toast.show();
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
