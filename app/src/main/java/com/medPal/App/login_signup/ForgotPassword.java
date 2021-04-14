package com.medPal.App.login_signup;

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

import com.medPal.login_signup.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

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
				Intent intent_toLogin = new Intent(getApplicationContext(), Login.class);
				startActivity(intent_toLogin);
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
							//Starting Write and Read data with URL
							//Creating array for parameters
							String[] field = new String[1];
							field[0] = "email";
							//Creating array for data
							String[] data = new String[1];
							data[0] = email_input;
							//DATABASE CONNECTION CHANGE http://hpyzl1.jupiter.nottingham.edu.my/Login_php/login.php
							PutData putData = new PutData("https://bulacke.xyz/medpal-db/sendForgotPasswordEmail.php", "POST", field, data);

							if (putData.startPut()) {
								if (putData.onComplete()) {
									String result = putData.getResult();
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

								}
							}
							//End Write and Read data with URL
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