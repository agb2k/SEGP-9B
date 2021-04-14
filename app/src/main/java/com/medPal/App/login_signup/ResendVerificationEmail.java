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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.medPal.login_signup.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

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
							//Starting Write and Read data with URL
							//Creating array for parameters
							String[] field = new String[1];
							field[0] = "email";
							//Creating array for data
							String[] data = new String[1];
							data[0] = email_input;
							//DATABASE CONNECTION CHANGE http://hpyzl1.jupiter.nottingham.edu.my/Login_php/login.php
							//PutData putData = new PutData("http://192.168.0.112/Android/login.php", "POST", field, data);
							PutData putData = new PutData("https://bulacke.xyz/medpal-db/resendVerificationEmail.php", "POST", field, data);

							if (putData.startPut()) {
								if (putData.onComplete()) {
									String result = putData.getResult();
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
										Intent intent_toLogin = new Intent(getApplicationContext(), Login.class);
										startActivity(intent_toLogin);
										finish();
									} else {
										Toast toast_loginFail = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
										toast_loginFail.show();
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