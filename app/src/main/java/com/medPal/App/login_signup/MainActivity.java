package com.medPal.App.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.medPal.login_signup.R;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView header = (TextView) findViewById(R.id.mainActivity_header);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String email = bundle.getString(Login.EXTRA_LOGINEMAIL);
		String password = bundle.getString(Login.EXTRA_LOGINPASSWORD);
		String headerString = email + "\n\n" + password;
		header.setText(headerString);

	}
}